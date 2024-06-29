package com.example.grocery.uielements.drawer

import androidx.compose.material3.DrawerState
import androidx.navigation.NavHostController
import kotlinx.coroutines.CoroutineScope

data class DrawerValues(
    val navController: NavHostController,
    val drawerState: DrawerState,
    val scope: CoroutineScope,
)