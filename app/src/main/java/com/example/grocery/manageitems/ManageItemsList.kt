package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updateItemOfList
import com.example.grocery.database.updatePlanItem
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItemsList(
    app: App,
    updateItem: UpdateItem
){
    val item = updateItem.toItem(app.placeSelector.first)

    if (updateItem.isNewItem) {
        val id = app.dbManager.insertItemIntoList(item)
        item.update(id = id, idItem = id)
    }
    else
        app.dbManager.updateItemOfList(item)

    if (item.id != -1L)
        app.addOrUpdateItemInList(item)
}