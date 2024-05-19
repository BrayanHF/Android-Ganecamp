package com.ganecamp.ui.animals

import androidx.compose.foundation.layout.Box
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController

@Composable
fun AnimalScreen(navController: NavHostController) {
    Box(){
        Text(text = "Hola esto es animales")
    }
}
