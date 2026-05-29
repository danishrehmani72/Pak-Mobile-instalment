package com.example.data

import androidx.compose.ui.graphics.vector.ImageVector

enum class PhoneCategory {
    KEYPAD, TOUCH, IPHONE
}

data class MobileDevice(
    val id: String,
    val name: String,
    val brand: String,
    val category: PhoneCategory,
    val price: Double,
    val installmentDurationMonths: Int,
    val downPaymentRequired: Boolean,
    val downPaymentPercentage: Int, // e.g. 0% for keypad, 20% for touch, 30% for iphone
    val features: List<String>,
    val specs: Map<String, String>,
    val imageUrlPlaceholder: String // For descriptive icons and graphics representation
)

object MobileData {
    val preloadedMobiles = listOf(
        // Keypad Phones
        MobileDevice(
            id = "nokia_105",
            name = "Nokia 105",
            brand = "Nokia",
            category = PhoneCategory.KEYPAD,
            price = 4500.0,
            installmentDurationMonths = 5,
            downPaymentRequired = false,
            downPaymentPercentage = 0,
            features = listOf("No Down Payment", "5 Months Installments", "Super Long Battery Backup", "Dual SIM Support"),
            specs = mapOf(
                "Battery" to "1000 mAh",
                "Display" to "1.77 inches QQVGA",
                "SIMs" to "Dual SIM (Mini-SIM)",
                "Radio" to "Wireless FM Radio",
                "Flashlight" to "Built-in LED Torch"
            ),
            imageUrlPlaceholder = "keypad"
        ),
        MobileDevice(
            id = "nokia_110",
            name = "Nokia 110",
            brand = "Nokia",
            category = PhoneCategory.KEYPAD,
            price = 6000.0,
            installmentDurationMonths = 6,
            downPaymentRequired = false,
            downPaymentPercentage = 0,
            features = listOf("Easy 6-Month Plan", "Built-in Camera", "Robust Polycarbonate Body", "No Down Payment"),
            specs = mapOf(
                "Battery" to "1150 mAh",
                "Display" to "1.8 inches QVGA",
                "Camera" to "QVGA Camera",
                "SIMs" to "Dual SIM",
                "Memory Card" to "MicroSD up to 32GB"
            ),
            imageUrlPlaceholder = "keypad"
        ),
        MobileDevice(
            id = "itel_it2173",
            name = "itel it2173",
            brand = "itel",
            category = PhoneCategory.KEYPAD,
            price = 3800.0,
            installmentDurationMonths = 5,
            downPaymentRequired = false,
            downPaymentPercentage = 0,
            features = listOf("Extremely Affordable", "No Down Payment Required", "King Voice Reader Feature"),
            specs = mapOf(
                "Battery" to "1000 mAh",
                "Display" to "1.77 inches",
                "SIMs" to "Dual SIM Support",
                "Body" to "Compact Grip Textured"
            ),
            imageUrlPlaceholder = "keypad"
        ),
        
        // Touch Phones (Android)
        MobileDevice(
            id = "itel_a60",
            name = "itel A60",
            brand = "itel",
            category = PhoneCategory.TOUCH,
            price = 39999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 10,
            features = listOf("12 Months Installments", "Affordable Monthly Plan", "PTA Approved Mobile", "Original Box Packed"),
            specs = mapOf(
                "Display" to "6.6 inches IPS Waterdrop Screen",
                "Battery" to "5000 mAh High Capacity",
                "Storage/RAM" to "32GB ROM + 2GB RAM",
                "Security" to "Face Unlock & Fingerprint Sensor",
                "Camera" to "8MP Rear Dual Camera"
            ),
            imageUrlPlaceholder = "touch"
        ),
        MobileDevice(
            id = "samsung_a15",
            name = "Samsung Galaxy A15",
            brand = "Samsung",
            category = PhoneCategory.TOUCH,
            price = 49999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 15,
            features = listOf("Easy Monthly Installments", "Original Box Packed", "PTA Approved", "Official Samsung Warranty"),
            specs = mapOf(
                "Display" to "6.5 inches Super AMOLED 90Hz",
                "Battery" to "5000 mAh 25W Charging Support",
                "Storage/RAM" to "128GB ROM + 6GB RAM",
                "Processor" to "MediaTek Helio G99 (6nm)",
                "Camera" to "50MP Triple Camera Setup"
            ),
            imageUrlPlaceholder = "touch"
        ),
        MobileDevice(
            id = "infinix_hot_40",
            name = "Infinix Hot 40",
            brand = "Infinix",
            category = PhoneCategory.TOUCH,
            price = 37999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 15,
            features = listOf("Fast Charging Support", "PTA Approved Box Packed", "Dynamic Magic Ring Notch", "12 Months Easy Plan"),
            specs = mapOf(
                "Display" to "6.78 inches 90Hz Refresh",
                "Battery" to "5000 mAh 33W Fast Charging",
                "Storage/RAM" to "256GB ROM + 8GB RAM",
                "Processor" to "MediaTek Helio G88",
                "Camera" to "50MP Dual Rear Camera"
            ),
            imageUrlPlaceholder = "touch"
        ),
        MobileDevice(
            id = "vivo_y17s",
            name = "Vivo Y17s",
            brand = "Vivo",
            category = PhoneCategory.TOUCH,
            price = 36999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 15,
            features = listOf("Glow Style Design", "IP54 Dust & Water Resistance", "12 Months Installments"),
            specs = mapOf(
                "Display" to "6.56 inches High Brightness Display",
                "Battery" to "5000 mAh 15W Charging",
                "Storage/RAM" to "128GB ROM + 4GB RAM + 4GB Extended",
                "Camera" to "50MP Clear Camera"
            ),
            imageUrlPlaceholder = "touch"
        ),
        MobileDevice(
            id = "oppo_a18",
            name = "Oppo A18",
            brand = "Oppo",
            category = PhoneCategory.TOUCH,
            price = 34999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 15,
            features = listOf("Oppo Glow Matte Design", "Smooth 90Hz Display", "Reliable Performance"),
            specs = mapOf(
                "Display" to "6.56 inches Sunlight Display",
                "Battery" to "5000 mAh Screen Energy Saver",
                "Storage/RAM" to "128GB ROM + 4GB RAM",
                "Camera" to "8MP Dual Camera"
            ),
            imageUrlPlaceholder = "touch"
        ),
        MobileDevice(
            id = "tecno_spark_20",
            name = "Tecno Spark 20",
            brand = "Tecno",
            category = PhoneCategory.TOUCH,
            price = 34999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 15,
            features = listOf("Stereo Dual Speakers", "Elegant Futuristic Aesthetics", "12 Months Plan"),
            specs = mapOf(
                "Display" to "6.6 inches 90Hz Punch-Hole Screen",
                "Battery" to "5000 mAh 18W Fast Charge",
                "Storage/RAM" to "256GB ROM + 8GB RAM",
                "Camera" to "50MP Ultra Clear Main Camera + 32MP Glowing Selfie"
            ),
            imageUrlPlaceholder = "touch"
        ),

        // Apple iPhones
        MobileDevice(
            id = "iphone_11",
            name = "Apple iPhone 11",
            brand = "Apple",
            category = PhoneCategory.IPHONE,
            price = 139999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 30,
            features = listOf("Easy Down Payment", "PTA Approved Model", "100% Original Liquid Retina Screen", "Waterproof Glass Body"),
            specs = mapOf(
                "Display" to "6.1 inches Liquid Retina HD IPS",
                "Battery" to "3110 mAh Super Fast Charging",
                "Security" to "Face ID Secure Authentication",
                "Camera" to "Dual 12MP Ultra-wide & Wide Cameras"
            ),
            imageUrlPlaceholder = "iphone"
        ),
        MobileDevice(
            id = "iphone_12",
            name = "Apple iPhone 12",
            brand = "Apple",
            category = PhoneCategory.IPHONE,
            price = 174999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 30,
            features = listOf("Premium Metal Architecture", "OLED Super Retina XDR", "PTA Approved Original Device"),
            specs = mapOf(
                "Display" to "6.1 inches Super Retina XDR OLED",
                "Battery" to "2815 mAh MagSafe Wireless",
                "Processor" to "A14 Bionic (5nm Rocket Chip)",
                "Camera" to "Dual 12MP System with Dolby Vision HDR"
            ),
            imageUrlPlaceholder = "iphone"
        ),
        MobileDevice(
            id = "iphone_13",
            name = "Apple iPhone 13",
            brand = "Apple",
            category = PhoneCategory.IPHONE,
            price = 224999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 35,
            features = listOf("PTA Approved Model", "Cinematic Videography Mode", "Original Devices on Installments"),
            specs = mapOf(
                "Display" to "6.1 inches Super Retina OLED (Brighter)",
                "Battery" to "3240 mAh (All-Day Battery)",
                "Processor" to "A15 Bionic (Supreme Speed)",
                "Camera" to "Advanced Dual Camera with Sensor-Shift OIS"
            ),
            imageUrlPlaceholder = "iphone"
        ),
        MobileDevice(
            id = "iphone_13_promax",
            name = "iPhone 13 Pro Max",
            brand = "Apple",
            category = PhoneCategory.IPHONE,
            price = 344999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 40,
            features = listOf("Down Payment Required", "PTA Approved", "120Hz ProMotion Cinematic Screen", "Original Unlocked Pristine Condition"),
            specs = mapOf(
                "Display" to "6.7 inches ProMotion Super Retina OLED 120Hz",
                "Battery" to "4352 mAh Ultimate Lifespan",
                "Processor" to "A15 Bionic GPU 5-Core Edition",
                "Camera" to "Triple 12MP Telephoto, Wide, and Ultra-Wide Cameras with LiDAR"
            ),
            imageUrlPlaceholder = "iphone"
        ),
        MobileDevice(
            id = "iphone_14",
            name = "Apple iPhone 14",
            brand = "Apple",
            category = PhoneCategory.IPHONE,
            price = 244999.0,
            installmentDurationMonths = 12,
            downPaymentRequired = true,
            downPaymentPercentage = 35,
            features = listOf("Crash Detection Safety Feature", "A15 Bionic Action Quad CPU", "Easy Easy Down Payments"),
            specs = mapOf(
                "Display" to "6.1 inches Ceramic Shield Display",
                "Battery" to "3279 mAh Optimized Battery Care",
                "Processor" to "A15 Bionic (Enhanced GPU)",
                "Camera" to "Dual 12MP Back Camera with Photonic Engine"
            ),
            imageUrlPlaceholder = "iphone"
        )
    )
}
