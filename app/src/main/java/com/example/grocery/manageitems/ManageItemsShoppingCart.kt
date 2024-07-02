package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.database.updateShoppingCart
import com.example.grocery.items.Item
import com.example.grocery.screens.updateitem.UpdateItem

fun updateItemsShoppingCart(
    app: App,
    updatedItem: Item
){
    val item = app.dbManager.updateShoppingCart(updatedItem)

    app.addOrUpdateItemInInventory(item)
}