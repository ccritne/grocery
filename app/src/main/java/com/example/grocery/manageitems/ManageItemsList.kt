package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updateItemOfList
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item

fun updateItemsList(
    app: App,
    updatedItem: Item
){

    var localCopy = updatedItem.copy()
    if (app.isNewItem.value) {
        val id = app.dbManager.insertItemIntoList(updatedItem)
        localCopy = updatedItem.copy(id = id, idItem = id)
    }
    else
        app.dbManager.updateItemOfList(localCopy)

    if (updatedItem.id != -1L)
        app.addOrUpdateItemInList(localCopy)
}