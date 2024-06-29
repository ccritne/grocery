package com.example.grocery.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.dropdownmenu.DropdownMenuSelection
import com.example.grocery.drawer.DrawerValues
import com.example.grocery.utilities.Screen
import kotlinx.coroutines.launch

@Composable
fun TopAppBar(
    app: App,
    drawerValues: DrawerValues
){


    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        IconButton(onClick = {
            drawerValues.scope.launch {
                drawerValues.drawerState.open()
            }
        }) {
            Icon(imageVector = Icons.Default.Menu, contentDescription = "House")
        }

        DropdownMenuSelection(
            list = app.placesMap.value.toList(),
            starter = app.placeSelector
        ) {
            if (app.navController.currentDestination?.route == Screen.UpdateItem.name)
                app.navController.navigateUp()

            app.updatePlaceSelector(it)

        }

        IconButton(onClick = {
            app.navController.navigate(Screen.Profile.name)
        }) {
            Icon(
                imageVector = Icons.Default.Person,
                contentDescription = "Calendar"
            )
        }
    }
}