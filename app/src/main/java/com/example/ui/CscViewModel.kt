package com.example.ui

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.R
import com.example.data.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import org.json.JSONArray
import java.io.File

class CscViewModel(application: Application) : AndroidViewModel(application) {

    private val database = CscDatabase.getDatabase(application)
    private val repository = OrderRepository(database.cscOrderDao())

    // App Navigation & Session states
    private val _currentRole = MutableStateFlow("Customer") // "Customer" or "Operator"
    val currentRole: StateFlow<String> = _currentRole.asStateFlow()

    private val _customerPhone = MutableStateFlow("")
    val customerPhone: StateFlow<String> = _customerPhone.asStateFlow()

    // Transient files picked for a new application upload
    private val _tempAttachedFiles = MutableStateFlow<List<String>>(emptyList())
    val tempAttachedFiles: StateFlow<List<String>> = _tempAttachedFiles.asStateFlow()

    // Global order flows
    val allOrders: StateFlow<List<CscOrder>> = repository.allOrders
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _statusFilter = MutableStateFlow("All")
    val statusFilter: StateFlow<String> = _statusFilter.asStateFlow()

    // Filtered orders for the Operator dashboard
    val filteredOrders: StateFlow<List<CscOrder>> = combine(allOrders, searchQuery, statusFilter) { list, query, filter ->
        list.filter { order ->
            val matchesQuery = order.customerName.contains(query, ignoreCase = true) || 
                               order.customerPhone.contains(query) || 
                               order.referenceNumber.contains(query, ignoreCase = true) ||
                               order.serviceType.contains(query, ignoreCase = true)
            
            val matchesFilter = if (filter == "All") true else order.status.equals(filter, ignoreCase = true)
            matchesQuery && matchesFilter
        }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Customer orders filtered dynamically based on current phone entered
    val customerOrders: StateFlow<List<CscOrder>> = combine(allOrders, _customerPhone) { list, phone ->
        if (phone.isBlank()) emptyList()
        else list.filter { it.customerPhone.trim() == phone.trim() }
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Gemini auto-generation draft state
    private val _notificationDraft = MutableStateFlow("")
    val notificationDraft: StateFlow<String> = _notificationDraft.asStateFlow()

    private val _isDrafting = MutableStateFlow(false)
    val isDrafting: StateFlow<String> = _isDrafting.map { if (it) "Generating professional draft..." else "" }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(), "")

    init {
        createNotificationChannel()
    }

    fun setRole(role: String) {
        _currentRole.value = role
    }

    fun setCustomerPhone(phone: String) {
        _customerPhone.value = phone
    }

    fun setSearchQuery(query: String) {
        _searchQuery.value = query
    }

    fun setStatusFilter(filter: String) {
        _statusFilter.value = filter
    }

    // Pick document, save, and add path to temp list
    fun pickAndSaveDocument(context: Context, uri: Uri) {
        viewModelScope.launch {
            val savedPath = repository.saveUriToInternalStorage(context, uri, "doc")
            if (savedPath.isNotEmpty()) {
                val current = _tempAttachedFiles.value.toMutableList()
                current.add(savedPath)
                _tempAttachedFiles.value = current
            }
        }
    }

    fun removeTempDocument(path: String) {
        val current = _tempAttachedFiles.value.toMutableList()
        if (current.remove(path)) {
            _tempAttachedFiles.value = current
            // Clean up file physically
            try {
                File(path).delete()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun clearTempDocuments() {
        _tempAttachedFiles.value = emptyList()
    }

    // Submit new CSC service request
    fun submitNewOrder(
        serviceType: String,
        name: String,
        phone: String,
        email: String,
        address: String,
        notes: String,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val jsonArray = JSONArray()
            _tempAttachedFiles.value.forEach { jsonArray.put(it) }

            val order = CscOrder(
                serviceType = serviceType,
                customerName = name,
                customerPhone = phone.trim(),
                customerEmail = email,
                customerAddress = address,
                description = notes,
                status = "Pending",
                documentPathsJson = jsonArray.toString(),
                timestamp = System.currentTimeMillis(),
                updatedAt = System.currentTimeMillis()
            )

            repository.insertOrder(order)
            _tempAttachedFiles.value = emptyList()
            
            // Generate notification alert for client
            sendLocalNotification(
                "Order Booked Successfully",
                "Your $serviceType request has been placed with Ranjeet CSC Cafe."
            )
            onSuccess()
        }
    }

    // Update order status/notes/reference (called by Ranjeet Bharti)
    fun updateOrderStatus(
        order: CscOrder,
        newStatus: String,
        remarks: String,
        newRef: String,
        receiptPath: String? = null,
        onSuccess: () -> Unit
    ) {
        viewModelScope.launch {
            val updated = order.copy(
                status = newStatus,
                statusNotes = remarks,
                referenceNumber = newRef,
                receiptPath = receiptPath ?: order.receiptPath,
                updatedAt = System.currentTimeMillis()
            )
            repository.updateOrder(updated)
            
            // Post dynamic notification to client device
            sendLocalNotification(
                "CSC Workspace: ${updated.serviceType} Update",
                "Status: $newStatus | remarks: ${remarks.ifEmpty { "Work processing" }}"
            )

            // Automate drafting notification text
            draftNotificationText(updated)
            onSuccess()
        }
    }

    // Save picked receipt
    fun saveReceiptFile(context: Context, uri: Uri, onSaved: (String) -> Unit) {
        viewModelScope.launch {
            val path = repository.saveUriToInternalStorage(context, uri, "receipt")
            if (path.isNotEmpty()) {
                onSaved(path)
            }
        }
    }

    // Call Gemini trigger to formulate professional update message
    fun draftNotificationText(order: CscOrder) {
        viewModelScope.launch {
            _isDrafting.value = true
            val draft = GeminiClient.generateStatusNotification(
                customerName = order.customerName,
                serviceType = order.serviceType,
                status = order.status,
                notes = order.statusNotes,
                refNumber = order.referenceNumber
            )
            _notificationDraft.value = draft
            _isDrafting.value = false
        }
    }

    fun clearDraftText() {
        _notificationDraft.value = ""
    }

    // Initialize notification channels
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val context = getApplication<Application>().applicationContext
            val name = "CSC Portal Notifications"
            val descriptionText = "Updates about your PAN, Passport, and CSC service submissions"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel("csc_alerts", name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    // Post a visible System Notification alert
    private fun sendLocalNotification(title: String, message: String) {
        val context = getApplication<Application>().applicationContext
        val builder = NotificationCompat.Builder(context, "csc_alerts")
            .setSmallIcon(android.R.drawable.stat_notify_chat)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setAutoCancel(true)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(System.currentTimeMillis().toInt(), builder.build())
    }

    // Utility to parse JSON paths array
    fun parseDocumentPaths(json: String): List<String> {
        return try {
            val array = JSONArray(json)
            val list = mutableListOf<String>()
            for (i in 0 until array.length()) {
                list.add(array.getString(i))
            }
            list
        } catch (e: Exception) {
            emptyList()
        }
    }
}
