package com.example.grocery.screens.updateitem.ui.constelements.buttons.save

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import com.example.grocery.App
import com.example.grocery.database.checkIfItemWithIdExistsInThisMoment
import com.example.grocery.database.checkIfItemWithNameExistsInThisPlace
import com.example.grocery.items.Item
import com.example.grocery.manageitems.updateItem
import com.example.grocery.screens.Screen
import com.example.grocery.utilities.getFormatterDateSql

@Composable
fun SaveUpdate(
    app: App,
    item: Item
){
    var alertVisible by remember {
        mutableStateOf(false)
    }

    if (alertVisible){
        AlertDialog(
            onDismissRequest = { alertVisible = false },
            confirmButton = { IconButton(onClick = {alertVisible=false}){ Text(text = "OK") } },
            icon = {Icon(imageVector = Icons.Default.Warning, contentDescription = "name used", tint = Color.Red)},
            title = { Text(text = "Item exists") }
        )
    }

    IconButton(onClick = {

        var proceed = true
        if(app.screen == Screen.Items || app.screen == Screen.CompositeItems)
            proceed = !app.dbManager.checkIfItemWithNameExistsInThisPlace(item.name.trim(), app.placeSelector.first, if (app.isNewItem.value) -1L else item.id)
        if(app.screen == Screen.Plan && app.isNewItem.value)
            proceed = !app.dbManager.checkIfItemWithIdExistsInThisMoment(item.idItem, item.idMoment, getFormatterDateSql().format(app.dateOperation.value))

        if (proceed) {
            updateItem(
                app = app,
                updatedItem = item
            )

            app.setItemState(false)

            app.navController.navigateUp()
        }else
            alertVisible = true

    }) {
        Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
    }
}