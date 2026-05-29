package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface InquiryDao {
    @Query("SELECT * FROM installment_inquiry ORDER BY submittedAt DESC")
    fun getAllInquiries(): Flow<List<InstallmentInquiry>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertInquiry(inquiry: InstallmentInquiry): Long

    @Delete
    suspend fun deleteInquiry(inquiry: InstallmentInquiry)

    @Query("UPDATE installment_inquiry SET status = :status WHERE id = :id")
    suspend fun updateInquiryStatus(id: Int, status: String)
}
