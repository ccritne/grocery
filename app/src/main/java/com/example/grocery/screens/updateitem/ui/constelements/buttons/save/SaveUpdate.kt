package com.example.grocery.screens.updateitem.ui.constelements.buttons.save

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.grocery.App
import com.example.grocery.manageitems.updateItem
import com.example.grocery.screens.updateitem.UpdateItem

@Composable
fun SaveUpdate(
    app: App,
    updateItem: UpdateItem
){
    IconButton(onClick = {
        updateItem(
            app = app,
            updateItem = updateItem
        )

        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
    }
}