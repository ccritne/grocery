package com.example.grocery.screens.updateitem.ui.constelements.buttons.delete

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.grocery.App
import com.example.grocery.database.deleteItem
import com.example.grocery.screens.Screen

@Composable
fun DeleteItem(
    app: App
){
    IconButton(onClick = {

        app.dbManager.deleteItem(
            when(app.screen){
                Screen.Plan -> "planning"
                else -> "items"
            },
            app.item.value.id
        )

        when(app.screen){
            Screen.Plan -> app.deleteItemsFromDailyPlan(listOf(Pair(app.item.value.idMoment, app.item.value.idItem)))
            else -> app.deleteItemsFromList(listOf(app.item.value.idItem))
        }

        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
    }
}