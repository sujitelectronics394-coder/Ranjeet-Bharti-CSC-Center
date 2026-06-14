package com.example.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CscOrderDao {
    @Query("SELECT * FROM csc_orders ORDER BY updatedAt DESC")
    fun getAllOrders(): Flow<List<CscOrder>>

    @Query("SELECT * FROM csc_orders WHERE customerPhone = :phone ORDER BY updatedAt DESC")
    fun getOrdersByCustomerPhone(phone: String): Flow<List<CscOrder>>

    @Query("SELECT * FROM csc_orders WHERE id = :id")
    fun getOrderById(id: Int): Flow<CscOrder?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrder(order: CscOrder): Long

    @Update
    suspend fun updateOrder(order: CscOrder)

    @Delete
    suspend fun deleteOrder(order: CscOrder)
}
