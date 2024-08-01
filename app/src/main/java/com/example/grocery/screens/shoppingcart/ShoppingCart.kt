package com.example.grocery.screens.shoppingcart

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import com.example.grocery.database.selectShoppingCartInRange
import com.example.grocery.items.Item
import com.example.grocery.items.ItemUI
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.date.Date
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.utilities.fromPairToMapEntry
import java.util.concurrent.TimeUnit

fun getChildrenItems(itemsMap: Map<Long, Item>, item: Item): List<Item> {
    val listChild = mutableListOf<Item>()
    val stack = ArrayDeque<Item>()
    stack.add(item)

    while (stack.isNotEmpty()) {
        val currentItem = stack.removeFirst()
        if (currentItem.children.isNotEmpty())
            currentItem.children.forEach { need ->
                val childItem = itemsMap[need.id]!!.copy(amount = need.amount*item.amount)
                if (childItem.children.isEmpty()) {
                    listChild.add(childItem)
                } else {
                    stack.add(childItem)
                }
            }
        else
            listChild.add(currentItem)
    }

    return listChild
}

@Composable
fun ShoppingCart(app: App) {

    app.screen = Screen.ShoppingCart

    val shoppingCart = app.dbManager.selectShoppingCartInRange(
                startDate = app.formatterSql.format(app.startDateOperation.value),
                endDate = app.formatterSql.format(app.endDateOperation.value),
                idPlace = app.placeSelector.first
            )

    val filteredShoppingCart = mutableListOf<Item>()

    shoppingCart.forEach{ item ->
        filteredShoppingCart.addAll(getChildrenItems(app.itemsMap, item))
    }



    val groupedByIdAndAmount = filteredShoppingCart
        .groupBy { it.id }
        .mapValues { entry ->
            val groupedItems = entry.value
            groupedItems.reduce { acc, item ->
                acc.copy(amount = acc.amount + item.amount)
            }
        }
        .values
        .toList()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Date(
                date = app.startDateOperation.value,
                enableLeft = app.startDateOperation.value.after(getDateNow()),
                enableRight = app.startDateOperation.value.before(app.endDateOperation.value),
                modifierIcons = Modifier.size(10.dp),
                fontSizeText = 15
            ){
                app.startDateOperation.value = it
            }

            Date(
                date = app.endDateOperation.value,
                enableLeft = app.endDateOperation.value.after(app.startDateOperation.value),
                enableRight = TimeUnit.MILLISECONDS.toDays(app.endDateOperation.value.time - app.startDateOperation.value.time) < 14,
                modifierIcons = Modifier.size(10.dp),
                fontSizeText = 15
            ){
                app.endDateOperation.value = it
            }
        }

        if (shoppingCart.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top
            ) {

                groupedByIdAndAmount.forEach {

                        ItemUI(
                            app = app,
                            item = fromPairToMapEntry(Pair(it.idItem, it))
                        )

                }
            }
        } else {
            Text(text = "No shopping cart!")
        }
    }

}