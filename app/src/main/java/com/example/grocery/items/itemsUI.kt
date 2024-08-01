package com.example.grocery.items

import android.util.Log
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
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
import com.example.grocery.utilities.fromPairToMapEntry

@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ItemUI(
    app: App,
    item: Map.Entry<Long, Item>,
    checkedState : Boolean = false,
    isANeed : Boolean = false
){

    val itemObject = item.value

    var isNeedsVisible by remember {
        mutableStateOf(false)
    }

    var isNeedsGeneralVisible by remember {
        mutableStateOf(false)
    }

    if (isNeedsVisible || isNeedsGeneralVisible){

        val itemId = itemObject.idItem

        val listNeeds = app.itemsMap[itemId]!!.children

        BasicAlertDialog(
            modifier = Modifier
                .height(500.dp)
                .fillMaxWidth(0.75f)
                .background(Color.White),
            onDismissRequest = {
                isNeedsVisible = false
                isNeedsGeneralVisible = false
            }
        ){
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 50.dp),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                listNeeds.forEach { need ->

                    val childItem = app.itemsMap[need.id]!!.copy(amount = need.amount)

                    ItemUI(app = app, item = fromPairToMapEntry(Pair(need.id, childItem)), isANeed = true)
                }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceAround,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(onClick = { isNeedsVisible = false }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "back",
                            tint = Color.Gray
                        )
                    }

                    IconButton(onClick = { isNeedsVisible = false }) {
                        Icon(
                            imageVector = Icons.Default.Done,
                            contentDescription = "see",
                            tint = Color.Gray
                        )
                    }
                }
            }


        }
    }



    Row(
        modifier = Modifier
            .fillMaxWidth()
            .combinedClickable(
                onClick = {
                    if (!isANeed) {
                        app.setTempItem(item = itemObject.copy())
                        app.setItemState(false)
                        Log.i("To change", itemObject.toString())
                        app.navController.navigate(Screen.UpdateItem.name)
                    }
                },
                onLongClick = {
                    if (app.screen == Screen.Plan) {
                        isNeedsVisible = true
                        isNeedsGeneralVisible = false
                    }
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

        if (app.screen != Screen.CompositeItems) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (
                            when(app.screen){
                                Screen.Items -> itemObject.amountInventory.toString()
                                Screen.CompositeItems -> itemObject.amountInventory.toString()
                                Screen.ShoppingCart -> itemObject.amountInventory.toString() + "/" + itemObject.amount.toString()
                                else -> itemObject.amount.toString()
                            }
                            )
                            +
                            app.unitsMap.value[itemObject.idUnit]?.second,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(end = 15.dp)
                )

            }
        }

    }


}