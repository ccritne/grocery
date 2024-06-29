package com.example.grocery.body

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
import com.example.grocery.items.ItemUI
import com.example.grocery.items.swipeable.SwipeToDeleteContainer
import com.example.grocery.utilities.Screen
import com.example.grocery.utilities.fromPairToMapEntry


@Composable
fun ListItems(
    app: App
){

    app.screen = Screen.Items

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
                        SwipeToDeleteContainer(
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