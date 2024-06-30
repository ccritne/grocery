package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.database.updateShoppingCart
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItemsShoppingCart(
    app: App,
    updateItem: UpdateItem
){
    val item = app.dbManager.updateShoppingCart(updateItem, app.placeSelector.first)

    app.addOrUpdateItemInInventory(item)
}