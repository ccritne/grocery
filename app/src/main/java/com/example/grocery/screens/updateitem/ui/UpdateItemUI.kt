package com.example.grocery.screens.updateitem.ui

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.screens.Screen
import com.example.grocery.screens.updateitem.ui.constelements.buttons.RowActions
import com.example.grocery.screens.updateitem.ui.constelements.unit.UnitSelectorUpdateItem
import com.example.grocery.screens.updateitem.ui.referenceSetup.amount.UiAmountReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.moments.UiMomentsDateReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.name.UiNameReference
import com.example.grocery.screens.updateitem.ui.setup.name.UiNameListItems
import com.example.grocery.uielements.dynamicform.DynamicChildrenForm


@Composable
fun UpdateItem(
    app: App
) {
    val itemLast by app.item

    val isNewItem by app.isNewItem



    val initialItem by remember(itemLast, isNewItem) {
        mutableStateOf(
            if (!isNewItem)
                itemLast
            else
                Item(
                    id = 0,
                    idItem = if(app.screen == Screen.Plan) app.itemsMap.entries.first().key else 0,
                    name = if(app.screen == Screen.Plan) app.itemsMap.entries.first().value.name else "",
                    amount = 0,
                    amountInventory = 0,
                    date = app.dateOperation.value,
                    price = 0f,
                    checked = false,
                    children = emptyList(),
                    idUnit = if(app.screen == Screen.Plan) app.itemsMap.entries.first().value.idUnit else if(app.screen == Screen.CompositeItems) 2L else app.unitsMap.value.entries.first().key,
                    idMoment = app.momentsMap.value.entries.first().key,
                    idPlace = app.placeSelector.first
                )

        )
    }

    var item by remember {
        mutableStateOf(initialItem)
    }

    val amountInventoryStarter by remember {
        mutableIntStateOf(item.amountInventory)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        if(app.screen == Screen.Items || app.screen == Screen.CompositeItems) {

            UiNameListItems(item.name) {
                item = item.copy(name = it)
            }
        }else
            UiNameReference(
                modifier = Modifier.fillMaxWidth(),
                app.itemsMap,
                if(app.screen == Screen.Plan)
                    Pair(item.idItem, app.itemsMap[item.idItem]?.name)
                else
                    Pair(item.id, item.name)
            ){
                item = item.copy(idItem = it, name = app.itemsMap[it]?.name?:"", idUnit = app.itemsMap[it]!!.idUnit)
            }

        if (app.screen == Screen.Plan)
            UiMomentsDateReference(
                app.momentsMap.value,
                item.date,
                item.idMoment
            ) {
                moment, date ->
                    item = item.copy(idMoment = moment, date = date)
                    app.changeDateOperation(date)
            }

        if(app.screen == Screen.ShoppingCart){
            Text(text = "In your inventory: "+amountInventoryStarter.toString()+
                    app.unitsMap.value[item.idUnit]?.second, fontSize = 30.sp)
            Text(text = "Will have: "+item.amountInventory.toString()+
                    app.unitsMap.value[item.idUnit]?.second, fontSize = 30.sp)
        }

        if (app.screen != Screen.CompositeItems) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 50.dp, bottom = 50.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {


                UiAmountReference(
                    starter = when(app.screen) {
                        Screen.Plan -> item.amount
                        Screen.Items -> item.amountInventory
                        else -> 0
                    },
                    fontSize = 50.sp,
                    modifier = Modifier
                        .size(width = 250.dp, height = 100.dp)
                        .padding(end = 35.dp)
                ){
                    item = when(app.screen){
                        Screen.Plan -> item.copy(amount = it)
                        Screen.ShoppingCart -> item.copy(amountInventory = amountInventoryStarter + it)
                        Screen.Items ->item.copy(amountInventory = it)
                        else -> item
                    }

                }
                UnitSelectorUpdateItem(
                    modifier = Modifier.size(width = 50.dp, height = 100.dp),
                    starter = Pair(item.idUnit, app.unitsMap.value[item.idUnit]?.second),
                    enabled = app.screen == Screen.Items,
                    unitsMap = app.unitsMap.value
                ){
                    item = item.copy(idUnit = it)
                }
            }
        }

        if (app.screen == Screen.CompositeItems && app.itemsMap.isNotEmpty() )
            DynamicChildrenForm(
                modifier = Modifier.fillMaxWidth().fillMaxHeight(0.75f),
                app = app,
                starter = item.children,
                onAddChildren = { newItem ->
                    val newList = item.children.toMutableList()
                    newList.add(newItem)

                    item = item.copy(children = newList)

                },
                onDeleteChildren = {index ->

                    val newList = item.children.toMutableList()
                    newList.removeAt(index)

                    item = item.copy(children = newList)
                },
                onChangeChildren = { index, changedItem ->
                    val updatedList = item.children.toMutableList()
                    updatedList[index] = changedItem
                    item = item.copy(children = updatedList)
                }
            )


        RowActions(
            app = app,
            item = item
        )

    }
}