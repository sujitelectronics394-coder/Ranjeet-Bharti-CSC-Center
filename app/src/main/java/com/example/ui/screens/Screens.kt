@file:OptIn(ExperimentalMaterial3Api::class)
package com.example.ui.screens

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.data.CscOrder
import com.example.ui.CscViewModel
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// --- CONSTANTS ---
const val ROUTE_ROLE_SELECTION = "role_selection"
const val ROUTE_CUSTOMER_LOGIN = "customer_login"
const val ROUTE_CUSTOMER_DASHBOARD = "customer_dashboard"
const val ROUTE_NEW_ORDER = "new_order"
const val ROUTE_ORDER_DETAILS = "order_details/{orderId}"
const val ROUTE_OPERATOR_DASHBOARD = "operator_dashboard"
const val ROUTE_UPDATE_ORDER = "update_order/{orderId}"

// --- HELPER WRAPPER ---
fun formatDate(timestamp: Long): String {
    val sdf = SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault())
    return sdf.format(Date(timestamp))
}

// --- SCREEN 1: ROLE SELECTION ---
@Composable
fun RoleSelectionScreen(navController: NavController, viewModel: CscViewModel) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            // Elegant Visual Icon representing Identity
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.AccountBalance,
                    contentDescription = "CSC Portal Icon",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(48.dp)
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ranjeet CSC Cafe",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                textAlign = TextAlign.Center
            )

            Text(
                text = "Digital Order Book & Tracker",
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            // Option 1: Customer Order portal
            Card(
                onClick = {
                    viewModel.setRole("Customer")
                    navController.navigate(ROUTE_CUSTOMER_LOGIN)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("customer_portal_card")
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFFE8F5E9)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = "Customer Login Icon",
                            tint = Color(0xFF2E7D32),
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Customer Zone",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Track status, upload files and request PAN/Passport",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                    )
                }
            }

            // Option 2: Ranjeet Bharti Operator View
            Card(
                onClick = {
                    viewModel.setRole("Operator")
                    navController.navigate(ROUTE_OPERATOR_DASHBOARD)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("operator_portal_card")
                    .padding(vertical = 8.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
            ) {
                Row(
                    modifier = Modifier
                        .padding(20.dp)
                        .fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(MaterialTheme.colorScheme.primary),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AdminPanelSettings,
                            contentDescription = "Ranjeet Panel Icon",
                            tint = MaterialTheme.colorScheme.onPrimary,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "Ranjeet's Admin Panel",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        Text(
                            text = "Update states, add references & trigger customer notifications",
                            fontSize = 13.sp,
                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.7f)
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "Next",
                        tint = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f)
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Office Information Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.5f)),
                border = BorderStroke(1.dp, Color(0xFFC4C6CF))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "☕ Ranjeet CSC Cyber Cafe",
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "📍 Pakariyar naugawa, Post Dandopur, Kushinagar, UP",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "📞 Mobile: +91 9169004836 | 📧 ranjeetbharti@zohomail.com",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

// --- SCREEN 2: CUSTOMER LOGIN ---
@Composable
fun CustomerLoginScreen(navController: NavController, viewModel: CscViewModel) {
    var phoneInput by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Customer Account Tracking") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Go Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.SendToMobile,
                contentDescription = "Phone icon",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(64.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Verify Your Number",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onBackground
            )

            Text(
                text = "Enter the 10-digit mobile number used when submitting documents to watch progress in real-time.",
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(30.dp))

            OutlinedTextField(
                value = phoneInput,
                onValueChange = {
                    if (it.length <= 10 && it.all { char -> char.isDigit() }) {
                        phoneInput = it
                        errorText = ""
                    }
                },
                label = { Text("Mobile Number") },
                placeholder = { Text("e.g. 9169004836") },
                prefix = { Text("+91 ") },
                leadingIcon = { Icon(imageVector = Icons.Default.Phone, contentDescription = null) },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_phone_input"),
                isError = errorText.isNotEmpty()
            )

            if (errorText.isNotEmpty()) {
                Text(
                    text = errorText,
                    color = MaterialTheme.colorScheme.error,
                    fontSize = 12.sp,
                    modifier = Modifier
                        .align(Alignment.Start)
                        .padding(top = 4.dp, start = 8.dp)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (phoneInput.length != 10) {
                        errorText = "Please enter a valid 10-digit mobile number."
                    } else {
                        viewModel.setCustomerPhone(phoneInput)
                        navController.navigate(ROUTE_CUSTOMER_DASHBOARD)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .testTag("login_submit_button"),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Track My Work", fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    }
}

// --- SCREEN 3: CUSTOMER DASHBOARD ---
@Composable
fun QuickServiceItem(
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    modifier: Modifier = Modifier,
    isPrimary: Boolean = false,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp)
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(if (isPrimary) Color(0xFF0061A4) else Color(0xFFE0E2EC)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = if (isPrimary) Color.White else Color(0xFF191C1E)
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = label,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            lineHeight = 12.sp,
            textAlign = TextAlign.Center,
            color = Color(0xFF191C1E)
        )
    }
}

@Composable
fun NavigationBarTabItem(
    isSelected: Boolean,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    label: String,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(vertical = 4.dp, horizontal = 12.dp)
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(50.dp))
                    .background(Color(0xFFD3E4FF))
                    .padding(horizontal = 20.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = label,
                    tint = Color(0xFF001D36),
                    modifier = Modifier.size(20.dp)
                )
            }
        } else {
            Icon(
                imageVector = icon,
                contentDescription = label,
                tint = Color(0xFF42474E),
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = label,
            fontSize = 10.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
            color = if (isSelected) Color(0xFF001D36) else Color(0xFF42474E)
        )
    }
}

