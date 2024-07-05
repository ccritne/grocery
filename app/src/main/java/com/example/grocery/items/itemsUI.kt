package com.example.grocery.items

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Popup
import androidx.compose.ui.window.PopupProperties
import androidx.compose.ui.zIndex
import com.example.grocery.App
import com.example.grocery.screens.Screen

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemUI(
    app: App,
    item: Map.Entry<Long, Item>,
    checkedState : Boolean = false
){

    val itemObject = item.value

    var isNeedsVisible by remember {
        mutableStateOf(false)
    }

    if (isNeedsVisible){

        val itemId = itemObject.idItem

        val listNeeds = app.itemsMap.value[itemId]!!.children

        BasicAlertDialog(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(0.75f)
                .background(Color.White),
            onDismissRequest = {isNeedsVisible = true},
        ){
            LazyColumn(
                modifier = Modifier.fillMaxSize().padding(bottom = 50.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                items(listNeeds) { need ->
                    Row(
                        modifier = Modifier.fillMaxWidth().height(50.dp),
                        horizontalArrangement = Arrangement.SpaceAround,
                        verticalAlignment = Alignment.CenterVertically
                        ) {
                        Text(app.itemsMap.value[need.id]!!.name)
                        Row(
                        ) {
                            Text(need.amount.toString())
                            Text(app.unitsMap.value[app.itemsMap.value[need.id]!!.idUnit]!!.second)
                        }
                    }
                }
            }
            IconButton(onClick = { isNeedsVisible = false }) {
                Icon(imageVector = Icons.Default.Done, contentDescription = "see", tint = Color.Gray)
            }

        }
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    app.setTempItem(item = itemObject.copy())
                    app.setItemState(false)
                    app.navController.navigate(Screen.UpdateItem.name)
                },
                onLongClick = {
                    if (app.screen == Screen.Plan)
                        isNeedsVisible = true
                },
                onLongClickLabel = "See needs"

            )
            .background(
                color = if (app.screen == Screen.ShoppingCart && itemObject.amountInventory >= itemObject.amount)
                    Color.Green
                else
                    Color.Transparent
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (checkedState)
                Icon(
                    imageVector = Icons.Default.Check, contentDescription = "Check",
                    modifier = Modifier.padding(start = 15.dp)
                )

            Text(
                text = itemObject.name,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = (if (app.screen == Screen.ShoppingCart)
                    itemObject.amountInventory.toString() + "/"
                else "")
                        +
                        (if(app.screen == Screen.Items) itemObject.amountInventory.toString() else itemObject.amount.toString())
                        +
                        app.unitsMap.value[itemObject.idUnit]?.second,
                fontSize = 30.sp,
                modifier = Modifier.padding(end = 15.dp)
            )

        }

    }


}