package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.database.insertItemIntoList
import com.example.grocery.database.updateItemOfList
import com.example.grocery.items.Item

fun updateItemsList(
    app: App,
    updatedItem: Item
){
    var localCopy = updatedItem.copy()
    var resultId = -1L

    if(app.isNewItem.value) {
        resultId = app.dbManager.insertItemIntoList(updatedItem)
        if(resultId != -1L)
            localCopy = updatedItem.copy(id = resultId, idItem = resultId)
    }else
        resultId = app.dbManager.updateItemOfList(localCopy).toLong()

    if (resultId != -1L)
        app.addOrUpdateItemInList(localCopy)
}