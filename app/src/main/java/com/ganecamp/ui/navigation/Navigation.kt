package com.ganecamp.ui.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.ganecamp.ui.animal.AnimalDetailScreen
import com.ganecamp.ui.animal.AnimalFormScreen
import com.ganecamp.ui.animal.AnimalScreen
import com.ganecamp.ui.lot.LotDetailScreen
import com.ganecamp.ui.lot.LotFormScreen
import com.ganecamp.ui.lot.LotScreen
import com.ganecamp.ui.scan.ScanScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        if (currentDestination in bottomBarItems.map { it.screen }) {
            BottomBar(navController)
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = AnimalsNav,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<AnimalsNav> { AnimalScreen(navController) }
            composable<LotsNav> { LotScreen(navController) }
            composable<ScanNav> { ScanScreen(navController) }
            composable<AnimalDetailNav> { backStackEntry ->
                val animalDetailNav = backStackEntry.toRoute<AnimalDetailNav>()
                AnimalDetailScreen(navController, animalDetailNav.animalId)
            }
            composable<LotDetailNav> { backStackEntry ->
                val lotDetailNav = backStackEntry.toRoute<LotDetailNav>()
                LotDetailScreen(navController, lotDetailNav.lotId)
            }
            composable<AnimalFormNav> { backStackEntry ->
                val animalFormNav = backStackEntry.toRoute<AnimalFormNav>()
                AnimalFormScreen(navController, animalFormNav.animalId, animalFormNav.tag)
            }
            composable<LotFormNav> { backStackEntry ->
                val lotFormNav = backStackEntry.toRoute<LotFormNav>()
                LotFormScreen(navController, lotFormNav.lotId)
            }
        }
    }
}