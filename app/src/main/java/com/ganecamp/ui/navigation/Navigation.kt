package com.ganecamp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Text
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ganecamp.ui.animals.AnimalScreen
import com.ganecamp.ui.lot.LotScreen
import com.ganecamp.ui.scan.ScanScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val items = listOf(Screen.Animal, Screen.Lot, Screen.Scan)

    Scaffold(
        bottomBar = { BottomBar(navController, items) }
    ) { innerPadding ->
        NavHost(
            navController, startDestination = Screen.Animal.route, Modifier.padding(innerPadding)
        ) {
            composable(Screen.Animal.route) { AnimalScreen(navController) }
            composable(Screen.Lot.route) { LotScreen(navController) }
            composable(Screen.Scan.route) { ScanScreen(navController) }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, items: List<Screen>) {
    BottomNavigation {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            BottomNavigationItem(
                label = { Text(text = stringResource(id = screen.title)) },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(id = screen.title)
                    )
                },
                selected = currentDestination == screen.route,
                onClick = {
                    navController.navigate(screen.route) {
                        popUpTo(navController.graph.startDestinationId) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                })
        }
    }
}
