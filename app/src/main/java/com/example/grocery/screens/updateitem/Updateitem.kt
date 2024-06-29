package com.example.grocery.screens.updateitem

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.grocery.App
import com.example.grocery.screens.Screen
import com.example.grocery.screens.updateitem.ui.UpdateItem
import com.example.grocery.screens.updateitem.ui.constelements.buttons.RowActions
import com.example.grocery.screens.updateitem.ui.constelements.unit.UnitSelectorUpdateItem
import com.example.grocery.screens.updateitem.ui.referenceSetup.amount.UiAmountReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.moments.UiMomentsDateReference
import com.example.grocery.screens.updateitem.ui.referenceSetup.name.UiNameReference
import com.example.grocery.screens.updateitem.ui.setup.name.UiNameList


@Composable
fun UpdateItem(
    app: App
) {

    val updateItem by remember(app.screen){
        mutableStateOf(
            UpdateItem(
                forSetup = app.screen == Screen.Items,
                itemsMap = app.itemsMap.value,
                unitsMap = app.unitsMap.value,
                areThereMomentsDate = app.screen == Screen.Plan,
                momentsMap = app.momentsMap.value,
                forShoppingCart = app.screen == Screen.ShoppingCart,
                isNewItem = app.isNewItem.value,
                amountInventory = if (app.screen == Screen.ShoppingCart)
                    app.item.value.amountInventory
                else
                    0,
                idItem = if (!app.isNewItem.value || app.screen != Screen.Items)
                    app.itemsMap.value.entries.first().key
                else
                    -1
            )
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = if(app.screen != Screen.Items) Arrangement.SpaceEvenly else Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if(updateItem.forSetup)
            UiNameList(updateItem)
        else
            UiNameReference(updateItem)


        if (!updateItem.forSetup) {

            if (updateItem.areThereMomentsDate)
                UiMomentsDateReference(updateItem) {
                    app.changeDateOperation(it)
                }

            UiAmountReference(updateItem)
        }
        else
            UnitSelectorUpdateItem(updateItem)


        RowActions(app, updateItem)


    }
}