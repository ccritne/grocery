package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updateItemOfList
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItemsList(
    app: App,
    updatedItem: Item
){


    if (app.isNewItem.value) {
        val id = app.dbManager.insertItemIntoList(updatedItem)
        updatedItem.update(id = id, idItem = id)
    }
    else
        app.dbManager.updateItemOfList(updatedItem)


    if (updatedItem.id != -1L)
        app.addOrUpdateItemInList(updatedItem)
}