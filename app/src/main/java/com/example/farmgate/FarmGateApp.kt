package com.example.farmgate


import androidx.compose.runtime.Composable
import androidx.navigation.compose.rememberNavController
import com.example.farmgate.core.navigation.AppNavGraph

@Composable
fun FarmGateApp() {
    val navController = rememberNavController()

    AppNavGraph(navController = navController)
}