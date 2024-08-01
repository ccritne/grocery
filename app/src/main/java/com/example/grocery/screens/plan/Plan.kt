package com.example.grocery.screens.plan

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.database.deleteItem
import com.example.grocery.database.insertPlanItem
import com.example.grocery.database.updatePlanChecked
import com.example.grocery.database.updatePlanItem
import com.example.grocery.items.Item
import com.example.grocery.items.ItemUI
import com.example.grocery.items.swipeable.SwipeableItems
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.date.Date
import com.example.grocery.uielements.floatingbuttons.ButtonAdd
import com.example.grocery.utilities.fromPairToMapEntry


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun Plan(
    app: App
) {


    app.screen = Screen.Plan

    Column(
        modifier = Modifier
            .fillMaxSize()
            .combinedClickable(
                onClick = {
                    app.copied.forEach { item ->
                        var newItem : Item
                        val id : Long

                        if (app.dailyPlanMap.containsKey(item.idMoment) && app.dailyPlanMap[item.idMoment]!!.containsKey(item.idItem)) {
                            val oldValue = app.dailyPlanMap[item.idMoment]!![item.idItem]!!.amount
                            val aggregateId = app.dailyPlanMap[item.idMoment]!![item.idItem]!!.id
                            newItem = item.copy(id = aggregateId, date = app.dateOperation.value, amount = oldValue + item.amount)
                            id = app.dbManager.updatePlanItem(item = newItem).toLong()
                        }
                        else {
                            newItem = item.copy(date = app.dateOperation.value)
                            id = app.dbManager.insertPlanItem(newItem)
                            newItem = newItem.copy(id = id)
                        }
                        if (id > 0L) {
                            app.addOrUpdateItemInPlan(newItem)
                        }
                    }


                    val toast = Toast.makeText(
                        app.applicationContext,
                        "Added all items!",
                        Toast.LENGTH_LONG
                    ) // in Activity
                    toast.show()
                    app.copied.clear()
                },
                onLongClick = {

                    app.copied = app.dailyPlanMap
                        .flatMap { item -> item.value.values }
                        .toMutableList()


                    val toast = Toast.makeText(
                        app.applicationContext,
                        "Copied all items!",
                        Toast.LENGTH_LONG
                    ) // in Activity
                    toast.show()
                },
                onLongClickLabel = "Copied items!"

            )
        ,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

        Date(
            date = app.dateOperation.value,
            enableLeft = true,
            enableRight = true,
            modifierIcons = Modifier.size(25.dp),
            fontSizeText = 35
        ){
                date -> app.changeDateOperation(date)
        }

        if (app.dailyPlanMap.isNotEmpty()) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                app.dailyPlanMap.forEach { moment ->
                    if (moment.value.isNotEmpty()) {
                        stickyHeader {
                            app.momentsMap.value[moment.key]?.let { momentName ->
                                Text(
                                    text = momentName,
                                    modifier = Modifier
                                        .padding(10.dp)
                                        .combinedClickable(
                                            onClick = {
                                                Log.i("Clicked on", app.momentsMap.value[moment.key].toString()+" <- ${moment.key}")
                                                Log.i("map", app.dailyPlanMap.entries.joinToString(separator = "\n") { (idMoment, map) -> "idMoment: $idMoment \n ${map.entries.joinToString("\n"){ (idItem, item) -> "idItem: $idItem -> item: $item"} }" })

                                                val momentKey = moment.key

                                                app.copied.forEach { item ->
                                                    var newItem : Item
                                                    val id : Long

                                                    if (app.dailyPlanMap[momentKey]!!.containsKey(item.idItem)) {
                                                        val oldValue = app.dailyPlanMap[momentKey]!![item.idItem]!!.amount
                                                        val aggregateId = app.dailyPlanMap[momentKey]!![item.idItem]!!.id
                                                        newItem = item.copy(id = aggregateId, date = app.dateOperation.value, idMoment = momentKey, amount = oldValue + item.amount)
                                                        id = app.dbManager.updatePlanItem(item = newItem).toLong()
                                                    }
                                                    else {
                                                        newItem = item.copy(date = app.dateOperation.value, idMoment = momentKey)

                                                        id = app.dbManager.insertPlanItem(newItem)
                                                        newItem = newItem.copy(id = id)
                                                    }

                                                    Log.i("New Item", newItem.toString())
                                                    if (id > 0L) {
                                                        app.addOrUpdateItemInPlan(newItem)
                                                    }
                                                }

                                                app.copied.clear()
                                            },
                                            onLongClick = {

                                                app.copied = moment.value
                                                    .map { item -> item.value }
                                                    .toMutableList()


                                                val toast = Toast.makeText(
                                                    app.applicationContext,
                                                    "Copied all items!",
                                                    Toast.LENGTH_LONG
                                                ) // in Activity
                                                toast.show()

                                                Log.i("item copied - moment", "${app.copied}")
                                            },
                                            onLongClickLabel = "Copied items!"

                                        ),
                                    fontSize = 35.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                )
                            }
                        }
                        items(moment.value.toList()) { item ->


                            val checkedState = remember {
                                mutableStateOf(item.second.checked)
                            }

                            SwipeableItems(
                                stayWhenStartEnd = false,
                                onStartEnd = {
                                    app.dbManager.deleteItem("planning", item.second.id)
                                    app.deleteItemsFromDailyPlan(
                                        listOf(
                                            Pair(moment.key, item.second.idItem)
                                        )
                                    )

                                },
                                stayWhenEndStart = true,
                                onEndStart = {

                                    val newValue = !item.second.checked

                                    checkedState.value = newValue
                                    app.dbManager.updatePlanChecked(
                                        item.second.id,
                                        newValue
                                    )

                                    val localCopy = item.second.copy(checked = newValue)

                                    app.addOrUpdateItemInPlan(localCopy)
                                },
                            ) {
                                ItemUI(
                                    app = app,
                                    item = fromPairToMapEntry(item),
                                    checkedState = checkedState.value
                                )
                            }
                        }
                    }

                }
            }
        } else
            Text(text = "No data found!")
    }
    
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ButtonAdd(app = app)
    }
}

