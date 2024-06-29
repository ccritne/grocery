package com.example.grocery.screens.updateitem.ui.constelements.buttons.delete

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import com.example.grocery.App
import com.example.grocery.screens.updateitem.ui.UpdateItem

@Composable
fun DeleteItem(
    app: App,
    updateItem: UpdateItem
){
    IconButton(onClick = {
        // TODO Insert function to delete item
        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
    }
}