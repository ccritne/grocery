package com.example.grocery.uielements.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import kotlinx.coroutines.launch


@Composable
fun Drawer(
    app: App,
    drawerValues: DrawerValues
){

    ModalDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.5f),
        drawerShape = RectangleShape
    ) {
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Grocery",
            onlyText = true,
            onClick = {}
        )
        HorizontalDivider()
        DrawerItem(
            drawerValues = drawerValues,
            screenToGo = null,
            textLabel = "Recreate db",
            onlyText = false,
            onClick = { app.dbManager.fillDataWeek() }
        )
    }

}