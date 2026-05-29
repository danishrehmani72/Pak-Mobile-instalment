package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "installment_inquiry")
data class InstallmentInquiry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val deviceTitle: String,
    val devicePrice: Double,
    val customerName: String,
    val whatsappNumber: String,
    val city: String,
    val durationMonths: Int,
    val downPaymentAmount: Double,
    val monthlyInstallmentAmount: Double,
    val status: String, // "Submitted", "Under Review", "Approved", "Need Action"
    val submittedAt: Long,
    val remarks: String
)
