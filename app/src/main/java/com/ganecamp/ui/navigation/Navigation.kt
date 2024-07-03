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
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.ganecamp.ui.animals.AnimalDetailScreen
import com.ganecamp.ui.animals.AnimalDetailTopBarContent
import com.ganecamp.ui.animals.AnimalFormScreen
import com.ganecamp.ui.animals.AnimalFormTopBarContent
import com.ganecamp.ui.animals.AnimalScreen
import com.ganecamp.ui.general.DefaultTopBarContent
import com.ganecamp.ui.general.TopBar
import com.ganecamp.ui.lot.LotDetailScreen
import com.ganecamp.ui.lot.LotDetailTopBarContent
import com.ganecamp.ui.lot.LotScreen
import com.ganecamp.ui.scan.ScanScreen
import com.ganecamp.ui.theme.Black
import com.ganecamp.ui.theme.DarkGreen
import com.ganecamp.ui.theme.LightGreen
import com.ganecamp.ui.theme.Typography

@Composable
fun Navigation() {
    val navController = rememberNavController()
    val items = listOf(Screen.Animal, Screen.Lot, Screen.Scan)

    Scaffold(bottomBar = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        if (currentDestination in bottomBarScreens) {
            BottomBar(navController, items)
        }
    }, topBar = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        if (currentDestination !in bottomBarScreens) {
            when (currentDestination) {
                ScreenInternal.AnimalDetail.route -> TopBar(navController) { AnimalDetailTopBarContent() }
                ScreenInternal.LotDetail.route -> TopBar(navController) { LotDetailTopBarContent() }
                ScreenInternal.AnimalForm.route -> TopBar(navController) { AnimalFormTopBarContent() }
                else -> TopBar(navController) { DefaultTopBarContent() }
            }
        }
    }) { innerPadding ->
        NavHost(
            navController, startDestination = Screen.Animal.route, Modifier.padding(innerPadding)
        ) {
            composable(Screen.Animal.route) { AnimalScreen(navController) }
            composable(Screen.Lot.route) { LotScreen(navController) }
            composable(Screen.Scan.route) { ScanScreen(navController) }
            composable(
                route = ScreenInternal.AnimalDetail.route,
                arguments = listOf(navArgument("animalId") { type = NavType.IntType },
                    navArgument("lotId") { type = NavType.IntType })
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getInt("animalId") ?: 0
                val lotId = backStackEntry.arguments?.getInt("lotId") ?: 0
                AnimalDetailScreen(navController, animalId, lotId)
            }
            composable(
                route = ScreenInternal.LotDetail.route,
                arguments = listOf(navArgument("lotId") { type = NavType.IntType })
            ) { backStackEntry ->
                val lotId = backStackEntry.arguments?.getInt("lotId") ?: 0
                LotDetailScreen(navController, lotId)
            }
            composable(
                route = ScreenInternal.AnimalForm.route,
                arguments = listOf(navArgument("animalId") { type = NavType.IntType })
            ) { backStackEntry ->
                val animalId = backStackEntry.arguments?.getInt("animalId") ?: 0
                AnimalFormScreen(navController, animalId)
            }
        }
    }
}

@Composable
fun BottomBar(navController: NavHostController, items: List<Screen>) {
    NavigationBar(containerColor = LightGreen, contentColor = Black) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route

        items.forEach { screen ->
            NavigationBarItem(label = {
                Text(
                    text = stringResource(id = screen.title), style = Typography.titleSmall
                )
            }, icon = {
                Icon(
                    painter = painterResource(id = screen.icon),
                    contentDescription = stringResource(id = screen.title),
                    modifier = Modifier.height(32.dp)
                )
            }, selected = currentDestination == screen.route, onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.startDestinationId) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }, colors = NavigationBarItemDefaults.colors(
                indicatorColor = DarkGreen
            )
            )
        }
    }
}
