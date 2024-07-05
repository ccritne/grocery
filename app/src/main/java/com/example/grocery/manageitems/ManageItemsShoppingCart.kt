package com.example.grocery.manageitems

import android.util.Log
import com.example.grocery.App
import com.example.grocery.database.updateShoppingCart
import com.example.grocery.items.Item

fun updateItemsShoppingCart(
    app: App,
    updatedItem: Item,
){
    val localCopy = updatedItem.copy()

    val rows = app.dbManager.updateShoppingCart(updatedItem)


    app.addOrUpdateItemInList(localCopy)
}