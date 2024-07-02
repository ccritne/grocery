package com.example.grocery.screens.updateitem.ui.constelements.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.screens.updateitem.ui.constelements.buttons.delete.DeleteItem
import com.example.grocery.screens.updateitem.ui.constelements.buttons.save.SaveUpdate
import java.util.Date

@Composable
fun RowActions(
    app: App,
    newId: MutableLongState,
    newIdItem: MutableLongState,
    newName: MutableState<String>,
    newIdMoment: MutableLongState,
    newDate: MutableState<Date>,
    newAmount: MutableIntState,
    newIdUnit: MutableLongState,
    item: MutableState<Item>
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DeleteItem(app = app)
        SaveUpdate(
            app = app,
            newId = newId,
            newIdItem = newIdItem,
            newAmount = newAmount,
            newDate = newDate,
            newIdMoment = newIdMoment,
            newName = newName,
            newIdUnit = newIdUnit,
            item = item
        )
    }
}