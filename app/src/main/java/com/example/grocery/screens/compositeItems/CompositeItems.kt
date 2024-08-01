package com.example.grocery.screens.compositeItems

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.database.deleteItem
import com.example.grocery.items.Item
import com.example.grocery.items.ItemUI
import com.example.grocery.items.swipeable.SwipeableItems
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.floatingbuttons.ButtonAdd
import com.example.grocery.utilities.fromPairToMapEntry

@Composable
fun CompositeItems(
    app: App
){

    app.screen = Screen.CompositeItems

    val itemsList by remember(app.itemsMap){
        mutableStateOf(app.itemsMap.filter { item -> item.value.children.isNotEmpty() })
    }

    var search by remember {
        mutableStateOf("")
    }

    var filteredItems by remember(app.itemsMap) { mutableStateOf(itemsList.toList()) }
    val filteredPagesItems by remember { derivedStateOf {

        if (filteredItems.isEmpty())
            null
        else
            filteredItems.chunked(10)

        }
    }


    var index by remember {
        mutableIntStateOf(0)
    }

    LaunchedEffect(search) {
        index = 0
        filteredItems = itemsList.filter { item -> item.value.name.contains(search.trim(), ignoreCase = true) }.toList()
    }

    Column(
        modifier = Modifier,
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            ButtonAdd(app = app)

            OutlinedTextField(
                leadingIcon = { Icon(imageVector = Icons.Default.Search, contentDescription = "search item") },
                value = search,
                singleLine = true,
                textStyle = TextStyle(fontSize = 30.sp),
                onValueChange = {
                    search = it
                }
            )

            IconButton(
                onClick = {
                    app.navController.navigate(Screen.Items.name)
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.List,
                    contentDescription = "CompositeItems",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {


            IconButton(
                enabled = index > 0,
                onClick = {
                    index--
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "BackIndex",
                    modifier = Modifier.fillMaxSize()
                )
            }
            Text(text = (index+1).toString(), fontSize = 24.sp)
            IconButton(
                enabled = index < (filteredPagesItems?.size?:0) - 1,
                onClick = {
                    index++
                }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = "ForwardIndex",
                    modifier = Modifier.fillMaxSize()
                )
            }
        }


        if (filteredPagesItems != null) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth()
            ) {

                filteredPagesItems?.let {
                    items(filteredPagesItems!![index]) { item ->

                        SwipeableItems(
                            stayWhenStartEnd = true,
                            onStartEnd = {
                                app.dbManager.deleteItem("items", item.first)
                                app.deleteItemsFromList(listOf(item.first))
                            },
                            stayWhenEndStart = false,
                            onEndStart = {},
                        ) { ItemUI(app = app, item = fromPairToMapEntry(item)) }
                    }
                }
            }
        } else
            Text(text = "No items found!")
    }


}