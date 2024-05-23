package com.ganecamp.ui.lot

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun LotScreen(navController: NavController){
    val viewModel: LotViewModel = hiltViewModel()
    val lots by viewModel.lots.observeAsState(initial = emptyList())
    val isLoading by viewModel.isLoading.observeAsState(initial = true)

    Box(){
        Text(text = "Hola esto es lotes")
    }
}