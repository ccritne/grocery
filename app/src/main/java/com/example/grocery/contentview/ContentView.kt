package com.example.grocery.contentview

import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.Scaffold
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import com.example.grocery.App
import com.example.grocery.drawer.Drawer
import com.example.grocery.navigation.BottomAppBar
import com.example.grocery.navigation.ContentScreen
import com.example.grocery.navigation.TopAppBar

@Composable
fun ContentView(app: App){

    val scope = rememberCoroutineScope()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)


    ModalNavigationDrawer(
        drawerState = drawerState,
        drawerContent = { Drawer( app = app, drawerState) },
    ) {
        Scaffold(
            topBar = { TopAppBar(app = app, scope = scope, drawerState = drawerState) },
            bottomBar = { BottomAppBar(app = app) },
            content = { paddingValues -> ContentScreen(app = app, paddingValues = paddingValues) }
        )
    }
}