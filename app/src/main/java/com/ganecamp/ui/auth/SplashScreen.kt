package com.ganecamp.ui.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.ganecamp.ui.general.IsLoading
import com.ganecamp.ui.navigation.AnimalsNav
import com.ganecamp.ui.navigation.LoginNav
import com.ganecamp.ui.navigation.SplashNav

@Composable
fun SplashScreen(navController: NavController) {
    val viewModel: SplashViewModel = hiltViewModel()

    val loadingComplete by viewModel.loadingComplete.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val farm by viewModel.farm.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.loadFarmSessionManger()
    }

    if (loadingComplete) {
        LaunchedEffect(currentUser, farm) {
            if (currentUser != null && farm != null) {
                navController.navigate(AnimalsNav) {
                    popUpTo(SplashNav) { inclusive = true }
                }
            } else {
                navController.navigate(LoginNav) {
                    popUpTo(SplashNav) { inclusive = true }
                }
            }
        }
    } else {
        IsLoading()
    }

}