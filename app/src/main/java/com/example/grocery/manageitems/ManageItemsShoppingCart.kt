package com.example.grocery.manageitems

import com.example.grocery.App
import com.example.grocery.database.updateShoppingCart
import com.example.grocery.items.Item

fun updateItemsShoppingCart(
    app: App,
    updatedItem: Item,
    amount: Int,
){

    val rows = app.dbManager.updateShoppingCart(updatedItem, amount)

    if(rows != 0)
        app.addOrUpdateItemInList(updatedItem)
}