package com.example.offroad

import androidx.compose.material.DrawerState
import androidx.compose.material.DrawerValue
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.offroad.profile.ProfileScreen
import com.example.offroad.map.MapScreen
import com.example.offroad.util.AppModalDrawer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun NavGraph(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    startDestination: String = Destinations.MAP_ROUTE,
    navActions: NavigationActions = remember(navController) {
        NavigationActions(navController)
    }
) {
    val currentNavBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = currentNavBackStackEntry?.destination?.route ?: startDestination

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Destinations.MAP_ROUTE) {
            AppModalDrawer(drawerState, currentRoute, navActions) {
                MapScreen(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
        composable(Destinations.PROFILE_ROUTE) {
            AppModalDrawer(drawerState, currentRoute, navActions) {
                ProfileScreen(openDrawer = { coroutineScope.launch { drawerState.open() } })
            }
        }
    }
}