@Composable
fun CustomBottomNavBar(
    selectedTab: Int,
    onTabSelected: (Int) -> Unit
) {
    Surface(
        color = Color(0xFFEEF0F7),
        modifier = Modifier
            .fillMaxWidth()
            .border(width = 1.dp, color = Color(0xFFC4C6CF)),
    ) {
        Row(
            modifier = Modifier
                .navigationBarsPadding()
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavigationBarTabItem(
                isSelected = selectedTab == 0,
                icon = Icons.Default.Home,
                label = "Home",
                onClick = { onTabSelected(0) }
            )
            NavigationBarTabItem(
                isSelected = selectedTab == 1,
                icon = Icons.Default.ReceiptLong,
                label = "Orders",
                onClick = { onTabSelected(1) }
            )
            NavigationBarTabItem(
                isSelected = selectedTab == 2,
                icon = Icons.Default.Notifications,
                label = "Alerts",
                onClick = { onTabSelected(2) }
            )
            NavigationBarTabItem(
                isSelected = selectedTab == 3,
                icon = Icons.Default.Person,
                label = "Profile",
                onClick = { onTabSelected(3) }
            )
        }
    }
}

@Composable
fun SimpleHighDensityStepperRow(
    title: String,
    desc: String,
    isActive: Boolean,
    isLast: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(16.dp)
                    .clip(CircleShape)
                    .background(if (isActive) Color(0xFF0061A4) else Color(0xFFE0E2EC))
                    .border(width = 2.dp, color = Color.White, shape = CircleShape)
            )
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(32.dp)
                        .background(if (isActive) Color(0xFF0061A4) else Color(0xFFC4C6CF))
                )
            }
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column {
            Text(
                text = title,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = if (isActive) Color(0xFF191C1E) else Color(0xFF42474E)
            )
            Text(
                text = desc,
                fontSize = 11.sp,
                color = Color(0xFF42474E)
            )
        }
    }
}

