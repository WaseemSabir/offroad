package com.example.offroad

import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.offroad.Screens.PROFILE_SCREEN
import com.example.offroad.Screens.MAP_SCREEN

/**
 * Screens used in [Destinations]
 */
private object Screens {
    const val MAP_SCREEN = "map"
    const val PROFILE_SCREEN = "profile"
}

/**
 * Destinations used in the [MainActivity]
 */
object Destinations {
    const val MAP_ROUTE = MAP_SCREEN
    const val PROFILE_ROUTE = PROFILE_SCREEN
}

/**
 * Models the navigation actions in the app.
 */
class NavigationActions(private val navController: NavHostController) {
    fun navigateToMap() {
        navController.navigate(
            MAP_SCREEN
        ) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            launchSingleTop = true
            restoreState = true
        }
    }

    fun navigateToStatistics() {
        navController.navigate(Destinations.PROFILE_ROUTE) {
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }

            launchSingleTop = true
            restoreState = true
        }
    }
}
