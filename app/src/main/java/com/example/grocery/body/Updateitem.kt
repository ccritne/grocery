package com.example.grocery.body

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.body.menu.Date
import com.example.grocery.body.menu.Moments
import com.example.grocery.utilities.Screen
import com.example.grocery.utilities.Units
import java.time.LocalDate
import java.time.format.DateTimeFormatter


@Composable
fun AmountField(amount: MutableState<String>, amountInventory: Int?=null, unitEnable: MutableState<Boolean>, unit: MutableState<Units>){

    var amountField by remember {
        mutableStateOf(
            if (amountInventory != null)
                "0"
            else
                amount.value
        )
    }

    Row(
        modifier = Modifier
            .fillMaxHeight(0.1f)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(0.5f),
            verticalAlignment = Alignment.CenterVertically
        ){
            if (amountInventory != null) {
                Text(text = amountInventory.toString(), fontSize = 35.sp)
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add", modifier = Modifier.size(35.dp))
            }
            OutlinedTextField(
                maxLines = 1,
                shape = RectangleShape,
                value = amountField,
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                placeholder = {
                    Text(
                        text = "Amount",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                },
                textStyle = TextStyle(textAlign = TextAlign.Center),
                onValueChange = {
                    amount.value = it
                    amountField = it
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
        DropdownMenuSelection(list = Units.entries, enabled = unitEnable, starter = unit){
            unit.value = it
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateItem(
    app: App
) {
    val nameField = remember {
        mutableStateOf(app.food.name)
    }

    val amount = remember {
        mutableStateOf(app.food.amount.toString())
    }

    val unit = remember {
        mutableStateOf(app.food.unit)
    }

    val momentSelector = remember {
        mutableStateOf(Moments.valueOf(Moments.entries[app.food.momentSelector].name))
    }


    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")
    val date: MutableState<LocalDate> = remember {
        mutableStateOf(LocalDate.parse(app.dateOperation, formatterSql))
    }



    Column(
        modifier = Modifier
            .background(color = Color.White)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (app.screen != Screen.House) {
            DropdownMenuSelection(
                list = app.dbManager.getAllFood(),
                starter = nameField
            ) {
                nameField.value = it
                if (nameField.value.isNotEmpty())
                    unit.value = app.dbManager.getUnitOf(nameField.value)!!
                else
                    unit.value = app.food.unit
            }
        } else {
            TextField(
                value = nameField.value,
                onValueChange = { nameField.value = it },
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
        }


        AmountField(amount = amount, amountInventory = if (app.screen == Screen.ShoppingCart) app.food.amountInventory else null, app.isNewFood, unit = unit)

        if (app.screen == Screen.Menu) {
            ChoiceMoment(momentSelector = momentSelector)
            Date(
                date = date,
                enableLeft = true,
                enableRight = true,
                modifierIcons = Modifier.size(25.dp),
                fontSizeText = 35
            )
        }


        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { app.navController.navigateUp() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            IconButton(onClick = {
                val updatedFood = app.food

                updatedFood.setUnit(unit.value)

                val exists = app.dbManager.itemExists(nameField.value)

                updatedFood.update(
                    newName = nameField.value,
                    newAmount = amount.value.toInt(),
                )

                if (app.screen == Screen.Menu) {

                    updatedFood.update(
                        newDate = date.value.format(formatterSql),
                        newMomentSelector = momentSelector.value.ordinal
                    )

                    updatedFood.setIdInventory(exists.first)

                    if (app.isNewFood.value)
                        app.dbManager.insertFood(updatedFood)
                    else
                        app.dbManager.updateFood(updatedFood)

                }

                if (app.screen == Screen.House) {
                    if (app.isNewFood.value && !exists.second)
                        app.dbManager.insertItemInventory(updatedFood)
                    else
                        app.dbManager.updateInventoryItem(updatedFood)
                }

                if (app.screen == Screen.ShoppingCart){
                    println("IDINVENTORY:"+app.food.idInventory)
                    app.dbManager.updateCart(app.food.idInventory, amount.value.toInt()+app.food.amountInventory)
                }

                app.isNewFood.value = false

                app.navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
            }
        }
    }
}