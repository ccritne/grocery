package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item
import com.example.grocery.items.MutableItem
import com.example.grocery.utilities.getFormatterDateSql

fun updateItemsPlan(
    app: App,
    item: Item,
    updatedItem: Item
){

    if (app.isNewItem.value) {
        val id = app.dbManager.insertPlanItem(updatedItem)
        updatedItem.update(id = id)
    }
    else
        app.dbManager.updatePlanItem(updatedItem)

    if (item.id != -1L)
        app.addOrUpdateItemInPlan(updatedItem, item.idMoment)

}