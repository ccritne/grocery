package com.example.grocery.screens.updateitem.ui.constelements.buttons.save

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.manageitems.updateItem
import com.example.grocery.utilities.getFormatterDateSql
import java.util.Date

@Composable
fun SaveUpdate(
    app: App,
    newId: MutableLongState,
    newIdItem: MutableLongState,
    newName: MutableState<String>,
    newIdMoment: MutableLongState,
    newDate: MutableState<Date>,
    newAmount: MutableIntState,
    newIdUnit: MutableLongState,
){
    IconButton(onClick = {

        val updatedItem = Item(
            id = newId.longValue,
            name = newName.value,
            amount = newAmount.intValue,
            amountInventory = app.item.value.amountInventory,
            date = getFormatterDateSql().format(newDate.value),
            price = -1.0f,
            checked = app.item.value.checked,
            idParent = -1L,
            idItem = newIdItem.longValue,
            idMoment = newIdMoment.longValue,
            idUnit = newIdUnit.longValue,
            idPlace = app.placeSelector.first
        )

        updateItem(
            app = app,
            updatedItem = updatedItem
        )

        app.navController.navigateUp()
    }) {
        Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
    }
}