package com.example.grocery.screens.updateitem.ui.constelements.buttons

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.screens.updateitem.ui.UpdateItem
import com.example.grocery.screens.updateitem.ui.constelements.buttons.delete.DeleteItem
import com.example.grocery.screens.updateitem.ui.constelements.buttons.save.SaveUpdate

@Composable
fun RowActions(
    app: App,
    updateItem: UpdateItem
){
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        DeleteItem(app = app, updateItem = updateItem)
        SaveUpdate(app = app, updateItem = updateItem)
    }
}