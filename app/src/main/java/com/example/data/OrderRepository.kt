package com.example.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import java.io.File
import java.io.FileOutputStream

class OrderRepository(private val cscOrderDao: CscOrderDao) {
    val allOrders: Flow<List<CscOrder>> = cscOrderDao.getAllOrders()

    fun getOrdersByCustomerPhone(phone: String): Flow<List<CscOrder>> =
        cscOrderDao.getOrdersByCustomerPhone(phone)

    fun getOrderById(id: Int): Flow<CscOrder?> =
        cscOrderDao.getOrderById(id)

    suspend fun insertOrder(order: CscOrder): Long = withContext(Dispatchers.IO) {
        cscOrderDao.insertOrder(order)
    }

    suspend fun updateOrder(order: CscOrder) = withContext(Dispatchers.IO) {
        cscOrderDao.updateOrder(order)
    }

    suspend fun deleteOrder(order: CscOrder) = withContext(Dispatchers.IO) {
        cscOrderDao.deleteOrder(order)
    }

    // Secure helper to copy picked media URIs to application internal sandbox
    // avoiding URI permission revocation issues on next app startup
    suspend fun saveUriToInternalStorage(context: Context, uri: Uri, prefix: String): String =
        withContext(Dispatchers.IO) {
            try {
                val inputStream = context.contentResolver.openInputStream(uri) ?: return@withContext ""
                val extension = context.contentResolver.getType(uri)?.split("/")?.lastOrNull() ?: "bin"
                val fileName = "csc_${prefix}_${System.currentTimeMillis()}.$extension"
                val file = File(context.filesDir, fileName)
                
                FileOutputStream(file).use { outputStream ->
                    inputStream.copyTo(outputStream)
                }
                file.absolutePath
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }
}
