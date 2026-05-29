package com.example

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class InstallmentViewModel(application: Application) : AndroidViewModel(application) {

    private val database = AppDatabase.getDatabase(application)
    private val dao = database.inquiryDao()

    // Search and Filter States
    private val _selectedCategory = MutableStateFlow<PhoneCategory?>(null)
    val selectedCategory: StateFlow<PhoneCategory?> = _selectedCategory.asStateFlow()

    private val _selectedBrand = MutableStateFlow<String?>(null)
    val selectedBrand: StateFlow<String?> = _selectedBrand.asStateFlow()

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    // Advanced search filters requested by user
    private val _selectedPriceRange = MutableStateFlow(PriceRangeFilter.ALL)
    val selectedPriceRange: StateFlow<PriceRangeFilter> = _selectedPriceRange.asStateFlow()

    private val _selectedDurationFilter = MutableStateFlow(DurationFilter.ALL)
    val selectedDurationFilter: StateFlow<DurationFilter> = _selectedDurationFilter.asStateFlow()

    private val _selectedDownPaymentFilter = MutableStateFlow(DownPaymentFilter.ALL)
    val selectedDownPaymentFilter: StateFlow<DownPaymentFilter> = _selectedDownPaymentFilter.asStateFlow()

    // Filtered mobiles list
    val filteredMobiles: StateFlow<List<MobileDevice>> = combine(
        combine(_selectedCategory, _selectedBrand, _searchQuery) { c, b, q -> Triple(c, b, q) },
        combine(_selectedPriceRange, _selectedDurationFilter, _selectedDownPaymentFilter) { p, d, dp -> Triple(p, d, dp) }
    ) { basic, advanced ->
        val (category, brand, query) = basic
        val (priceRange, duration, downPayment) = advanced
        MobileData.preloadedMobiles.filter { device ->
            val matchesCategory = category == null || device.category == category
            val matchesBrand = brand == null || device.brand.equals(brand, ignoreCase = true)
            val matchesQuery = query.isEmpty() || 
                    device.name.contains(query, ignoreCase = true) ||
                    device.brand.contains(query, ignoreCase = true) ||
                    device.features.any { it.contains(query, ignoreCase = true) }
            
            val matchesPrice = when (priceRange) {
                PriceRangeFilter.ALL -> true
                PriceRangeFilter.UNDER_15K -> device.price < 15000.0
                PriceRangeFilter.FROM_15K_TO_50K -> device.price in 15000.0..50000.0
                PriceRangeFilter.FROM_50K_TO_150K -> device.price in 50000.0..150000.0
                PriceRangeFilter.OVER_150K -> device.price > 150000.0
            }

            val matchesDuration = when (duration) {
                DurationFilter.ALL -> true
                DurationFilter.SHORT -> device.installmentDurationMonths in 5..6
                DurationFilter.LONG -> device.installmentDurationMonths in 10..12
            }

            val matchesDownPayment = when (downPayment) {
                DownPaymentFilter.ALL -> true
                DownPaymentFilter.ZERO_PERCENT -> !device.downPaymentRequired || device.downPaymentPercentage == 0
            }

            matchesCategory && matchesBrand && matchesQuery && matchesPrice && matchesDuration && matchesDownPayment
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), MobileData.preloadedMobiles)

    // Extracted Unique Brands
    val availableBrands: List<String> = MobileData.preloadedMobiles.map { it.brand }.distinct()

    // Room Persistent Inquiries
    val inquiries: Flow<List<InstallmentInquiry>> = dao.getAllInquiries()

    // Calculator Inputs
    val calculatorPrice = MutableStateFlow("45000")
    val calculatorBrand = MutableStateFlow("Samsung")
    val calculatorName = MutableStateFlow("Galaxy A15")
    val calculatorCategory = MutableStateFlow(PhoneCategory.TOUCH)
    val calculatorDuration = MutableStateFlow(12) // Months
    val calculatorDownPaymentPercent = MutableStateFlow(15) // Percentage

    // Active device being viewed/customized in the Bottom Sheet
    private val _activeDevice = MutableStateFlow<MobileDevice?>(null)
    val activeDevice: StateFlow<MobileDevice?> = _activeDevice.asStateFlow()

    // Specific detail customization for sheet
    val customSheetDuration = MutableStateFlow(12)
    val customSheetDownPaymentPercent = MutableStateFlow(15)

    fun selectCategory(category: PhoneCategory?) {
        _selectedCategory.value = category
    }

    fun selectBrand(brand: String?) {
        _selectedBrand.value = if (_selectedBrand.value == brand) null else brand
    }

    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun selectPriceRange(filter: PriceRangeFilter) {
        _selectedPriceRange.value = filter
    }

    fun selectDurationFilter(filter: DurationFilter) {
        _selectedDurationFilter.value = filter
    }

    fun selectDownPaymentFilter(filter: DownPaymentFilter) {
        _selectedDownPaymentFilter.value = filter
    }

    fun resetFilters() {
        _selectedBrand.value = null
        _selectedCategory.value = null
        _selectedPriceRange.value = PriceRangeFilter.ALL
        _selectedDurationFilter.value = DurationFilter.ALL
        _selectedDownPaymentFilter.value = DownPaymentFilter.ALL
        _searchQuery.value = ""
    }

    fun setActiveDevice(device: MobileDevice?) {
        _activeDevice.value = device
        device?.let {
            customSheetDuration.value = device.installmentDurationMonths
            customSheetDownPaymentPercent.value = device.downPaymentPercentage
        }
    }

    // Calculation Formula to get dynamic down payment & monthly installments
    fun calculateMonthlyInstallment(
        price: Double,
        downPayPercent: Int,
        months: Int,
        category: PhoneCategory
    ): InstallmentBreakdown {
        val downPayAmount = price * (downPayPercent / 100.0)
        val remainingAmt = price - downPayAmount
        
        // Monthly rate set to 1.5% (representing 18% simple annual interest rate)
        val monthlyMarkupRate = 0.015
        
        val totalMarkup = remainingAmt * (monthlyMarkupRate * months)
        val repayableAmount = remainingAmt + totalMarkup
        val monthlyAmount = if (months > 0) repayableAmount / months else 0.0
        
        return InstallmentBreakdown(
            totalPrice = price,
            downPaymentAmount = downPayAmount,
            monthlyAmount = monthlyAmount,
            months = months,
            totalMarkup = totalMarkup,
            totalRepayable = repayableAmount + downPayAmount
        )
    }

    // Submit inquiry to DB
    fun createInquiry(
        deviceTitle: String,
        devicePrice: Double,
        customerName: String,
        whatsappNumber: String,
        city: String,
        duration: Int,
        downPayPercent: Int,
        category: PhoneCategory,
        remarks: String = ""
    ) {
        viewModelScope.launch {
            val breakdown = calculateMonthlyInstallment(devicePrice, downPayPercent, duration, category)
            val inquiry = InstallmentInquiry(
                deviceTitle = deviceTitle,
                devicePrice = devicePrice,
                customerName = customerName,
                whatsappNumber = whatsappNumber,
                city = city,
                durationMonths = duration,
                downPaymentAmount = breakdown.downPaymentAmount,
                monthlyInstallmentAmount = breakdown.monthlyAmount,
                status = "Submitted", // Submitted, Under Review, Approved, Contact Required
                submittedAt = System.currentTimeMillis(),
                remarks = remarks
            )
            dao.insertInquiry(inquiry)
        }
    }

    // Delete inquiry
    fun deleteInquiry(inquiry: InstallmentInquiry) {
        viewModelScope.launch {
            dao.deleteInquiry(inquiry)
        }
    }

    fun triggerWhatsApp(context: Context, inquiry: InstallmentInquiry) {
        val message = """
        🇵🇰 *NEW MOBILE INSTALLMENT INQUIRY* #${inquiry.id}
        
        👤 *Customer Details:*
        *Name:* ${inquiry.customerName}
        *City:* ${inquiry.city}
        *Phone/WhatsApp:* ${inquiry.whatsappNumber}
        
        📱 *Device Selected:*
        *Model:* ${inquiry.deviceTitle}
        *Price:* Rs. ${inquiry.devicePrice.toInt()}
        
        📊 *Installment Plan:*
        *Months:* ${inquiry.durationMonths} Months
        *Down Payment:* Rs. ${inquiry.downPaymentAmount.toInt()}
        *Monthly Installment:* Rs. ${inquiry.monthlyInstallmentAmount.toInt()}/month
        
        ✍️ *Customer Remarks:*
        ${inquiry.remarks.ifEmpty { "I am interested in buying this on installments. Please guide me about the approval process." }}
        
        _Mobile Installments Pakistan © 2026_
        """.trimIndent()

        openWhatsApp(context, message)
    }

    fun openGeneralWhatsApp(context: Context) {
        val generalMsg = "Assalam-o-Alaikum, I am visiting your Mobile Installments app and want to query about buying a phone on easy installments. Please guide me."
        openWhatsApp(context, generalMsg)
    }

    private fun openWhatsApp(context: Context, text: String) {
        try {
            val intent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("https://api.whatsapp.com/send?phone=923482640090&text=${Uri.encode(text)}")
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(intent)
        } catch (e: Exception) {
            // Fallback for browsers or standard text sharing
            val textIntent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                putExtra(Intent.EXTRA_TEXT, text)
                flags = Intent.FLAG_ACTIVITY_NEW_TASK
            }
            context.startActivity(textIntent)
        }
    }
}

data class InstallmentBreakdown(
    val totalPrice: Double,
    val downPaymentAmount: Double,
    val monthlyAmount: Double,
    val months: Int,
    val totalMarkup: Double,
    val totalRepayable: Double
)

enum class PriceRangeFilter(val displayName: String) {
    ALL("All Prices"),
    UNDER_15K("Under Rs. 15,000"),
    FROM_15K_TO_50K("Rs. 15,000 - 50,000"),
    FROM_50K_TO_150K("Rs. 50,000 - 150,000"),
    OVER_150K("Rs. 150,000+")
}

enum class DurationFilter(val displayName: String) {
    ALL("All Durations"),
    SHORT("5-6 Months"),
    LONG("10-12 Months")
}

enum class DownPaymentFilter(val displayName: String) {
    ALL("Any Down Payment"),
    ZERO_PERCENT("0% Down Payment Only")
}