@Composable
fun CustomerDashboardScreen(navController: NavController, viewModel: CscViewModel) {
    val phone by viewModel.customerPhone.collectAsState()
    val orders by viewModel.customerOrders.collectAsState()
    val context = LocalContext.current

    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            Surface(
                color = Color(0xFFD3E4FF),
                shape = RoundedCornerShape(bottomStart = 24.dp, bottomEnd = 24.dp),
                shadowElevation = 1.dp,
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier
                        .statusBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.Top
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = "CSC Digital Sewa",
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF001D36)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                IconButton(
                                    onClick = { navController.navigate(ROUTE_ROLE_SELECTION) },
                                    modifier = Modifier.size(24.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Logout,
                                        contentDescription = "Log out",
                                        tint = Color(0xFF001D36),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = Color(0xFF42474E),
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Pakariyar Naugawa, Kushinagar",
                                    fontSize = 12.sp,
                                    color = Color(0xFF42474E),
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(0xFF0061A4)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "RB",
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color.White.copy(alpha = 0.6f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "9169004836",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0061A4)
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(50.dp))
                                .background(Color(0xFF0061A4))
                                .clickable {
                                    val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9169004836"))
                                    context.startActivity(dialIntent)
                                }
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Call,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(11.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "Online Now",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }
                        }
                    }
                }
            }
        },
        bottomBar = {
            CustomBottomNavBar(selectedTab = selectedTab) { selectedTab = it }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(Color(0xFFF7F9FF))
                .padding(16.dp)
        ) {
            when (selectedTab) {
                0 -> { // HOME
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "QUICK SERVICES",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF191C1E),
                                    letterSpacing = 1.sp
                                )
                                Text(
                                    text = "View All",
                                    fontSize = 11.sp,
                                    color = Color(0xFF0061A4),
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.clickable { navController.navigate(ROUTE_NEW_ORDER) }
                                )
                            }
                            Spacer(modifier = Modifier.height(10.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                QuickServiceItem(
                                    icon = Icons.Default.ContactPage,
                                    label = "PAN\nCard",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate(ROUTE_NEW_ORDER) }
                                )
                                QuickServiceItem(
                                    icon = Icons.Default.Language,
                                    label = "Passport",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate(ROUTE_NEW_ORDER) }
                                )
                                QuickServiceItem(
                                    icon = Icons.Default.HowToReg,
                                    label = "Voter ID",
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate(ROUTE_NEW_ORDER) }
                                )
                                QuickServiceItem(
                                    icon = Icons.Default.Add,
                                    label = "New Order",
                                    isPrimary = true,
                                    modifier = Modifier.weight(1f),
                                    onClick = { navController.navigate(ROUTE_NEW_ORDER) }
                                )
                            }
                        }

                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "ACTIVE ORDERS",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF191C1E),
                                    letterSpacing = 1.sp
                                )
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(CircleShape)
                                        .background(Color(0xFFBA1A1A))
                                )
                            }
                        }

                        val activeOrdersList = orders.filter { it.status != "Completed" }
                        if (activeOrdersList.isEmpty()) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Box(
                                        modifier = Modifier.fillMaxWidth().padding(24.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = "No active orders. Create a new request above!",
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = Color(0xFF42474E)
                                        )
                                    }
                                }
                            }
                        } else {
                            val activeOrder = activeOrdersList.first()
                            item {
                                Card(
                                    onClick = { navController.navigate("order_details/${activeOrder.id}") },
                                    colors = CardDefaults.cardColors(containerColor = Color.White),
                                    border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                                    shape = RoundedCornerShape(24.dp),
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Column(modifier = Modifier.padding(16.dp)) {
                                        Row(
                                            modifier = Modifier.fillMaxWidth(),
                                            horizontalArrangement = Arrangement.SpaceBetween,
                                            verticalAlignment = Alignment.Top
                                        ) {
                                            Column {
                                                Text(
                                                    text = "ORDER #${activeOrder.id}",
                                                    fontSize = 11.sp,
                                                    color = Color(0xFF42474E),
                                                    fontWeight = FontWeight.Bold
                                                )
                                                Text(
                                                    text = activeOrder.serviceType,
                                                    fontSize = 16.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = Color(0xFF191C1E)
                                                )
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .clip(RoundedCornerShape(8.dp))
                                                    .background(Color(0xFFD3E4FF))
                                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                                            ) {
                                                Text(
                                                    text = activeOrder.status.uppercase(),
                                                    color = Color(0xFF001D36),
                                                    fontSize = 10.sp,
                                                    fontWeight = FontWeight.Bold
                                                )
                                            }
                                        }

                                        Spacer(modifier = Modifier.height(12.dp))
                                        HorizontalDivider(color = Color(0xFFC4C6CF))
                                        Spacer(modifier = Modifier.height(12.dp))

                                        val curStep = when (activeOrder.status) {
                                            "Pending" -> 1
                                            "Under Review" -> 2
                                            "Uploaded to Portal" -> 3
                                            "Completed" -> 4
                                            else -> 2
                                        }

                                        Column(
                                            verticalArrangement = Arrangement.spacedBy(12.dp),
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            SimpleHighDensityStepperRow(
                                                title = "Documents Verified",
                                                desc = if (curStep >= 2) "Verified & compiled" else "Awaiting scan review",
                                                isActive = curStep >= 2
                                            )
                                            SimpleHighDensityStepperRow(
                                                title = "Uploaded to CSC Portal",
                                                desc = if (curStep >= 3) "Filed with government portal" else "Awaiting upload queue",
                                                isActive = curStep >= 3,
                                                isLast = true
                                            )
                                        }

                                        Spacer(modifier = Modifier.height(16.dp))

                                        Button(
                                            onClick = { navController.navigate("order_details/${activeOrder.id}") },
                                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0061A4)),
                                            modifier = Modifier.fillMaxWidth(),
                                            shape = RoundedCornerShape(16.dp)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.UploadFile,
                                                contentDescription = null,
                                                tint = Color.White,
                                                modifier = Modifier.size(16.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(
                                                text = "Upload More Documents",
                                                color = Color.White,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> { // ORDERS
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "ORDER HISTORY (${orders.size})",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF191C1E),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (orders.isEmpty()) {
                            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Text("No orders found for this account.", color = Color(0xFF42474E))
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(orders) { order ->
                                    Card(
                                        onClick = { navController.navigate("order_details/${order.id}") },
                                        colors = CardDefaults.cardColors(containerColor = Color.White),
                                        border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Column {
                                                    Text(text = order.serviceType, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFF191C1E))
                                                    Text(text = "ID: #${order.id} | ${formatDate(order.timestamp).substringBefore(",")}", fontSize = 12.sp, color = Color(0xFF42474E))
                                                }
                                                StatusBadge(status = order.status)
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                2 -> { // ALERTS
                    val alerts = orders.filter { it.status == "Action Required" }
                    Column(modifier = Modifier.fillMaxSize()) {
                        Text(
                            text = "ATTENTION REQUIRED",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF191C1E),
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        if (alerts.isEmpty()) {
                            Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = Color(0xFF0061A4), modifier = Modifier.size(48.dp))
                                    Spacer(modifier = Modifier.height(8.dp))
                                    Text("Everything is complete!", fontWeight = FontWeight.Bold, color = Color(0xFF191C1E))
                                    Text("No action or corrections are required at this moment.", fontSize = 13.sp, color = Color(0xFF42474E))
                                }
                            }
                        } else {
                            LazyColumn(
                                modifier = Modifier.weight(1f),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                items(alerts) { order ->
                                    Card(
                                        onClick = { navController.navigate("order_details/${order.id}") },
                                        colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                                        border = BorderStroke(1.dp, Color(0xFFBA1A1A)),
                                        shape = RoundedCornerShape(16.dp),
                                        modifier = Modifier.fillMaxWidth()
                                    ) {
                                        Column(modifier = Modifier.padding(16.dp)) {
                                            Row(
                                                modifier = Modifier.fillMaxWidth(),
                                                horizontalArrangement = Arrangement.SpaceBetween,
                                                verticalAlignment = Alignment.CenterVertically
                                            ) {
                                                Text(text = order.serviceType, fontWeight = FontWeight.Bold, fontSize = 15.sp, color = Color(0xFFBA1A1A))
                                                StatusBadge(status = order.status)
                                            }
                                            Spacer(modifier = Modifier.height(8.dp))
                                            Text(
                                                text = order.statusNotes.ifEmpty { "Additional clarification required. Direct tap to open tracking detail" },
                                                fontSize = 13.sp,
                                                color = Color(0xFF191C1E)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                3 -> { // PROFILE
                    Column(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("CUSTOMER PROFILE", fontWeight = FontWeight.Bold, fontSize = 11.sp, color = Color(0xFF191C1E), letterSpacing = 1.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                DetailRow(label = "Logged-In Tracking Phone", value = "+91 $phone")
                                DetailRow(label = "Subscribed Center Area", value = "Pakariyar Naugawa, Kushinagar")
                            }
                        }

                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color.White),
                            border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text(
                                    text = "☕ Ranjeet Digital Cyber Cafe",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = Color(0xFF0061A4)
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "📍 Address: Pakariyar naugawa, Post Dandopur, Kushinagar, UP",
                                    fontSize = 12.sp,
                                    color = Color(0xFF42474E)
                                )
                                Text(
                                    text = "📞 Mobile: +91 9169004836 | 📧 ranjeetbharti@zohomail.com",
                                    fontSize = 12.sp,
                                    color = Color(0xFF42474E)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// --- SCREEN 4: NEW ORDER BOOKING FORM ---
@Composable
fun NewOrderScreen(navController: NavController, viewModel: CscViewModel) {
    val tempFiles by viewModel.tempAttachedFiles.collectAsState()
    val context = LocalContext.current

    // Form states
    var clientName by remember { mutableStateOf("") }
    var clientPhone by remember { mutableStateOf("") }
    var clientEmail by remember { mutableStateOf("") }
    var clientAddress by remember { mutableStateOf("") }
    var serviceNotes by remember { mutableStateOf("") }

    val servicesList = listOf("PAN Card Application", "Passport Application", "Voter Card (Voter ID)", "Aadhaar Modification", "Other CSC Services")
    var selectedService by remember { mutableStateOf(servicesList[0]) }
    var dropdownExpanded by remember { mutableStateOf(false) }

    // Init customer view's phone auto fill
    val activePhone by viewModel.customerPhone.collectAsState()
    LaunchedEffect(activePhone) {
        if (activePhone.isNotEmpty() && clientPhone.isEmpty()) {
            clientPhone = activePhone
        }
    }

    // Document Picker API interface
    val pickingLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let { viewModel.pickAndSaveDocument(context, it) }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("CSC Order Submission") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close Form")
                    }
                }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Text(
                    text = "Request CSC Services Offline",
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.primary
                )
                Text(
                    text = "Provide customer info and upload clear images/scans of supporting papers (Aadhaar, photos, income certificates). Ranjeet will verify and submit to government portal.",
                    fontSize = 13.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
                Spacer(modifier = Modifier.height(10.dp))
            }

            // Client Personal Inputs
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                ) {
                    Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                        Text("1. Customer Profile Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)

                        // Service Selector Dropdown
                        Box(modifier = Modifier.fillMaxWidth()) {
                            OutlinedTextField(
                                value = selectedService,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Service Requested") },
                                trailingIcon = {
                                    IconButton(onClick = { dropdownExpanded = true }) {
                                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { dropdownExpanded = true }
                            )
                            DropdownMenu(
                                expanded = dropdownExpanded,
                                onDismissRequest = { dropdownExpanded = false },
                                modifier = Modifier.fillMaxWidth(0.9f)
                            ) {
                                servicesList.forEach { service ->
                                    DropdownMenuItem(
                                        text = { Text(service) },
                                        onClick = {
                                            selectedService = service
                                            dropdownExpanded = false
                                        }
                                    )
                                }
                            }
                        }

                        OutlinedTextField(
                            value = clientName,
                            onValueChange = { clientName = it },
                            label = { Text("Customer Full Name *") },
                            singleLine = true,
                            leadingIcon = { Icon(Icons.Default.Badge, null) },
                            modifier = Modifier.fillMaxWidth().testTag("form_name_field")
                        )

                        OutlinedTextField(
                            value = clientPhone,
                            onValueChange = { if (it.all { char -> char.isDigit() }) clientPhone = it },
                            label = { Text("Customer Mobile Number *") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            leadingIcon = { Icon(Icons.Default.Phone, null) },
                            modifier = Modifier.fillMaxWidth().testTag("form_phone_field")
                        )

                        OutlinedTextField(
                            value = clientEmail,
                            onValueChange = { clientEmail = it },
                            label = { Text("Customer Email ID") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            leadingIcon = { Icon(Icons.Default.Email, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = clientAddress,
                            onValueChange = { clientAddress = it },
                            label = { Text("Customer Address Details") },
                            leadingIcon = { Icon(Icons.Default.Home, null) },
                            modifier = Modifier.fillMaxWidth()
                        )

                        OutlinedTextField(
                            value = serviceNotes,
                            onValueChange = { serviceNotes = it },
                            label = { Text("Specific Instructions / Requirements") },
                            minLines = 2,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Paper Upload Panel
            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("2. Upload Supporting Scans / Documents", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Please attach clear copies of ID proofs, address proofs, photo, or signs.",
                            fontSize = 12.sp,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        Button(
                            onClick = { pickingLauncher.launch("*/*") },
                            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("upload_docs_button"),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.UploadFile, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Select Files / Pictures")
                        }

                        // Listing transient cached paths
                        if (tempFiles.isNotEmpty()) {
                            Spacer(modifier = Modifier.height(12.dp))
                            LazyRow(
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                items(tempFiles) { path ->
                                    val file = File(path)
                                    Box(
                                        modifier = Modifier
                                            .size(80.dp)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant)
                                    ) {
                                        // Preview thumbnail
                                        AsyncImage(
                                            model = file,
                                            contentDescription = "Upload Preview",
                                            modifier = Modifier.fillMaxSize()
                                        )

                                        // Delete cross mark symbol
                                        IconButton(
                                            onClick = { viewModel.removeTempDocument(path) },
                                            modifier = Modifier
                                                .size(24.dp)
                                                .align(Alignment.TopEnd)
                                                .background(Color.Red.copy(alpha = 0.8f), CircleShape)
                                        ) {
                                            Icon(
                                                imageVector = Icons.Default.Close,
                                                contentDescription = "Remove",
                                                tint = Color.White,
                                                modifier = Modifier.size(14.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Submissions Actions
            item {
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    onClick = {
                        if (clientName.isBlank() || clientPhone.isBlank()) {
                            Toast.makeText(context, "Full Name and Phone Number are mandatory!", Toast.LENGTH_LONG).show()
                        } else {
                            viewModel.submitNewOrder(
                                serviceType = selectedService,
                                name = clientName,
                                phone = clientPhone,
                                email = clientEmail,
                                address = clientAddress,
                                notes = serviceNotes,
                                onSuccess = {
                                    Toast.makeText(context, "Application filed successfully!", Toast.LENGTH_LONG).show()
                                    navController.navigateUp()
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("submit_application_button"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Register Application Request", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}

// --- SCREEN 5: REAL-TIME TRACKING / ORDER DETAILS SCREEN ---
@Composable
fun OrderDetailsScreen(orderId: Int, navController: NavController, viewModel: CscViewModel) {
    val orders by viewModel.allOrders.collectAsState()
    val order = orders.find { it.id == orderId }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text("Application Progress Tracking") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigateUp() }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (order == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text("Order details not found.")
            }
        } else {
            val docPaths = viewModel.parseDocumentPaths(order.documentPathsJson)

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Header overview Card
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = order.serviceType,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                                StatusBadge(status = order.status)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Registered on: ${formatDate(order.timestamp)}",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSecondaryContainer.copy(alpha = 0.7f)
                            )
                        }
                    }
                }

                // Tracking Stepper Visual Milestones
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text("Application Milestone History", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            Spacer(modifier = Modifier.height(16.dp))

                            // 4 Core Stages in CSC Order processing
                            val currentStepIndex = when (order.status) {
                                "Pending" -> 1
                                "Under Review" -> 2
                                "Uploaded to Portal" -> 3
                                "Completed" -> 4
                                else -> 2 // Action Required is an exception, display alert below
                            }

                            TrackingStepRow(stepNum = 1, title = "Application Placed", desc = "CSC team received details.", isActive = currentStepIndex >= 1)
                            TrackingStepRow(stepNum = 2, title = "Under Verification", desc = "Ranjeet is scanning and preparing files.", isActive = currentStepIndex >= 2)
                            TrackingStepRow(stepNum = 3, title = "Uploaded to CSC Portal", desc = "Application uploaded. Reference Ack ID issued.", isActive = currentStepIndex >= 3, ackNumber = order.referenceNumber)
                            TrackingStepRow(stepNum = 4, title = "Completed & Ready", desc = "Approved! Digital copy, slip or card generated.", isActive = currentStepIndex >= 4, isLast = true)
                        }
                    }
                }

                // If Operator Action Required (Documents blurry, mismatch details)
                if (order.status == "Action Required") {
                    item {
                        Card(
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFBE9E7)),
                            border = BorderStroke(1.dp, Color(0xFFFF5722)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = "Action notice",
                                    tint = Color(0xFFD84315),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Correction Action Required",
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFD84315),
                                        fontSize = 15.sp
                                    )
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = if (order.statusNotes.isNotEmpty()) order.statusNotes else "Ranjeet has indicated database discrepancy. Please tap call or chat below to verify and supply requested information.",
                                        fontSize = 13.sp,
                                        color = Color(0xFF5D4037)
                                    )
                                }
                            }
                        }
                    }
                }

                // Remarks and Details Card
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                    ) {
                        Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            Text("File Overview Details", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                            
                            DetailRow(label = "Applicant Name", value = order.customerName)
                            DetailRow(label = "Associated Phone", value = "+91 " + order.customerPhone)
                            DetailRow(label = "Email Address", value = order.customerEmail.ifEmpty { "Not Provided" })
                            DetailRow(label = "Mailing Address", value = order.customerAddress.ifEmpty { "Not Provided" })
                            DetailRow(label = "Client Instructions", value = order.description.ifEmpty { "None" })

                            if (order.statusNotes.isNotEmpty() && order.status != "Action Required") {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surfaceVariant)
                                        .padding(10.dp)
                                ) {
                                    Column {
                                        Text("Operator Update Memo:", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = MaterialTheme.colorScheme.primary)
                                        Text(order.statusNotes, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                    }
                                }
                            }
                        }
                    }
                }

                // Uploaded papers preview
                if (docPaths.isNotEmpty()) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Text("Your Uploaded Documents Scans", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                                Spacer(modifier = Modifier.height(10.dp))
                                LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                    items(docPaths) { path ->
                                        val file = File(path)
                                        Card(
                                            onClick = {
                                                // Preview in fullscreen or view standard local paths
                                                Toast.makeText(context, "Storage Path: " + file.name, Toast.LENGTH_SHORT).show()
                                            },
                                            shape = RoundedCornerShape(8.dp),
                                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.onSurface.copy(alpha = 0.1f))
                                        ) {
                                            AsyncImage(
                                                model = file,
                                                contentDescription = "Document Scan Preview",
                                                modifier = Modifier.size(100.dp)
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }

                // Digital slip / Receipt from operator (Completed files)
                if (order.receiptPath != null) {
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.4f)),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                        ) {
                            Column(modifier = Modifier.padding(16.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text("🎫 Digital Receipt / Completed ID Doc", fontWeight = FontWeight.Bold, fontSize = 15.sp, color = MaterialTheme.colorScheme.primary)
                                    Icon(Icons.Default.CloudDone, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                }
                                Spacer(modifier = Modifier.height(10.dp))
                                AsyncImage(
                                    model = File(order.receiptPath),
                                    contentDescription = "Receipt Slip Preview",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(180.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            Toast.makeText(context, "Receipt File: ${File(order.receiptPath).name}", Toast.LENGTH_LONG).show()
                                        }
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(
                                    text = "Your digital order has been completed by Ranjeet Bharti. Tap above to enlarge or inspect.",
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            }
                        }
                    }
                }

                // Operator Contact Details at screen base
                item {
                    Text(
                        text = "Need further assist? Contact Ranjeet immediately:",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Button(
                            onClick = {
                                val callIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:9169004836"))
                                context.startActivity(callIntent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Call, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Call Operator")
                        }

                        Button(
                            onClick = {
                                // Direct Whatsapp intent prefilled link using international phone markup
                                val textMsg = "Hello Ranjeet, tracking my order ($orderId) for ${order.serviceType}."
                                val url = "https://api.whatsapp.com/send?phone=919169004836&text=" + Uri.encode(textMsg)
                                val wsIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                context.startActivity(wsIntent)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075E54)),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Icon(Icons.Default.Chat, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("WhatsApp Chat")
                        }
                    }
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// --- SCREEN 6: OPERATOR CONTROL CENTER / REGISTER DASHBOARD ---
@Composable
fun OperatorDashboardScreen(navController: NavController, viewModel: CscViewModel) {
    val search by viewModel.searchQuery.collectAsState()
    val filter by viewModel.statusFilter.collectAsState()
    val orders by viewModel.filteredOrders.collectAsState()
    val fullOrdersList by viewModel.allOrders.collectAsState()
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Ranjeet's CSC Workspace") },
                navigationIcon = {
                    IconButton(onClick = { navController.navigate(ROUTE_ROLE_SELECTION) }) {
                        Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Exit Panel")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        Toast.makeText(context, "Operator: Ranjeet Bharti (+91 9169004836)", Toast.LENGTH_LONG).show()
                    }) {
                        Icon(imageVector = Icons.Default.Info, contentDescription = "Shop info")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(ROUTE_NEW_ORDER) },
                modifier = Modifier.testTag("admin_book_fab")
            ) {
                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = "Add Customer Application")
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp)
        ) {
            // Business Stats overview Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    StatBox(count = fullOrdersList.size, label = "Total Jobs")
                    StatBox(count = fullOrdersList.count { it.status == "Pending" }, label = "New/Pending", highlight = true)
                    StatBox(count = fullOrdersList.count { it.status == "Action Required" }, label = "Issues", error = true)
                    StatBox(count = fullOrdersList.count { it.status == "Completed" }, label = "Closed")
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Search query and filter chips
            OutlinedTextField(
                value = search,
                onValueChange = { viewModel.setSearchQuery(it) },
                label = { Text("Search Applicant (Name / Phone / Ack No)") },
                leadingIcon = { Icon(Icons.Default.Search, null) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("operator_search_input")
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Filter row
            val filterOptions = listOf("All", "Pending", "Under Review", "Uploaded to Portal", "Action Required", "Completed")
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(filterOptions) { opt ->
                    FilterChip(
                        selected = filter == opt,
                        onClick = { viewModel.setStatusFilter(opt) },
                        label = { Text(opt, fontSize = 12.sp) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "Applicant Records (${orders.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (orders.isEmpty()) {
                Box(modifier = Modifier.weight(1f).fillMaxWidth(), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.Task, "No files found", tint = Color.Gray, modifier = Modifier.size(48.dp))
                        Spacer(modifier = Modifier.height(6.dp))
                        Text("No matching registrations found.", color = Color.Gray)
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(orders) { order ->
                        Card(
                            onClick = { navController.navigate("update_order/${order.id}") },
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                            border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("operator_order_item_${order.id}")
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column {
                                        Text(order.customerName, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                        Text(order.serviceType, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                                    }
                                    StatusBadge(status = order.status)
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "📞 +91 ${order.customerPhone}",
                                        fontSize = 13.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                                    )
                                    Text(
                                        text = "Updated: ${formatDate(order.updatedAt).substringBefore(",")}",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                }

                                if (order.referenceNumber.isNotEmpty()) {
                                    Text(
                                        text = "Ack ID: " + order.referenceNumber,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f),
                                        modifier = Modifier.padding(top = 4.dp)
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

// --- SCREEN 7: UPDATE STATUS & SEND AUTOMATED NOTIFICATIONS (RANJEET'S ACTION SCREEN) ---
@Composable
fun UpdateOrderScreen(orderId: Int, navController: NavController, viewModel: CscViewModel) {
    val orders by viewModel.allOrders.collectAsState()
    val order = orders.find { it.id == orderId }
    val context = LocalContext.current

    if (order == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Order details not found.")
        }
    } else {
        var statusSelection by remember { mutableStateOf(order.status) }
        var remarksSelection by remember { mutableStateOf(order.statusNotes) }
        var refNoSelection by remember { mutableStateOf(order.referenceNumber) }
        var finalReceiptPath by remember { mutableStateOf(order.receiptPath) }

        val docPaths = viewModel.parseDocumentPaths(order.documentPathsJson)
        val draftText by viewModel.notificationDraft.collectAsState()
        val isDraftingText by viewModel.isDrafting.collectAsState()

        val receiptPicker = rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) { uri ->
            uri?.let {
                viewModel.saveReceiptFile(context, it) { path ->
                    finalReceiptPath = path
                }
            }
        }

        Scaffold(
            topBar = {
                CenterAlignedTopAppBar(
                    title = { Text("Update Order & Notify Client") },
                    navigationIcon = {
                        IconButton(onClick = { navController.navigateUp() }) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "Back")
                        }
                    }
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Header Details Overview
                item {
                    Card(
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(text = order.customerName, fontWeight = FontWeight.Bold, fontSize = 18.sp)
                            Text(text = "Applied Service: ${order.serviceType}", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Medium)
                            Text(text = "Customer Phone: +91 " + order.customerPhone, fontSize = 13.sp, color = Color.Gray)
                        }
                    }
                }

                // Customer attached files preview
                if (docPaths.isNotEmpty()) {
                    item {
                        Text("Customer Uploaded Scans:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                        Spacer(modifier = Modifier.height(4.dp))
                        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            items(docPaths) { path ->
                                Card(
                                    onClick = { Toast.makeText(context, "Full local path: $path", Toast.LENGTH_SHORT).show() },
                                    shape = RoundedCornerShape(8.dp)
                                ) {
                                    AsyncImage(model = File(path), contentDescription = null, modifier = Modifier.size(80.dp))
                                }
                            }
                        }
                    }
                }

                // Select Status Picker
                item {
                    Text("Select Status Stage:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    val statusStages = listOf("Pending", "Under Review", "Uploaded to Portal", "Action Required", "Completed")
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        statusStages.forEach { stage ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .clickable { statusSelection = stage }
                                    .padding(vertical = 10.dp, horizontal = 12.dp)
                                    .background(if (statusSelection == stage) MaterialTheme.colorScheme.secondaryContainer else Color.Transparent),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(selected = statusSelection == stage, onClick = { statusSelection = stage })
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(text = stage, fontSize = 15.sp, fontWeight = FontWeight.Medium)
                            }
                        }
                    }
                }

                // Reference Input and Remarks
                item {
                    Text("CSC Portal Information:", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp), modifier = Modifier.fillMaxWidth()) {
                        OutlinedTextField(
                            value = refNoSelection,
                            onValueChange = { refNoSelection = it },
                            label = { Text("Government Portal Ack / Reference No") },
                            placeholder = { Text("e.g. PN-938210-UP") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("update_ack_field")
                        )

                        OutlinedTextField(
                            value = remarksSelection,
                            onValueChange = { remarksSelection = it },
                            label = { Text("Status Remarks & Message for Customer") },
                            placeholder = { Text("e.g. Aadhaar details matched, proceeding with passport uploads") },
                            minLines = 2,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("update_remarks_field")
                        )
                    }
                }

                // Digital receipt upload panel (Receipt from Government portals)
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color(0xFFC4C6CF)),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text("Attach Government Receipt / Final ID Card (Optional)", fontWeight = FontWeight.Bold, fontSize = 13.sp)
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { receiptPicker.launch("*/*") },
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary),
                                modifier = Modifier.fillMaxWidth().testTag("upload_receipt_button")
                            ) {
                                Icon(Icons.Default.CloudUpload, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Select Receipt Scan")
                            }

                            if (finalReceiptPath != null) {
                                Spacer(modifier = Modifier.height(10.dp))
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    AsyncImage(model = File(finalReceiptPath!!), contentDescription = null, modifier = Modifier.size(60.dp).clip(RoundedCornerShape(6.dp)))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("Receipt file uploaded successfully", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.weight(1f))
                                    IconButton(onClick = { finalReceiptPath = null }) {
                                        Icon(Icons.Default.Delete, contentDescription = "Delete", tint = Color.Red)
                                    }
                                }
                            }
                        }
                    }
                }

                // Action to update Database
                item {
                    Button(
                        onClick = {
                            viewModel.updateOrderStatus(
                                order = order,
                                newStatus = statusSelection,
                                remarks = remarksSelection,
                                newRef = refNoSelection,
                                receiptPath = finalReceiptPath,
                                onSuccess = {
                                    Toast.makeText(context, "Order successfully modified!", Toast.LENGTH_LONG).show()
                                }
                            )
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("save_order_button")
                    ) {
                        Text("Save Status Modifications", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Gemini drafting widget
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color.White),
                        border = BorderStroke(1.dp, Color(0xFFC4C6CF))
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text("🤖 Smart Notification Generator", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                                if (isDraftingText.isNotEmpty()) {
                                    CircularProgressIndicator(modifier = Modifier.size(18.dp), strokeWidth = 2.dp)
                                }
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                "Using Gemini, we can draft a customized notification in Hindi, English or Hinglish which can be sent to the customer across WhatsApp or SMS.",
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = { viewModel.draftNotificationText(order.copy(status = statusSelection, statusNotes = remarksSelection, referenceNumber = refNoSelection)) },
                                modifier = Modifier.fillMaxWidth().testTag("generate_draft_button"),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.tertiary)
                            ) {
                                Icon(Icons.Default.AutoAwesome, null)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Draft Notification Message")
                            }

                            if (draftText.isNotEmpty()) {
                                Spacer(modifier = Modifier.height(12.dp))
                                Text("Generated Draft Message Preview:", fontWeight = FontWeight.Bold, fontSize = 13.sp, color = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(MaterialTheme.colorScheme.surface)
                                        .border(1.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.2f), RoundedCornerShape(8.dp))
                                        .padding(12.dp)
                                ) {
                                    Text(text = draftText, fontSize = 13.sp, color = MaterialTheme.colorScheme.onSurface)
                                }

                                Spacer(modifier = Modifier.height(10.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                                ) {
                                    // Button to Copy SMS text
                                    OutlinedButton(
                                        onClick = {
                                            val clipboard = context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                                            val clip = ClipData.newPlainText("CSC SMS", draftText)
                                            clipboard.setPrimaryClip(clip)
                                            Toast.makeText(context, "Notification copied to clipboard!", Toast.LENGTH_SHORT).show()
                                        },
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Icon(Icons.Default.ContentCopy, null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Copy Message")
                                    }

                                    // Button to Share via WhatsApp
                                    Button(
                                        onClick = {
                                            try {
                                                // Clean up phone for api formatting
                                                val cleanPhone = order.customerPhone.trim()
                                                val phoneFormatted = if (cleanPhone.length == 10) "91$cleanPhone" else cleanPhone
                                                val url = "https://api.whatsapp.com/send?phone=$phoneFormatted&text=" + Uri.encode(draftText)
                                                val shareIntent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                                                context.startActivity(shareIntent)
                                            } catch (e: Exception) {
                                                e.printStackTrace()
                                                Toast.makeText(context, "WhatsApp is not installed on this device.", Toast.LENGTH_SHORT).show()
                                            }
                                        },
                                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF075E54)),
                                        modifier = Modifier.weight(1.2f)
                                    ) {
                                        Icon(Icons.Default.Send, null)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("Send WhatsApp")
                                    }
                                }
                            }
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

// --- SUB-COMPONENTS & LABELS ---

@Composable
fun StatusBadge(status: String) {
    val (bgColor, textColor, textValue) = when (status) {
        "Pending" -> Triple(Color(0xFFFFF9C4), Color(0xFFF57F17), "New")
        "Under Review" -> Triple(Color(0xFFE0F7FA), Color(0xFF006064), "Under Review")
        "Uploaded to Portal" -> Triple(Color(0xFFE3F2FD), Color(0xFF0D47A1), "On Portal")
        "Action Required" -> Triple(Color(0xFFFFEBEE), Color(0xFFB71C1C), "Action Required")
        "Completed" -> Triple(Color(0xFFE8F5E9), Color(0xFF1B5E20), "Completed")
        else -> Triple(Color(0xFFECEFF1), Color(0xFF37474F), status)
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(bgColor)
            .padding(vertical = 4.dp, horizontal = 10.dp)
    ) {
        Text(
            text = textValue,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Composable
fun StatBox(count: Int, label: String, highlight: Boolean = false, error: Boolean = false) {
    val themeColor = when {
        error -> Color(0xFFB71C1C)
        highlight -> MaterialTheme.colorScheme.primary
        else -> MaterialTheme.colorScheme.onPrimaryContainer
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(6.dp)
    ) {
        Text(
            text = count.toString(),
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            color = themeColor
        )
        Text(
            text = label,
            fontSize = 11.sp,
            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.6f),
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    Column(modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)) {
        Text(text = label, fontSize = 11.sp, color = Color.Gray, fontWeight = FontWeight.Medium)
        Text(text = value, fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun TrackingStepRow(
    stepNum: Int,
    title: String,
    desc: String,
    isActive: Boolean,
    isLast: Boolean = false,
    ackNumber: String = ""
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .size(24.dp)
                    .clip(CircleShape)
                    .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.4f)),
                contentAlignment = Alignment.Center
            ) {
                if (isActive) {
                    Icon(imageVector = Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(14.dp))
                } else {
                    Text(text = stepNum.toString(), color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }
            }
            if (!isLast) {
                Box(
                    modifier = Modifier
                        .width(2.dp)
                        .height(40.dp)
                        .background(if (isActive) MaterialTheme.colorScheme.primary else Color.Gray.copy(alpha = 0.3f))
                )
            }
        }

        Spacer(modifier = Modifier.width(12.dp))

        Column {
            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = if (isActive) MaterialTheme.colorScheme.onSurface else Color.Gray
            )
            Text(
                text = desc,
                fontSize = 12.sp,
                color = if (isActive) MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f) else Color.Gray.copy(alpha = 0.7f)
            )
            if (activeStepAckNumberShown(stepNum, ackNumber, isActive)) {
                Spacer(modifier = Modifier.height(4.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(MaterialTheme.colorScheme.primaryContainer)
                        .padding(vertical = 2.dp, horizontal = 6.dp)
                ) {
                    Text(
                        text = "Ack No: $ackNumber",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

private fun activeStepAckNumberShown(stepNum: Int, ackNumber: String, isActive: Boolean): Boolean {
    return stepNum == 3 && ackNumber.isNotEmpty() && isActive
}
