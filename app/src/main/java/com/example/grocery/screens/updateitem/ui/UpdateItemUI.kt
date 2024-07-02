package com.example.grocery.screens.updateitem.ui

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.screens.Screen
import com.example.grocery.screens.updateitem.ui.constelements.buttons.RowActions
import com.example.grocery.screens.updateitem.ui.constelements.unit.UnitSelectorUpdateItem
import com.example.grocery.screens.updateitem.ui.referenceSetup.amount.UiAmountReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.moments.UiMomentsDateReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.name.UiNameReference
import com.example.grocery.screens.updateitem.ui.setup.name.UiNameListItems


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
            UiNameListItems(name)
        else
            UiNameReference(app.itemsMap.value, app.unitsMap.value, idItem, nameRef, nameRefDrop, idUnit, unit)

        if (app.screen != Screen.Items) {
            if (app.screen == Screen.Plan)
                UiMomentsDateReference(
                    app.momentsMap.value,
                    app.dateOperation,
                    momentSelector
                ) {
                    app.changeDateOperation(it)
                }

            UiAmountReference(
                app.screen == Screen.ShoppingCart,
                unit,
                amount,
                amountInventory
            )
        }
        else
            UnitSelectorUpdateItem(app.unitsMap.value, idUnit, unit)

       RowActions(
           app = app,
           newId = id,
           newIdItem = idItem,
           newAmount = amount,
           newDate = app.dateOperation,
           newIdMoment = momentSelector,
           newName = if(app.screen == Screen.Items) name else nameRef,
           newIdUnit = idUnit
       )


    }
}