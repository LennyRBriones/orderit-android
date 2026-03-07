package com.orderit.ui.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.orderit.ui.components.OrderItTopBar
import com.orderit.ui.components.SideBar
import com.orderit.ui.components.SideBarItem
import com.orderit.ui.dashboard.DashboardScreen
import com.orderit.ui.menu.MenuScreen
import com.orderit.ui.metrics.MetricsScreen
import com.orderit.ui.orders.OrdersScreen
import com.orderit.ui.theme.BackgroundGray

@Composable
fun MainScaffold(
    onLogout: () -> Unit
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    var searchQuery by remember { mutableStateOf("") }

    val selectedItem = remember(backStackEntry) {
        when (backStackEntry?.destination?.route) {
            "com.orderit.ui.navigation.OrdersRoute" -> SideBarItem.Ordenes
            "com.orderit.ui.navigation.MenuRoute" -> SideBarItem.Menu
            "com.orderit.ui.navigation.MetricsRoute" -> SideBarItem.Metricas
            else -> SideBarItem.Dashboard
        }
    }

    Row(Modifier.fillMaxSize()) {
        SideBar(
            selectedItem = selectedItem,
            onItemSelected = { item ->
                val route: Any = when (item) {
                    SideBarItem.Dashboard -> DashboardRoute
                    SideBarItem.Ordenes -> OrdersRoute
                    SideBarItem.Menu -> MenuRoute
                    SideBarItem.Metricas -> MetricsRoute
                }
                navController.navigate(route) {
                    popUpTo<DashboardRoute> { saveState = true }
                    launchSingleTop = true
                    restoreState = true
                }
            },
            onLogout = onLogout
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxHeight()
                .background(BackgroundGray)
        ) {
            OrderItTopBar(
                searchQuery = searchQuery,
                onSearchChange = { searchQuery = it }
            )

            NavHost(
                navController = navController,
                startDestination = DashboardRoute,
                modifier = Modifier.weight(1f)
            ) {
                composable<DashboardRoute> { DashboardScreen() }
                composable<OrdersRoute> { OrdersScreen() }
                composable<MenuRoute> { MenuScreen() }
                composable<MetricsRoute> { MetricsScreen() }
            }
        }
    }
}
