package com.example.grocery.screens.updateitem.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import com.example.grocery.items.Item
import com.example.grocery.screens.Screen
import com.example.grocery.screens.updateitem.ui.constelements.buttons.RowActions
import com.example.grocery.screens.updateitem.ui.constelements.unit.UnitSelectorUpdateItem
import com.example.grocery.screens.updateitem.ui.referenceSetup.amount.UiAmountReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.moments.UiMomentsDateReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.name.UiNameReference
import com.example.grocery.screens.updateitem.ui.setup.name.UiNameListItems
import com.example.grocery.utilities.getFormatterDateSql


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
                    idItem = app.itemsMap.value.entries.first().key,
                    name = "",
                    amount = 0,
                    amountInventory = 0,
                    date = app.dateOperation.value,
                    price = 0f,
                    checked = false,
                    idParent = -1,
                    idUnit = app.itemsMap.value.entries.first().value.idUnit,
                    idMoment = app.momentsMap.value.entries.first().key,
                    idPlace = app.placeSelector.first
                )
        )
    }

    var item by remember {
        mutableStateOf(initialItem)
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = if(app.screen != Screen.Items) Arrangement.SpaceEvenly else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(app.screen == Screen.Items)
            UiNameListItems(item.name){
                item = item.copy(name = it)
            }
        else
            UiNameReference(
                app.itemsMap.value,
                Pair(item.idItem, app.itemsMap.value[item.idItem]?.name)
            ){
                item = item.copy(idItem = it, name = app.itemsMap.value[it]?.name?:"", idUnit = app.itemsMap.value[it]?.idUnit?:-1)
            }

        if (app.screen == Screen.Plan)
            UiMomentsDateReference(
                app.momentsMap.value,
                item.date,
                item.idMoment
            ) {
                moment, date ->
                    item = item.copy(idMoment = moment, date = date)
            }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UiAmountReference(
                starter = if(app.screen == Screen.Items) item.amountInventory else item.amount,
                modifier = Modifier.fillMaxWidth(0.8f)
            ){

                if(app.screen == Screen.Items)
                    item = item.copy(amountInventory = it)
                else
                    item = item.copy(amount = it)

            }
            UnitSelectorUpdateItem(
                Pair(item.idUnit, app.unitsMap.value[item.idUnit]?.second),
                app.screen == Screen.Items,
                app.unitsMap.value
            ){
                item = item.copy(idUnit = it)
            }
        }



       RowActions(
           app = app,
           item = item
       )


    }
}