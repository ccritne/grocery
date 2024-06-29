package com.example.grocery.drawer

import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.utilities.Screen
import kotlinx.coroutines.launch

@Composable
fun DrawerItem(
    drawerValues: DrawerValues,
    screenToGo: Screen?,
    textLabel: String,
    onlyText: Boolean,
    onClick : () -> Unit,
){
    NavigationDrawerItem(
        shape = RectangleShape,
        label = { Text(textLabel, fontSize = 16.sp) },
        selected = false,
        onClick = {
            if(!onlyText) {
                drawerValues.scope.launch {
                    drawerValues.drawerState.close()
                }

                if (screenToGo != null)
                    drawerValues.navController.navigate(screenToGo.name)

                onClick()
            }
        }
    )
}