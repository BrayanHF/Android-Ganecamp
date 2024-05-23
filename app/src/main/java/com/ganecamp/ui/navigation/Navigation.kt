package com.ganecamp.ui.navigation

import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ganecamp.ui.animals.AnimalScreen
import com.ganecamp.ui.lot.LotScreen
import com.ganecamp.ui.scan.ScanScreen
import com.ganecamp.ui.theme.*

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
    NavigationBar(
        containerColor = LightGreen,
        contentColor = Black
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(
                label = {
                    Text(
                        text = stringResource(id = screen.title),
                        style = Typography.titleSmall
                    )
                },
                icon = {
                    Icon(
                        painter = painterResource(id = screen.icon),
                        contentDescription = stringResource(id = screen.title),
                        modifier = Modifier.height(32.dp)
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
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = DarkGreen
                )
            )
        }
    }
}
