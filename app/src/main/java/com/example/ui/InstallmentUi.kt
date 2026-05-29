package com.example.ui

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.painterResource
import com.example.R
import com.example.InstallmentBreakdown
import com.example.InstallmentViewModel
import com.example.PriceRangeFilter
import com.example.DurationFilter
import com.example.DownPaymentFilter
import com.example.data.*
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun ExploreScreen(
    viewModel: InstallmentViewModel,
    onNavigateToInquiry: (MobileDevice, Int, Int) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val searchQuery by viewModel.searchQuery.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()
    val selectedBrand by viewModel.selectedBrand.collectAsState()
    val selectedPriceRange by viewModel.selectedPriceRange.collectAsState()
    val selectedDurationFilter by viewModel.selectedDurationFilter.collectAsState()
    val selectedDownPaymentFilter by viewModel.selectedDownPaymentFilter.collectAsState()
    val filteredMobiles by viewModel.filteredMobiles.collectAsState()
    val activeDevice by viewModel.activeDevice.collectAsState()

    var showApplyQuickSheet by remember { mutableStateOf(false) }
    var showFilters by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("explore_screen")
    ) {
        // Hero Header Vibe
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF006D5B)),
            shape = RoundedCornerShape(32.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .drawBehind {
                        drawCircle(
                            color = Color.White.copy(alpha = 0.08f),
                            radius = size.minDimension * 0.7f,
                            center = Offset(size.width * 0.95f, size.height * 0.15f)
                        )
                    }
                    .padding(16.dp)
            ) {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Upgrade Your Mobile Today.",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Black,
                                lineHeight = 26.sp
                            )
                        }
                        // High-fidelity brand logomark badge
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .background(Color.White.copy(alpha = 0.2f), shape = CircleShape)
                                .padding(2.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.ic_brand_logo),
                                contentDescription = "Brand Logo Badge",
                                modifier = Modifier.size(36.dp)
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "No heavy upfront costs. PTA Approved devices with easy 12-month plans.",
                        color = Color.White.copy(alpha = 0.9f),
                        fontSize = 11.sp,
                        lineHeight = 15.sp,
                        fontWeight = FontWeight.Medium
                    )
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(50.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("0% DOWNPAYMENT", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                        Box(
                            modifier = Modifier
                                .background(Color.White.copy(alpha = 0.15f), shape = RoundedCornerShape(50.dp))
                                .border(1.dp, Color.White.copy(alpha = 0.3f), shape = RoundedCornerShape(50.dp))
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text("FAST APPROVAL", fontSize = 8.sp, fontWeight = FontWeight.Bold, color = Color.White)
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Search field
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { viewModel.updateSearchQuery(it) },
                        placeholder = { Text("Search Nokia, Samsung, iPhone...", color = Color.White.copy(alpha = 0.6f)) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.White.copy(alpha = 0.8f)) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { viewModel.updateSearchQuery("") }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Clear", tint = Color.White)
                                }
                            }
                        },
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.6f),
                            focusedBorderColor = Color.White.copy(alpha = 0.7f),
                            unfocusedBorderColor = Color.White.copy(alpha = 0.4f),
                            focusedContainerColor = Color.White.copy(alpha = 0.15f),
                            unfocusedContainerColor = Color.White.copy(alpha = 0.15f)
                        ),
                        singleLine = true,
                        shape = RoundedCornerShape(24.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_field")
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        TextButton(
                            onClick = { showFilters = !showFilters },
                            colors = ButtonDefaults.textButtonColors(contentColor = Color.White)
                        ) {
                            Icon(
                                imageVector = if (showFilters) Icons.Default.FilterList else Icons.Default.FilterList,
                                contentDescription = "Toggle Filters",
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (showFilters) "Hide Filters ▲" else "Refine & Filter ▼",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black
                            )
                        }
                        
                        val isAnyFilterModifierActive = selectedPriceRange != PriceRangeFilter.ALL ||
                                selectedDurationFilter != DurationFilter.ALL ||
                                selectedDownPaymentFilter != DownPaymentFilter.ALL ||
                                selectedBrand != null || selectedCategory != null || searchQuery.isNotEmpty()
                        
                        if (isAnyFilterModifierActive) {
                            TextButton(
                                onClick = { viewModel.resetFilters() },
                                colors = ButtonDefaults.textButtonColors(contentColor = Color.White.copy(alpha = 0.9f))
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = "Reset Filters",
                                    modifier = Modifier.size(13.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Reset All", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }

                    AnimatedVisibility(
                        visible = showFilters,
                        enter = fadeIn() + expandVertically(),
                        exit = fadeOut() + shrinkVertically()
                    ) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 4.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.12f)),
                            shape = RoundedCornerShape(20.dp),
                            border = BorderStroke(1.dp, Color.White.copy(alpha = 0.18f))
                        ) {
                            Column(
                                modifier = Modifier.padding(12.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                // Price Range Filters
                                Column {
                                    Text(
                                        text = "PRICE RANGE FILTER",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        PriceRangeFilter.values().forEach { priceFilter ->
                                            val isPriceSelected = selectedPriceRange == priceFilter
                                            Box(
                                                modifier = Modifier
                                                    .background(
                                                        color = if (isPriceSelected) Color.White else Color.White.copy(alpha = 0.1f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isPriceSelected) Color.White else Color.White.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable { viewModel.selectPriceRange(priceFilter) }
                                                    .padding(horizontal = 10.dp, vertical = 6.dp)
                                            ) {
                                                Text(
                                                    text = priceFilter.displayName,
                                                    color = if (isPriceSelected) Color(0xFF006D5B) else Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                // Installment Duration Filters
                                Column {
                                    Text(
                                        text = "PLAN DURATION MONTHS",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        DurationFilter.values().forEach { durationFilter ->
                                            val isDurationSelected = selectedDurationFilter == durationFilter
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .background(
                                                        color = if (isDurationSelected) Color.White else Color.White.copy(alpha = 0.1f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isDurationSelected) Color.White else Color.White.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable { viewModel.selectDurationFilter(durationFilter) }
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = durationFilter.displayName,
                                                    color = if (isDurationSelected) Color(0xFF006D5B) else Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }

                                // Down Payment Filters
                                Column {
                                    Text(
                                        text = "DOWN PAYMENT REQUIREMENT",
                                        color = Color.White.copy(alpha = 0.85f),
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        letterSpacing = 1.sp
                                    )
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        DownPaymentFilter.values().forEach { downPaymentFilter ->
                                            val isDownPaymentSelected = selectedDownPaymentFilter == downPaymentFilter
                                            Box(
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .background(
                                                        color = if (isDownPaymentSelected) Color.White else Color.White.copy(alpha = 0.1f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .border(
                                                        width = 1.dp,
                                                        color = if (isDownPaymentSelected) Color.White else Color.White.copy(alpha = 0.2f),
                                                        shape = RoundedCornerShape(12.dp)
                                                    )
                                                    .clickable { viewModel.selectDownPaymentFilter(downPaymentFilter) }
                                                    .padding(vertical = 6.dp),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = downPaymentFilter.displayName,
                                                    color = if (isDownPaymentSelected) Color(0xFF006D5B) else Color.White,
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        // Horizontal Category Selectors
        Text(
            text = "Featured Categories",
            fontSize = 15.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 16.dp, bottom = 8.dp)
        )
        
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState())
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CategoryChip(
                label = "All Devices",
                selected = selectedCategory == null,
                icon = Icons.Default.PhoneAndroid,
                onClick = { viewModel.selectCategory(null) }
            )
            CategoryChip(
                label = "Keypad Phones",
                selected = selectedCategory == PhoneCategory.KEYPAD,
                icon = Icons.Default.Dialpad,
                onClick = { viewModel.selectCategory(PhoneCategory.KEYPAD) }
            )
            CategoryChip(
                label = "Touch Smartphones",
                selected = selectedCategory == PhoneCategory.TOUCH,
                icon = Icons.Default.SmartScreen,
                onClick = { viewModel.selectCategory(PhoneCategory.TOUCH) }
            )
            CategoryChip(
                label = "iPhones",
                selected = selectedCategory == PhoneCategory.IPHONE,
                icon = Icons.Default.Star,
                onClick = { viewModel.selectCategory(PhoneCategory.IPHONE) }
            )
        }

        // Horizontal Brand Filter
        Text(
            text = "Browse Brands",
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(start = 16.dp, top = 12.dp, bottom = 6.dp)
        )
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                FilterChip(
                    selected = selectedBrand == null,
                    onClick = { viewModel.selectBrand(null) },
                    label = { Text("All Brands") }
                )
            }
            items(viewModel.availableBrands) { brand ->
                FilterChip(
                    selected = selectedBrand == brand,
                    onClick = { viewModel.selectBrand(brand) },
                    label = { Text(brand) }
                )
            }
        }

        // Mobile Cards Grid / List
        if (filteredMobiles.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.size(64.dp),
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No mobiles matching your request.",
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Try searching Nokia, Samsung or check other filters.",
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            viewModel.updateSearchQuery("")
                            viewModel.selectCategory(null)
                            viewModel.selectBrand(null)
                        }
                    ) {
                        Text("Reset All Filters")
                    }
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                contentPadding = PaddingValues(12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier
                    .weight(1f)
                    .testTag("mobiles_grid")
            ) {
                items(filteredMobiles) { phone ->
                    MobileCard(
                        phone = phone,
                        onClick = { viewModel.setActiveDevice(phone) }
                    )
                }
            }
        }
    }

    // Detail dialog/bottom configuration sheet representing original pictures and easy plans
    activeDevice?.let { phone ->
        AlertDialog(
            onDismissRequest = { viewModel.setActiveDevice(null) },
            confirmButton = {},
            dismissButton = {
                TextButton(onClick = { viewModel.setActiveDevice(null) }) {
                    Text("Close Details")
                }
            },
            title = {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = phone.name,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                        Text(
                            text = phone.brand,
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.primary,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(
                                color = when (phone.category) {
                                    PhoneCategory.KEYPAD -> Color(0xFF1ABC9C).copy(alpha = 0.15f)
                                    PhoneCategory.TOUCH -> Color(0xFF3498DB).copy(alpha = 0.15f)
                                    PhoneCategory.IPHONE -> Color(0xFF9B59B6).copy(alpha = 0.15f)
                                },
                                shape = RoundedCornerShape(12.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = when (phone.category) {
                                PhoneCategory.KEYPAD -> "Keypad"
                                PhoneCategory.TOUCH -> "Touch"
                                PhoneCategory.IPHONE -> "iPhone"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = when (phone.category) {
                                PhoneCategory.KEYPAD -> Color(0xFF16A085)
                                PhoneCategory.TOUCH -> Color(0xFF2980B9)
                                PhoneCategory.IPHONE -> Color(0xFF8E44AD)
                            }
                        )
                    }
                }
            },
            text = {
                val months by viewModel.customSheetDuration.collectAsState()
                val downPayPercent by viewModel.customSheetDownPaymentPercent.collectAsState()
                
                val breakdown = viewModel.calculateMonthlyInstallment(
                    phone.price,
                    downPayPercent,
                    months,
                    phone.category
                )

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Custom Simulated Image Box
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(110.dp)
                            .background(
                                Brush.verticalGradient(
                                    listOf(
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.05f),
                                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                                    )
                                ),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = when (phone.category) {
                                    PhoneCategory.KEYPAD -> Icons.Default.Dialpad
                                    PhoneCategory.TOUCH -> Icons.Default.Smartphone
                                    PhoneCategory.IPHONE -> Icons.Default.Star
                                },
                                contentDescription = null,
                                modifier = Modifier.size(44.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "100% Original Packed Device",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Installment Simulator within details window
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.05f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = "Interactive Installment Simulator",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(10.dp))
                            
                            // Months Selector
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Duration: ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("${months} Months", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Slider(
                                value = months.toFloat(),
                                onValueChange = { viewModel.customSheetDuration.value = it.toInt() },
                                valueRange = when(phone.category) {
                                    PhoneCategory.KEYPAD -> 3f..6f
                                    else -> 3f..12f
                                },
                                steps = when(phone.category) {
                                    PhoneCategory.KEYPAD -> 2
                                    else -> 8
                                },
                                modifier = Modifier.height(28.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            // Down payment
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text("Down Payment: ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                                Text("${downPayPercent}% (Rs. ${breakdown.downPaymentAmount.toInt()})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                            }
                            Slider(
                                value = downPayPercent.toFloat(),
                                onValueChange = { viewModel.customSheetDownPaymentPercent.value = it.toInt() },
                                valueRange = when(phone.category) {
                                    PhoneCategory.KEYPAD -> 0f..30f
                                    PhoneCategory.TOUCH -> 10f..40f
                                    PhoneCategory.IPHONE -> 25f..50f
                                },
                                steps = 3,
                                modifier = Modifier.height(28.dp)
                            )

                            Divider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text("Monthly Payment:", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                                    Text(
                                        text = "Rs. ${breakdown.monthlyAmount.toInt()} /mo",
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.ExtraBold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                Button(
                                    onClick = {
                                        onNavigateToInquiry(phone, months, downPayPercent)
                                        viewModel.setActiveDevice(null)
                                    },
                                    shape = RoundedCornerShape(12.dp)
                                ) {
                                    Text("Apply Plan", fontSize = 11.sp)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Bulleted Highlight Specs
                    Text("Guarantees & Offer Points:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    phone.features.forEach { feature ->
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 2.dp)) {
                            Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(feature, fontSize = 12.sp)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Detailed Specifications Table
                    Text("Technical Specifications:", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                    Spacer(modifier = Modifier.height(6.dp))
                    phone.specs.forEach { (label, value) ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 26.dp)
                                .background(Color.Black.copy(alpha = 0.02f))
                                .padding(vertical = 4.dp, horizontal = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = label,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(0.4f),
                                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                            )
                            Text(
                                text = value,
                                fontSize = 11.sp,
                                modifier = Modifier.weight(0.6f)
                            )
                        }
                        Divider(color = Color.Black.copy(alpha = 0.05f))
                    }
                }
            }
        )
    }
}

// Sub-components
@Composable
fun CategoryChip(
    label: String,
    selected: Boolean,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable { onClick() }
            .testTag("category_chip_$label"),
        color = if (selected) Color(0xFF006D5B) else Color.White,
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Color(0xFF006D5B) else Color(0xFFE5E7EB)
        ),
        shadowElevation = if (selected) 2.dp else 1.dp
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(
                        color = if (selected) Color.White.copy(alpha = 0.2f) else Color(0xFFE0F2F1),
                        shape = CircleShape
                      ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    modifier = Modifier.size(15.dp),
                    tint = if (selected) Color.White else Color(0xFF006D5B)
                )
            }
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = if (selected) Color.White else Color(0xFF1F2937),
                letterSpacing = (-0.2).sp
            )
        }
    }
}

@Composable
fun MobileCard(
    phone: MobileDevice,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .testTag("mobile_card_${phone.id}"),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp)
    ) {
        Column {
            // Simulated product image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(115.dp)
                    .background(
                        Brush.radialGradient(
                            listOf(
                                Color(0xFF006D5B).copy(alpha = 0.08f),
                                Color.Transparent
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .background(Color(0xFFE0F2F1), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (phone.category) {
                                PhoneCategory.KEYPAD -> Icons.Default.Dialpad
                                PhoneCategory.TOUCH -> Icons.Default.Smartphone
                                PhoneCategory.IPHONE -> Icons.Default.PhoneIphone
                            },
                            contentDescription = null,
                            modifier = Modifier.size(22.dp),
                            tint = Color(0xFF006D5B)
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = phone.brand.uppercase(),
                        fontWeight = FontWeight.Black,
                        fontSize = 9.sp,
                        color = Color(0xFF006D5B),
                        letterSpacing = 1.2.sp
                    )
                }

                // Ribbon Badge
                Box(
                    modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(8.dp)
                        .background(
                            color = if (phone.downPaymentPercentage == 0) Color(0xFF006D5B) else Color(0xFFEA580C),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (phone.downPaymentPercentage == 0) "0% Down" else "${phone.downPaymentPercentage}% Down",
                        fontSize = 8.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.White
                    )
                }
            }

            // Text Context
            Column(modifier = Modifier.padding(12.dp)) {
                if (phone.price > 40000.0) {
                    Text(
                        text = "BEST SELLER",
                        color = Color(0xFFEA580C),
                        fontWeight = FontWeight.Black,
                        fontSize = 8.sp,
                        letterSpacing = 0.5.sp
                    )
                }
                Text(
                    text = phone.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color(0xFF1F2937)
                )

                // Key Specs badging dynamically fetched from specs map
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    val ramStorage = phone.specs["Storage/RAM"] ?: phone.specs["Memory Card"]
                    if (ramStorage != null) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFF1F5F9), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = ramStorage.substringBefore(" ROM").substringBefore(" Read").take(15),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF475569)
                            )
                        }
                    }
                    val batterySpec = phone.specs["Battery"]
                    if (batterySpec != null) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFE2F0FD), shape = RoundedCornerShape(4.dp))
                                .padding(horizontal = 4.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = batterySpec.substringBefore(" High").substringBefore(" Super").take(10),
                                fontSize = 8.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F52BA)
                            )
                        }
                    }
                }
                
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 2.dp)
                ) {
                    Icon(
                        Icons.Default.Schedule,
                        contentDescription = null,
                        modifier = Modifier.size(11.dp),
                        tint = Color(0xFF006D5B)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "${phone.installmentDurationMonths} Months Plan",
                        fontSize = 11.sp,
                        color = Color(0xFF4B5563),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Estimate Monthly Payments (calculated on standard percentages)
                val sampleBreakdown = if (phone.price <= 6000) phone.price / phone.installmentDurationMonths 
                                      else (phone.price * 0.85) * (1 + 0.012 * phone.installmentDurationMonths) / phone.installmentDurationMonths
                
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Rs. ${sampleBreakdown.toInt()} /mo",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF006D5B)
                        )
                        Text(
                            text = "Price: Rs. ${phone.price.toInt()}",
                            fontSize = 9.sp,
                            color = Color(0xFF9CA3AF)
                        )
                    }
                    
                    Box(
                        modifier = Modifier
                            .size(24.dp)
                            .background(Color(0xFF006D5B), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            Icons.Default.ArrowForward,
                            contentDescription = "View",
                            modifier = Modifier.size(13.dp),
                            tint = Color.White
                        )
                    }
                }
            }
        }
    }
}


