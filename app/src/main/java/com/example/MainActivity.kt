package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.ui.CscViewModel
import com.example.ui.screens.*
import com.example.ui.theme.MyApplicationTheme

class MainActivity : ComponentActivity() {
    private val viewModel: CscViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = ROUTE_ROLE_SELECTION
                    ) {
                        composable(ROUTE_ROLE_SELECTION) {
                            RoleSelectionScreen(navController, viewModel)
                        }
                        composable(ROUTE_CUSTOMER_LOGIN) {
                            CustomerLoginScreen(navController, viewModel)
                        }
                        composable(ROUTE_CUSTOMER_DASHBOARD) {
                            CustomerDashboardScreen(navController, viewModel)
                        }
                        composable(ROUTE_NEW_ORDER) {
                            NewOrderScreen(navController, viewModel)
                        }
                        composable(
                            route = ROUTE_ORDER_DETAILS,
                            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
                            OrderDetailsScreen(orderId, navController, viewModel)
                        }
                        composable(ROUTE_OPERATOR_DASHBOARD) {
                            OperatorDashboardScreen(navController, viewModel)
                        }
                        composable(
                            route = ROUTE_UPDATE_ORDER,
                            arguments = listOf(navArgument("orderId") { type = NavType.IntType })
                        ) { backStackEntry ->
                            val orderId = backStackEntry.arguments?.getInt("orderId") ?: 0
                            UpdateOrderScreen(orderId, navController, viewModel)
                        }
                    }
                }
            }
        }
    }
}
