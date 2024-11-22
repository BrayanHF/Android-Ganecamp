package com.ganecamp.ui.screen.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.ui.navigation.screens.AnimalsNav
import com.ganecamp.ui.navigation.screens.LoginNav
import com.ganecamp.ui.navigation.screens.SplashNav

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = hiltViewModel()
    val load by viewModel.load.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFarmSessionManger()
    }

    LaunchedEffect(load) {
        if (isLoading) return@LaunchedEffect
        if (load) {
            navController.navigate(AnimalsNav) {
                popUpTo(SplashNav) { inclusive = true }
            }
        } else {
            navController.navigate(LoginNav) {
                popUpTo(SplashNav) { inclusive = true }
            }
        }
    }

}