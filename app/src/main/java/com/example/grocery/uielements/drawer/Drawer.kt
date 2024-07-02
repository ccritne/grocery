package com.example.grocery.uielements.drawer

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import com.example.grocery.App


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