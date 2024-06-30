package com.example.grocery.screens.listitems

import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.database.deleteItem
import com.example.grocery.uielements.floatingbuttons.ButtonAdd
import com.example.grocery.items.ItemUI
import com.example.grocery.items.swipeable.SwipeableItems
import com.example.grocery.screens.Screen
import com.example.grocery.utilities.fromPairToMapEntry


@Composable
fun ListItems(
    app: App
){

    val itemsList = app.itemsMap.value
    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { ButtonAdd(app = app) }
    ) { paddingValues ->
        if (itemsList.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxHeight(1f)
                    .fillMaxWidth()
            ) {
                items(itemsList.toList()) { item ->
                        SwipeableItems(
                            stayWhenStartEnd = true,
                            onStartEnd = {
                                app.dbManager.deleteItem("items", item.first)
                                app.deleteItemsFromList(listOf(item.first))
                            },
                            stayWhenEndStart = false,
                            onEndStart = {},
                        ) { ItemUI(app = app, item = fromPairToMapEntry(item) ) }

                }
            }
        } else
            Text(text = "No items found!")
    }

}