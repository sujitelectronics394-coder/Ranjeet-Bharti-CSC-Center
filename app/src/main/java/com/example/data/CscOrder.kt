package com.example.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "csc_orders")
data class CscOrder(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val serviceType: String, // "PAN Card", "Passport", "Voter ID", "Aadhaar Services", "Other Work"
    val customerName: String,
    val customerPhone: String,
    val customerEmail: String,
    val customerAddress: String = "",
    val description: String = "",
    val status: String = "Pending", // "Pending", "Under Review", "Uploaded to Portal", "Action Required", "Completed"
    val statusNotes: String = "", // Messages from Ranjeet, e.g., "Documents verified. Applied on CSC portal."
    val referenceNumber: String = "", // Portals application Reference ID/Number
    val timestamp: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val documentPathsJson: String = "[]", // List of locally stored document file paths as a JSON string
    val receiptPath: String? = null // Path to local receipt if uploaded
)
