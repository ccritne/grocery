package com.example.grocery.screens.updateitem.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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


    val name = remember {
        mutableStateOf("")
    }

    val id = remember {
        mutableLongStateOf(-1L)
    }

    val idItem = remember {
        mutableLongStateOf(
                app.itemsMap.value.entries.first().key
        )
    }

    val nameRef = remember {
        mutableStateOf(app.itemsMap.value.entries.first().value.name)
    }

    val nameRefDrop = remember {
        mutableStateOf(
            Pair(idItem.longValue, app.itemsMap.value.entries.first().value.name)
        )
    }

    val idUnit = remember {
        mutableLongStateOf(app.itemsMap.value[idItem.longValue]?.idUnit?: -1)
    }
    val unit = remember {
        mutableStateOf(
            app.unitsMap.value[app.itemsMap.value[idItem.longValue]?.idUnit]?.second?: ""
        )
    }

    val momentSelector = remember {
        mutableLongStateOf(app.momentsMap.value.entries.first().key)
    }

    val amount = remember {
        mutableIntStateOf(0)
    }

    val amountInventory = remember {
        mutableIntStateOf(0)
    }

    val item = remember {
        mutableStateOf(
            Item(
                id = -1,
                idItem = app.itemsMap.value[0]?.idItem?:-1,
                name = "",
                amount = 0,
                amountInventory = 0,
                date = app.dateOperation.value,
                price = 0f,
                checked = false,
                idParent = -1,
                idUnit = app.itemsMap.value[0]?.idUnit?:-1,
                idMoment = app.momentsMap.value.entries.first().key,
                idPlace = app.placeSelector.first
            )
        )
    }

    if (!app.isNewItem.value){
        id.longValue = app.item.value.id
        name.value = app.item.value.name
        idItem.longValue = app.item.value.idItem
        nameRef.value = app.item.value.name
        nameRefDrop.value = Pair(idItem.longValue, nameRef.value)
        idUnit.longValue = app.item.value.idUnit
        unit.value = app.unitsMap.value[idUnit.longValue]!!.second
        momentSelector.longValue = app.item.value.idMoment
        amount.intValue = app.item.value.amount
        amountInventory.intValue = app.item.value.amountInventory

    }


    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = if(app.screen != Screen.Items) Arrangement.SpaceEvenly else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(app.screen == Screen.Items)
            UiNameListItems(item.value.name){
                item.value = item.value.copy(name = it)
            }
        else
            UiNameReference(
                app.itemsMap.value,
                Pair(item.value.idItem, app.itemsMap.value[item.value.idItem]?.name)
            ){
                item.value = item.value.copy(idItem = it, name = app.itemsMap.value[it]?.name?:"", idUnit = app.itemsMap.value[it]?.idUnit?:-1)
            }

        if (app.screen == Screen.Plan)
            UiMomentsDateReference(
                app.momentsMap.value,
                item.value.date,
                item.value.idMoment
            ) {
                moment, date -> app.changeDateOperation(date)
                item.value = item.value.copy(idMoment = moment, date=date)
            }

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 50.dp, bottom = 50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UiAmountReference(
                starter = if(app.screen == Screen.Items) item.value.amountInventory else item.value.amount,
                modifier = Modifier.fillMaxWidth(0.8f)
            ){
                if(app.screen == Screen.Items)
                    item.value = item.value.copy(amountInventory = it)
                else
                    item.value = item.value.copy(amount = it)
            }
            UnitSelectorUpdateItem(
                Pair(item.value.idUnit, app.unitsMap.value[item.value.idUnit]?.second),
                app.screen == Screen.Items,
                app.unitsMap.value
            ){
                item.value = item.value.copy(idUnit = it)
            }
        }



       RowActions(
           app = app,
           newId = id,
           newIdItem = idItem,
           newAmount = amount,
           newDate = app.dateOperation,
           newIdMoment = momentSelector,
           newName = if(app.screen == Screen.Items) name else nameRef,
           newIdUnit = idUnit,
           item = item
       )


    }
}