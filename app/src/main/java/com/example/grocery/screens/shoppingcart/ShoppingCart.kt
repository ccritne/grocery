package com.example.grocery.screens.shoppingcart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import com.example.grocery.uielements.date.Date
import com.example.grocery.database.selectShoppingCartInRange
import com.example.grocery.screens.Screen
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.items.ItemUI
import com.example.grocery.utilities.fromPairToMapEntry

@Composable
fun ShoppingCart(app: App) {

    val shoppingCart = app.dbManager.selectShoppingCartInRange(
                startDate = app.formatterSql.format(app.startDateOperation.value),
                endDate = app.formatterSql.format(app.endDateOperation.value),
                idPlace = app.placeSelector.first
            )



    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.1f),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            Date(
                modifier = Modifier.fillMaxWidth(0.5f),
                date = app.startDateOperation,
                enableLeft = app.startDateOperation.value.after(getDateNow()),
                enableRight = app.startDateOperation.value.before(app.endDateOperation.value),
                modifierIcons = Modifier.size(15.dp),
                fontSizeText = 20
            ){
                app.startDateOperation.value = it
            }

            Date(
                modifier = Modifier.fillMaxWidth(),
                date = app.endDateOperation,
                enableLeft = app.endDateOperation.value.after(app.startDateOperation.value),
                enableRight = true,
                modifierIcons = Modifier.size(15.dp),
                fontSizeText = 20
            ){
                app.endDateOperation.value = it
            }
        }

        if (shoppingCart.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                shoppingCart.forEach {
                    ItemUI(app = app, item = fromPairToMapEntry(Pair(it.idItem, it)) )
                }
            }
        } else {
            Text(text = "No shopping cart!")
        }
    }

}