package com.example.grocery.screens.plan

import androidx.compose.foundation.ExperimentalFoundationApi
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
import com.example.grocery.database.updatePlanChecked
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

    val dailyPlan by remember(app.dailyPlanMap){
        app.dailyPlanMap
    }

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
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

        if (dailyPlan.isNotEmpty()) {

            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {
                dailyPlan.forEach { moment ->
                    if (moment.value.isNotEmpty()) {
                        stickyHeader {
                            app.momentsMap.value[moment.key]?.let { momentName ->
                                Text(
                                    text = momentName,
                                    modifier = Modifier.padding(10.dp),
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
                                    app.dbManager.deleteItem("planning", item.first)
                                    app.deleteItemsFromDailyPlan(
                                        listOf(
                                            Pair(moment.key, item.first)
                                        )
                                    )

                                },
                                stayWhenEndStart = true,
                                onEndStart = {

                                    val newValue = !item.second.checked

                                    checkedState.value = newValue
                                    app.dbManager.updatePlanChecked(
                                        item.first,
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

