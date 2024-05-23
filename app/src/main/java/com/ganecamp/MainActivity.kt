package com.ganecamp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ganecamp.ui.navigation.Navigation
import com.ganecamp.ui.theme.AndroidGanecampTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AndroidGanecampTheme {
                Navigation()
            }
        }
    }
}