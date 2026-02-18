package com.example.crossword

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.crossword.game.GameScreen
import com.example.crossword.todays_crossword.TodaysCrosswordScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()
            NavHost(navController = navController, startDestination = "main") {
                composable("main") {
                    MainScreen(navController = navController)
                }
                composable("game") {
                    GameScreen(navController = navController)
                }
                composable("todays_crossword") {
                    TodaysCrosswordScreen(navController = navController)
                }
            }
        }
    }
}