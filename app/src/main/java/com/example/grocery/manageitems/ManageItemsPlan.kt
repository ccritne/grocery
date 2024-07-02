package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item

fun updateItemsPlan(
    app: App,
    item: Item,
    updatedItem: Item
){
    var localCopy = updatedItem.copy()

    if (app.isNewItem.value) {
        val id = app.dbManager.insertPlanItem(localCopy)
        localCopy = localCopy.copy(id = id)
    }
    else
        app.dbManager.updatePlanItem(localCopy)

    if (item.id != -1L)
        app.addOrUpdateItemInPlan(localCopy, item.idMoment)

}