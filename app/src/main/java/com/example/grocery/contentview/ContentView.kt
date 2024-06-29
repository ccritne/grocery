package com.example.grocery.contentview

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.grocery.App
import com.example.grocery.drawer.Drawer
import com.example.grocery.drawer.DrawerValues
import com.example.grocery.navigation.BottomAppBar
import com.example.grocery.navigation.ContentScreen
import com.example.grocery.navigation.TopAppBar

@Composable
fun ContentView(app: App){

    val drawerValues = DrawerValues(
        scope = rememberCoroutineScope(),
        drawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
        navController = app.navController
    )

    ModalNavigationDrawer(
        drawerState = drawerValues.drawerState,
        drawerContent = { Drawer( app = app, drawerValues = drawerValues) },
    ) {
        Scaffold(
            topBar = { TopAppBar(app = app, drawerValues = drawerValues) },
            bottomBar = { BottomAppBar(app = app) },
            content = { paddingValues -> ContentScreen(app = app, paddingValues = paddingValues) }
        )
    }
}