package com.example.grocery.screens.updateitem.ui.constelements.buttons.save

import android.util.Log
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.manageitems.updateItem
import com.example.grocery.utilities.getFormatterDateSql
import java.util.Date

@Composable
fun SaveUpdate(
    app: App,
    item: Item
){

    IconButton(onClick = {

        updateItem(
            app = app,
            updatedItem = item
        )

        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
    }
}