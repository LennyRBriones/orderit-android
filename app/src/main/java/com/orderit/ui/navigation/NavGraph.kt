package com.orderit.ui.navigation

import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.orderit.ui.login.LoginScreen

@Composable
fun OrderItNavGraph() {
    val rootNavController = rememberNavController()

    NavHost(
        navController = rootNavController,
        startDestination = LoginRoute
    ) {
        composable<LoginRoute> {
            LoginScreen(
                onLoginSuccess = {
                    rootNavController.navigate(MainRoute) {
                        popUpTo<LoginRoute> { inclusive = true }
                    }
                }
            )
        }
        composable<MainRoute> {
            MainScaffold(
                onLogout = {
                    rootNavController.navigate(LoginRoute) {
                        popUpTo<MainRoute> { inclusive = true }
                    }
                }
            )
        }
    }
}