@Composable
fun CalculatorScreen(
    viewModel: InstallmentViewModel,
    onNavigateWithPlan: (String, Double, Int, Int, PhoneCategory) -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    val price by viewModel.calculatorPrice.collectAsState()
    val brand by viewModel.calculatorBrand.collectAsState()
    val name by viewModel.calculatorName.collectAsState()
    val category by viewModel.calculatorCategory.collectAsState()
    val duration by viewModel.calculatorDuration.collectAsState()
    val downPaymentPercent by viewModel.calculatorDownPaymentPercent.collectAsState()

    val numericPrice = price.toDoubleOrNull() ?: 0.0
    val breakdown = viewModel.calculateMonthlyInstallment(
        numericPrice,
        downPaymentPercent,
        duration,
        category
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("calculator_screen")
    ) {
        // Headline
        Text(
            text = "Easy Installment Simulator 📊",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Calculate dynamic monthly installments with live markup estimation for any custom mobile model.",
            fontSize = 12.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Form Fields
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("Device Specifications", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                
                // Device Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { viewModel.calculatorName.value = it },
                    label = { Text("Device Model Name") },
                    placeholder = { Text("e.g. Galaxy A15 / iPhone 13") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // Brand
                    OutlinedTextField(
                        value = brand,
                        onValueChange = { viewModel.calculatorBrand.value = it },
                        label = { Text("Brand") },
                        singleLine = true,
                        modifier = Modifier.weight(0.5f)
                    )
                    // Price
                    OutlinedTextField(
                        value = price,
                        onValueChange = { viewModel.calculatorPrice.value = it },
                        label = { Text("Price (PKR)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        singleLine = true,
                        modifier = Modifier.weight(0.5f)
                    )
                }

                // Category selection rows
                Text("Select Category (Influences Markup Standard):", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf(
                        PhoneCategory.KEYPAD to "Keypad",
                        PhoneCategory.TOUCH to "Touch Device",
                        PhoneCategory.IPHONE to "Apple iPhone"
                    ).forEach { (catType, label) ->
                        val isSelected = category == catType
                        OutlinedButton(
                            onClick = { viewModel.calculatorCategory.value = catType },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        ) {
                            Text(label, fontSize = 10.sp, maxLines = 1, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Plan parameter Sliders
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Select Installment Parameters", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.height(10.dp))

                // Months slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Duration Period:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("$duration Months", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = duration.toFloat(),
                    onValueChange = { viewModel.calculatorDuration.value = it.toInt() },
                    valueRange = when (category) {
                        PhoneCategory.KEYPAD -> 3f..6f
                        else -> 3f..12f
                    },
                    steps = when (category) {
                        PhoneCategory.KEYPAD -> 2
                        else -> 8
                    }
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Down payment percent slider
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Down Payment Target:", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    Text("$downPaymentPercent% (Rs. ${breakdown.downPaymentAmount.toInt()})", fontSize = 12.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                }
                Slider(
                    value = downPaymentPercent.toFloat(),
                    onValueChange = { viewModel.calculatorDownPaymentPercent.value = it.toInt() },
                    valueRange = when (category) {
                        PhoneCategory.KEYPAD -> 0f..30f
                        PhoneCategory.TOUCH -> 10f..40f
                        PhoneCategory.IPHONE -> 25f..50f
                    },
                    steps = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Calculated Output Card (Luxury Pakistan Styling with Gradient borders or elements)
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("calculator_result_card")
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text("ESTIMATED MONTHLY INSTALLMENT", color = Color.White.copy(alpha = 0.8f), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        Text(
                            text = if (numericPrice > 0) "Rs. ${breakdown.monthlyAmount.toInt()} /mo" else "Rs. 0 /mo",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = Color.White
                        )
                    }
                    Box(
                        modifier = Modifier
                            .background(Color.White.copy(alpha = 0.2f), shape = RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "${duration} Months",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))
                Divider(color = Color.White.copy(alpha = 0.25f))
                Spacer(modifier = Modifier.height(12.dp))

                // Specific detailed lines
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Selected Mobile Device:", color = Color.White.copy(alpha = 0.82f), fontSize = 11.sp)
                    Text("$brand $name", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Approx. Down Payment:", color = Color.White.copy(alpha = 0.82f), fontSize = 11.sp)
                    Text("Rs. ${breakdown.downPaymentAmount.toInt()}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Processing & Security Fee:", color = Color.White.copy(alpha = 0.82f), fontSize = 11.sp)
                    Text("FREE (Rs. 0)", color = MaterialTheme.colorScheme.tertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(4.dp))
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                    Text("Total Estim. Payable:", color = Color.White.copy(alpha = 0.82f), fontSize = 11.sp)
                    Text("Rs. ${breakdown.totalRepayable.toInt()}", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = {
                        if (numericPrice <= 0) {
                            Toast.makeText(context, "Please enter a valid price first", Toast.LENGTH_SHORT).show()
                        } else {
                            onNavigateWithPlan(
                                "$brand $name",
                                numericPrice,
                                duration,
                                downPaymentPercent,
                                category
                            )
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Submit Application Inquiry Now", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}


@Composable
fun ApplyScreen(
    viewModel: InstallmentViewModel,
    predefinedDeviceName: String,
    predefinedPrice: Double,
    predefinedDuration: Int,
    predefinedDownPercent: Int,
    predefinedCategory: PhoneCategory,
    onSuccess: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    var customerName by remember { mutableStateOf("") }
    var whatsappNumber by remember { mutableStateOf("03") }
    var selectedCity by remember { mutableStateOf("Lahore") }
    var remarks by remember { mutableStateOf("") }
    var incomeRange by remember { mutableStateOf("Rs. 25,000 - 50,000") }
    var employmentType by remember { mutableStateOf("Salaried Person") }
    var guarantorChoice by remember { mutableStateOf("1 Guarantor ID Copy") }
    var selectedPaymentMethod by remember { mutableStateOf("Easypaisa") }
    var screenshotSentConfirmed by remember { mutableStateOf(false) }

    // Forms pre-population
    var deviceNameForm by remember { mutableStateOf(predefinedDeviceName) }
    var devicePriceForm by remember { mutableStateOf(if (predefinedPrice > 0) predefinedPrice.toString() else "45000") }
    var durationForm by remember { mutableStateOf(predefinedDuration) }
    var downPercentForm by remember { mutableStateOf(predefinedDownPercent) }
    var categoryForm by remember { mutableStateOf(predefinedCategory) }

    // Reset when predefined variables expand
    LaunchedEffect(predefinedDeviceName, predefinedPrice, predefinedDuration, predefinedDownPercent, predefinedCategory) {
        if (predefinedDeviceName.isNotEmpty()) {
            deviceNameForm = predefinedDeviceName
            devicePriceForm = predefinedPrice.toString()
            durationForm = predefinedDuration
            downPercentForm = predefinedDownPercent
            categoryForm = predefinedCategory
        }
    }

    val numericPrice = devicePriceForm.toDoubleOrNull() ?: 0.0
    val breakdown = viewModel.calculateMonthlyInstallment(
        numericPrice,
        downPercentForm,
        durationForm,
        categoryForm
    )

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("apply_screen")
    ) {
        Text(
            text = "Easy Installments Application 📑",
            fontWeight = FontWeight.Bold,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = "Submit your installment application. Approved applications are processed on original packed devices with fast shipping.",
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Phone Highlight Container
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text("Selected Purchase:", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                        Text(deviceNameForm, fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    }
                    Box(
                        modifier = Modifier
                            .background(MaterialTheme.colorScheme.primary, shape = RoundedCornerShape(8.dp))
                            .padding(8.dp)
                    ) {
                        Text("Rs. ${breakdown.monthlyAmount.toInt()} /mo", color = Color.White, fontWeight = FontWeight.ExtraBold, fontSize = 12.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text("Duration: ${durationForm} Months", fontSize = 11.sp)
                    Text("Down Payment: Rs. ${breakdown.downPaymentAmount.toInt()} (${downPercentForm}%)", fontSize = 11.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Personal Information Card
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
        ) {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Your Contact & Verification Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                
                // Full Name
                OutlinedTextField(
                    value = customerName,
                    onValueChange = { customerName = it },
                    label = { Text("Full Name (CNIC matching)") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null) },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("apply_name_field")
                )

                // Phone/WhatsApp Number
                OutlinedTextField(
                    value = whatsappNumber,
                    onValueChange = { 
                        // simple digit formatting with 03 starter
                        if (it.length <= 11) whatsappNumber = it 
                    },
                    label = { Text("WhatsApp Mobile Number") },
                    placeholder = { Text("03xx-xxxxxxx") },
                    leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("apply_whatsapp_field")
                )

                // Choose Pakistani City Dropdown simulation (using custom selectors for safety)
                Text("Select Delivery City:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Lahore", "Karachi", "Islamabad", "Rawalpindi", "Faisalabad", "Multan", "Peshawar", "Other").forEach { city ->
                        val isSelected = selectedCity == city
                        OutlinedButton(
                            onClick = { selectedCity = city },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            ),
                            border = BorderStroke(
                                1.dp,
                                if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f)
                            )
                        ) {
                            Text(city, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Employment Type Slider
                Text("Employment Type:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("Salaried Person", "Business Owner", "Self-Employed", "Student/Freelancer").forEach { emp ->
                        val isSelected = employmentType == emp
                        OutlinedButton(
                            onClick = { employmentType = emp },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            ),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        ) {
                            Text(emp, fontSize = 11.sp)
                        }
                    }
                }

                // Security Guarantee Selection
                Text("Security Plan Guarantee:", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Row(
                    modifier = Modifier.fillMaxWidth().horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    listOf("1 Guarantor ID Copy", "Cheque Book Copy", "Utility Bill / Rental Copy").forEach { guar ->
                        val isSelected = guarantorChoice == guar
                        OutlinedButton(
                            onClick = { guarantorChoice = guar },
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = if (isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.1f) else Color.Transparent
                            ),
                            border = BorderStroke(1.dp, if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                        ) {
                            Text(guar, fontSize = 11.sp)
                        }
                    }
                }

                // Optional Message/Notes
                OutlinedTextField(
                    value = remarks,
                    onValueChange = { remarks = it },
                    label = { Text("Special Remarks/Notes (Optional)") },
                    placeholder = { Text("e.g. Please deliver after 5:00 PM / Need specific color") },
                    modifier = Modifier.fillMaxWidth(),
                    maxLines = 3
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Official Downpayment Accounts Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
                .testTag("payment_method_card"),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccountBalanceWallet,
                        contentDescription = "Wallet",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Official Down Payment Accounts 💳",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Text(
                    text = "Transfer down payment Rs. ${breakdown.downPaymentAmount.toInt()} (or custom advance token) to any account below. Send screenshot on WhatsApp, then confirm below to process verification.",
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    lineHeight = 15.sp
                )

                // Easypaisa
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedPaymentMethod == "Easypaisa") MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (selectedPaymentMethod == "Easypaisa") MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedPaymentMethod = "Easypaisa" }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF2ECC71).copy(alpha = 0.15f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("EP", color = Color(0xFF27AE60), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Easypaisa Account", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("0331-6215263", fontWeight = FontWeight.Black, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(
                        onClick = {
                            val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Easypaisa", "03316215263")
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "Easypaisa Number Copied", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                    }
                }

                // Naya Pay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedPaymentMethod == "Naya pay") MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (selectedPaymentMethod == "Naya pay") MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedPaymentMethod = "Naya pay" }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFF3498DB).copy(alpha = 0.15f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("NP", color = Color(0xFF2980B9), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Naya Pay Account", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("0348-2640090", fontWeight = FontWeight.Black, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(
                        onClick = {
                            val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Naya Pay", "03482640090")
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "Naya Pay Number Copied", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                    }
                }

                // SadaPay
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            if (selectedPaymentMethod == "Sadapay") MaterialTheme.colorScheme.primary.copy(alpha = 0.08f) else Color.Transparent,
                            shape = RoundedCornerShape(8.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = if (selectedPaymentMethod == "Sadapay") MaterialTheme.colorScheme.primary else Color.LightGray.copy(alpha = 0.5f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .clickable { selectedPaymentMethod = "Sadapay" }
                        .padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .background(Color(0xFFE74C3C).copy(alpha = 0.15f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("SP", color = Color(0xFFC0392B), fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text("Sadapay Account", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                        Text("0348-2640090", fontWeight = FontWeight.Black, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                    }
                    IconButton(
                        onClick = {
                            val clipboardManager = context.getSystemService(android.content.Context.CLIPBOARD_SERVICE) as android.content.ClipboardManager
                            val clip = android.content.ClipData.newPlainText("Sadapay", "03482640090")
                            clipboardManager.setPrimaryClip(clip)
                            Toast.makeText(context, "Sadapay Number Copied", Toast.LENGTH_SHORT).show()
                        },
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(Icons.Default.ContentCopy, contentDescription = "Copy", modifier = Modifier.size(14.dp))
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Verified Box Checklist section
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { screenshotSentConfirmed = !screenshotSentConfirmed },
                    colors = CardDefaults.cardColors(
                        containerColor = if (screenshotSentConfirmed) MaterialTheme.colorScheme.primary.copy(alpha = 0.06f) else Color(0xFFFFF9E6)
                    ),
                    border = BorderStroke(
                        width = 1.5.dp,
                        color = if (screenshotSentConfirmed) MaterialTheme.colorScheme.primary else Color(0xFFE2B710)
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(10.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Checkbox(
                            checked = screenshotSentConfirmed,
                            onCheckedChange = { screenshotSentConfirmed = it },
                            colors = CheckboxDefaults.colors(
                                checkedColor = MaterialTheme.colorScheme.primary,
                                uncheckedColor = Color(0xFFB38F00)
                            ),
                            modifier = Modifier.size(20.dp).testTag("payment_screenshot_confirmed_checkbox")
                        )
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "VERIFIED DEPOSIT COMPLIANCE",
                                fontSize = 8.5.sp,
                                fontWeight = FontWeight.Black,
                                color = if (screenshotSentConfirmed) MaterialTheme.colorScheme.primary else Color(0xFF806000)
                            )
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = "I verify that I have sent or will send the transfer screenshot of Down Payment to WhatsApp customer confirmation support line.",
                                fontSize = 10.5.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (screenshotSentConfirmed) MaterialTheme.colorScheme.primary else Color(0xFF806000),
                                lineHeight = 14.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Direct Submit Button
        Button(
            onClick = {
                if (customerName.isEmpty()) {
                    Toast.makeText(context, "Please enter your CNIC Full Name", Toast.LENGTH_SHORT).show()
                } else if (whatsappNumber.length < 11 || !whatsappNumber.startsWith("03")) {
                    Toast.makeText(context, "Enter a valid 11-digit WhatsApp number starting with 03xx", Toast.LENGTH_SHORT).show()
                } else if (!screenshotSentConfirmed) {
                    Toast.makeText(context, "⚠️ Please check the verified box and send the screenshot first then confirm", Toast.LENGTH_LONG).show()
                } else {
                    val fullRemarks = "Method: $selectedPaymentMethod. Verified Box: Yes. Guarantor: $guarantorChoice. Employment: $employmentType. Notes: $remarks"
                    viewModel.createInquiry(
                        deviceTitle = deviceNameForm,
                        devicePrice = numericPrice,
                        customerName = customerName,
                        whatsappNumber = whatsappNumber,
                        city = selectedCity,
                        duration = durationForm,
                        downPayPercent = downPercentForm,
                        category = categoryForm,
                        remarks = fullRemarks
                    )
                    Toast.makeText(context, "Application submitted successfully locally!", Toast.LENGTH_LONG).show()
                    onSuccess()
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .testTag("apply_submit_button")
        ) {
            Icon(Icons.Default.CheckCircle, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Confirm Application & Submit Inquiry", fontWeight = FontWeight.Bold, fontSize = 13.sp)
        }
    }
}


@Composable
fun StatusScreen(
    viewModel: InstallmentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val inquiries by viewModel.inquiries.collectAsState(initial = emptyList())

    Column(
        modifier = modifier
            .fillMaxSize()
            .testTag("status_screen")
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(modifier = Modifier.weight(1f)) {
                    Text("My Applications State", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Text("Track your submitted local mobile installment inquiries.", color = Color.White.copy(alpha = 0.9f), fontSize = 11.sp)
                }
                Icon(Icons.Default.Assignment, contentDescription = null, tint = Color.White, modifier = Modifier.size(28.dp))
            }
        }

        if (inquiries.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center) {
                    Icon(Icons.Default.Feed, contentDescription = null, modifier = Modifier.size(72.dp), tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f))
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("No Submitted Applications", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                    Spacer(modifier = Modifier.height(4.dp))
                    Text("Choose an original phone from the Explore tab, customize parameters, and file a verification request to track approvals.", textAlign = TextAlign.Center, fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                }
            }
        } else {
            LazyColumn(
                contentPadding = PaddingValues(12.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp),
                modifier = Modifier.weight(1f)
            ) {
                items(inquiries) { inquiry ->
                    InquiryStatusCard(
                        inquiry = inquiry,
                        onSendWhatsApp = { viewModel.triggerWhatsApp(context, inquiry) },
                        onDelete = { viewModel.deleteInquiry(inquiry) }
                    )
                }
            }
        }
    }
}

@Composable
fun InquiryStatusCard(
    inquiry: InstallmentInquiry,
    onSendWhatsApp: () -> Unit,
    onDelete: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded }
            .testTag("inquiry_card_${inquiry.id}"),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Header Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = inquiry.deviceTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "#PK-${inquiry.id + 1000}",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = MaterialTheme.colorScheme.primary.copy(alpha = 0.6f)
                        )
                    }
                    Text(
                        text = "Submitted: " + SimpleDateFormat("MMM dd, yyyy - hh:mm a", Locale.getDefault()).format(Date(inquiry.submittedAt)),
                        fontSize = 10.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                
                // Status Pill
                Box(
                    modifier = Modifier
                        .background(
                            color = when (inquiry.status) {
                                "Submitted" -> MaterialTheme.colorScheme.primary.copy(alpha = 0.15f)
                                "Under Review" -> Color(0xFFE67E22).copy(alpha = 0.15f)
                                "Approved" -> Color(0xFF2ECC71).copy(alpha = 0.15f)
                                else -> Color.Red.copy(alpha = 0.11f)
                            },
                            shape = RoundedCornerShape(12.dp)
                        )
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = inquiry.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = when (inquiry.status) {
                            "Submitted" -> MaterialTheme.colorScheme.primary
                            "Under Review" -> Color(0xFFD35400)
                            "Approved" -> Color(0xFF27AE60)
                            else -> Color.Red
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Sub details list
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Down Payment", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("Rs. ${inquiry.downPaymentAmount.toInt()}", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column {
                    Text("Installment Period", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("${inquiry.durationMonths} Months", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                }
                Column {
                    Text("Monthly Premium", fontSize = 9.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                    Text("Rs. ${inquiry.monthlyInstallmentAmount.toInt()}/mo", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                }
            }

            // Animated expansion detail
            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column {
                    Spacer(modifier = Modifier.height(12.dp))
                    Divider(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f))
                    Spacer(modifier = Modifier.height(10.dp))

                    Text("Appliction Progress Timeline:", fontWeight = FontWeight.Bold, fontSize = 11.sp)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Progress Stepper Representation
                    TimelineStepper(currentStep = when(inquiry.status) {
                        "Submitted" -> 1
                        "Under Review" -> 2
                        "Approved" -> 3
                        else -> 1
                    })

                    // Deposit Transfer notice card
                    Card(
                        modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.35f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                            Text(
                                text = "Required: Verify Down Payment Transfer 💰",
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Text(
                                text = "To process your installment verification instantly, transfer the Down Payment of Rs. ${inquiry.downPaymentAmount.toInt()} to one of our mobile accounts below and send the screenshot on WhatsApp.",
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onPrimaryContainer,
                                lineHeight = 13.sp
                            )
                            
                            Spacer(modifier = Modifier.height(4.dp))
                            
                            Column(verticalArrangement = Arrangement.spacedBy(2.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🟢 Easypaisa Account:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    Text("0331-6215263", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🔵 Naya Pay Account:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    Text("0348-2640090", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text("🟠 SadaPay Account:", fontSize = 10.sp, fontWeight = FontWeight.SemiBold)
                                    Text("0348-2640090", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Delete Button
                        OutlinedButton(
                            onClick = onDelete,
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                            border = BorderStroke(1.dp, Color.Red.copy(alpha = 0.3f)),
                            modifier = Modifier.weight(0.4f),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Delete, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Cancel", fontSize = 11.sp)
                        }

                        // WhatsApp Submit
                        Button(
                            onClick = onSendWhatsApp,
                            modifier = Modifier.weight(0.6f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Send to WhatsApp Now", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            if (!expanded) {
                Box(modifier = Modifier.fillMaxWidth().padding(top = 6.dp), contentAlignment = Alignment.Center) {
                    Text("Tap to view status details & WhatsApp link", fontSize = 10.sp, color = MaterialTheme.colorScheme.primary.copy(alpha = 0.7f), fontWeight = FontWeight.Medium)
                }
            }
        }
    }
}

@Composable
fun TimelineStepper(currentStep: Int) {
    val steps = listOf("Submitted", "Verification", "Approved Plan", "Dispatched")
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, label ->
            val stepNum = index + 1
            val isActive = currentStep >= stepNum
            val isCurrent = currentStep == stepNum

            Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .background(
                            color = if (isActive) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.1f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isActive && stepNum < currentStep) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(12.dp), tint = Color.White)
                    } else {
                        Text(text = stepNum.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold, color = if (isActive) Color.White else Color.Black.copy(alpha = 0.4f))
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = label,
                    fontSize = 9.sp,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                    color = if (isActive) MaterialTheme.colorScheme.primary else Color.Black.copy(alpha = 0.4f),
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}


@Composable
fun SupportScreen(
    viewModel: InstallmentViewModel,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
            .testTag("support_screen")
    ) {
        // Welcome Banner
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text("Customer Support & Helpline 🇵🇰", color = Color.White, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("🕒 Daily", color = MaterialTheme.colorScheme.tertiary, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "Need help choosing a mobile or completing your verification details? Talk directly with our Pakistan installment verification desk.",
                    color = Color.White.copy(alpha = 0.9f),
                    fontSize = 12.sp
                )
                Spacer(modifier = Modifier.height(14.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    Button(
                        onClick = {
                            val intent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:03482640090"))
                            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                            context.startActivity(intent)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = Color.White, contentColor = MaterialTheme.colorScheme.primary),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Call Us Now", fontSize = 11.sp)
                    }

                    Button(
                        onClick = { viewModel.openGeneralWhatsApp(context) },
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF25D366), contentColor = Color.White),
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("WhatsApp Now", fontSize = 11.sp)
                    }
                }
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Helpline & WhatsApp Support: 0348-2640090",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 11.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Why Choose Us
        Text("⭐ Why Choose Us", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        listOf(
            "100% Original Packed Mobiles" to "We only deliver officially warranty-packed items in original containers.",
            "Trusted Installment Service" to "Over 5 years of serving happy verified clients in Punjab, Sindh & Islamabad.",
            "Lowest Monthly Payments" to "No hidden markups. Transparent customizable plan simulations for every packet.",
            "Fast Verification & App approvals" to "Swift approval process with minimal guarantees (ID copying, bills).",
            "Delivery to All Major Cities" to "Secure shipping with packing guarantees to Lahore, Karachi, Islamabad, Faisalabad, and more."
        ).forEach { (title, description) ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.05f))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), shape = CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(title, fontWeight = FontWeight.Bold, fontSize = 13.sp)
                        Text(description, fontSize = 11.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Delivery Section Info
        Text("🚚 Secure Delivery Areas", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Pakistan-wide secure transit and verification terminals available in:", fontSize = 11.sp, fontWeight = FontWeight.Medium)
                Spacer(modifier = Modifier.height(10.dp))
                
                // Grid of cities with status
                val cities = listOf(
                    "Lahore ( Punjab Hub )" to "Available (1-2 Days)",
                    "Karachi ( Sindh Terminal )" to "Available (2-3 Days)",
                    "Islamabad / Rawalpindi" to "Available (1-2 Days)",
                    "Faisalabad ( Industrial )" to "Available (1-2 Days)",
                    "Other districts" to "Via Secured Courier"
                )

                cities.forEach { (city, time) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.LocationOn, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(city, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Text(time, fontSize = 10.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Footer standard notes
        Text(
            text = "Mobile Installments Pakistan © 2026\nLicensed verification installments carrier. All Devices are 100% PTA Approved.",
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            modifier = Modifier.fillMaxWidth()
        )
    }
}

// Helpers
@Composable
fun FilterChip(
    selected: Boolean,
    onClick: () -> Unit,
    label: @Composable () -> Unit
) {
    Surface(
        modifier = Modifier.clickable { onClick() },
        color = if (selected) Color(0xFFE0F2F1) else Color.White,
        shape = RoundedCornerShape(14.dp),
        border = BorderStroke(
            width = 1.dp,
            color = if (selected) Color(0xFF006D5B) else Color(0xFFE5E7EB)
        ),
        shadowElevation = if (selected) 1.dp else 0.dp
    ) {
        Box(modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)) {
            label()
        }
    }
}
