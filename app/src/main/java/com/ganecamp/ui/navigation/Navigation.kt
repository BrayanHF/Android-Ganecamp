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
import com.ganecamp.ui.navigation.screens.AnimalDetailNav
import com.ganecamp.ui.navigation.screens.AnimalFormNav
import com.ganecamp.ui.navigation.screens.AnimalsNav
import com.ganecamp.ui.navigation.screens.EventAddFormNav
import com.ganecamp.ui.navigation.screens.LoginNav
import com.ganecamp.ui.navigation.screens.LotDetailNav
import com.ganecamp.ui.navigation.screens.LotFormNav
import com.ganecamp.ui.navigation.screens.LotsNav
import com.ganecamp.ui.navigation.screens.RegisterNav
import com.ganecamp.ui.navigation.screens.ScanNav
import com.ganecamp.ui.navigation.screens.SplashNav
import com.ganecamp.ui.navigation.screens.VaccineAddFormNav
import com.ganecamp.ui.navigation.screens.WeightFormNav
import com.ganecamp.ui.screen.animal.AnimalDetailScreen
import com.ganecamp.ui.screen.animal.AnimalFormScreen
import com.ganecamp.ui.screen.animal.AnimalScreen
import com.ganecamp.ui.screen.auth.LoginScreen
import com.ganecamp.ui.screen.auth.RegisterScreen
import com.ganecamp.ui.screen.auth.SplashScreen
import com.ganecamp.ui.screen.event.EventAddFormScreen
import com.ganecamp.ui.screen.lot.LotDetailScreen
import com.ganecamp.ui.screen.lot.LotFormScreen
import com.ganecamp.ui.screen.lot.LotScreen
import com.ganecamp.ui.screen.scan.ScanScreen
import com.ganecamp.ui.screen.vaccine.VaccineAddFormScreen
import com.ganecamp.ui.screen.weight.WeightFormScreen

@Composable
fun Navigation() {
    val navController = rememberNavController()

    Scaffold(bottomBar = {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentDestination = navBackStackEntry?.destination?.route
        if (currentDestination in bottomBarItems.map { it.screen }) {
            NavigationBar(navController)
        }
    }) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = SplashNav,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable<SplashNav> { SplashScreen(navController) }
            composable<LoginNav> { LoginScreen(navController) }
            composable<RegisterNav> { RegisterScreen(navController) }
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
            composable<VaccineAddFormNav> { backStackEntry ->
                val vaccineAddFormNav = backStackEntry.toRoute<VaccineAddFormNav>()
                VaccineAddFormScreen(navController, vaccineAddFormNav.animalId)
            }
            composable<EventAddFormNav> { backStackEntry ->
                val eventAddFormNav = backStackEntry.toRoute<EventAddFormNav>()
                EventAddFormScreen(
                    navController,
                    eventAddFormNav.entityId,
                    eventAddFormNav.entityType
                )
            }
            composable<WeightFormNav> { backStackEntry ->
                val weightAddFormNav = backStackEntry.toRoute<WeightFormNav>()
                WeightFormScreen(navController, weightAddFormNav.animalId)
            }
        }
    }
}