package com.example.grocery.screens.updateitem.ui.constelements.buttons.delete

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.items.MutableItem

@Composable
fun DeleteItem(
    app: App
){
    IconButton(onClick = {
        // TODO Insert function to delete item
        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
    }
}