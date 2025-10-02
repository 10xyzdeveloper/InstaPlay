package com.house.playtest

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.house.playtest.presentation.navigation.NavGraph
import com.house.playtest.ui.theme.PlayTestTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            PlayTestTheme {
                val navController = rememberNavController()
                NavGraph(navController = navController)
            }
        }
    }
}