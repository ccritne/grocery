package com.example.grocery.body

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.grocery.App
import com.example.testapp.body.menu.Date
import com.example.testapp.body.menu.Moments
import com.example.testapp.utilities.Food
import com.example.testapp.utilities.Screen
import com.example.testapp.utilities.Units
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun AmountField(amount: MutableState<String>, unit: MutableState<Units>){

    var amountField by amount

    Row(
        modifier = Modifier
            .fillMaxHeight(0.1f)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        OutlinedTextField(
            shape = RectangleShape,
            value = amountField,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            placeholder = {
                Text(
                    text = "Amount",
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            },
            textStyle = TextStyle(textAlign = TextAlign.Center),
            onValueChange = {
                amountField = it
            },
            modifier = Modifier.fillMaxWidth(0.5f)
        )
        DropdownMenuSelection(list = Units.entries.map{it.symbol}, starter = unit.value.symbol)
    }
}

@Composable
fun Moment(
    momentSelector: MutableState<Moments>,
    moment: Moments
){
    Row(
        verticalAlignment = Alignment.CenterVertically
    ) {
        RadioButton(
            selected = momentSelector.value == moment,
            onClick = { momentSelector.value = moment })

        Text(text = moment.name)
    }
}

@Composable
fun ChoiceMoment(
    momentSelector: MutableState<Moments>
){

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        Moments.entries.chunked(1).forEach{ moments ->
            Row (
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                moments.forEach {
                    Moment(momentSelector = momentSelector, moment = it)
                }
            }
        }
    }
}

@Composable
fun DropdownMenuSelection(
    list: List<String>,
    starter: String
){
    var expanded by remember { mutableStateOf(false) }
    var selectedText by remember {
        mutableStateOf(starter)
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.5f)
    ) {
        IconButton(
            modifier = Modifier.fillMaxWidth(),
            onClick =  { expanded = true }
        ){
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = selectedText)
                Icon(
                    imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.ArrowDropDown,
                    contentDescription = null
                )
            }
        }
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            list.forEach { item ->
                DropdownMenuItem(
                    text = {
                        Text(
                            text = item,
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    onClick = {
                        selectedText = item
                        expanded = false
                    }
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun UpdateItem(
    app: App
){
    var nameField by remember {
        mutableStateOf(app.food.name)
    }

    val amount = remember {
        mutableStateOf(app.food.amount.toString())
    }

    val unit = remember {
        mutableStateOf(Units.valueOf(app.food.unit.name))
    }

    val momentSelector = remember {
        mutableStateOf(Moments.valueOf(Moments.entries[app.food.momentSelector].name))
    }

    val caller = app.getPreviousRoute()

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

        if (caller != null && caller == Screen.Menu)
            DropdownMenuSelection(list = app.dbManager.getAllFood(), starter = app.food.name)
        else{
            TextField(
                value = nameField,
                onValueChange = {nameField = it},
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(textAlign = TextAlign.Center)
            )
        }

        AmountField(amount = amount, unit = unit)

        if (caller == Screen.Menu) {
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
        ){
            IconButton(onClick = { app.navController.navigateUp() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
            }
            IconButton(onClick = {
                val updatedFood = app.food

                updatedFood.setUnit(unit.value)

                val exists = app.dbManager.itemExists(nameField)

                updatedFood.update(
                    newName = nameField,
                    newAmount = amount.value.toInt(),
                )

                if (caller == Screen.Menu) {

                    updatedFood.update(
                        newDate = date.value.format(formatterSql),
                        newMomentSelector = momentSelector.value.ordinal
                    )

                    if(app.isNewFood == true) {

                        if(exists.second){
                            updatedFood.setIdInventory(exists.first)
                            app.dbManager.insertFood(updatedFood)
                        }else{
                            app.dbManager.insertItemInventory(updatedFood)
                            updatedFood.setIdInventory(app.dbManager.itemExists(nameField).first)
                            app.dbManager.insertFood(updatedFood)
                        }
                    }else{
                        app.dbManager.updateFood(updatedFood)
                    }
                }

                if (caller == Screen.House){
                    if(app.isNewFood == true && !exists.second)
                        app.dbManager.insertItemInventory(updatedFood)
                    else
                        app.dbManager.updateInventoryItem(updatedFood)
                }

                app.navController.navigateUp()
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Add item")
            }
        }
    }

}

@Composable
fun DeleteItem(
    title: String,
    message: String,
    onDelete: (Boolean) -> Unit = {}
){
    Dialog(
        onDismissRequest = { onDelete(false) },
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .background(color = Color.White),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(text = title, modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), fontSize = 35.sp, textAlign = TextAlign.Center)
            Text(text = message, modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), fontSize = 25.sp, textAlign = TextAlign.Center)

            Row(modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp), horizontalArrangement = Arrangement.SpaceAround) {
                TextButton(onClick = {
                    onDelete(false)
                }) {
                    Text("No")
                }
                TextButton(onClick = {
                    onDelete(true)
                }) {
                    Text("Yes")
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ButtonAdd(
    app: App
){
    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")
            app.dateOperation = LocalDate.now().format(formatterSql)
            app.isNewFood = true
            app.setFood(Food())
            app.navController.navigate(Screen.UpdateItem.name)
        }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Item(
    app: App,
    food: Food
){

    var checked by remember {
        mutableStateOf(food.eaten)
    }

    val screen = app.navController.currentDestination?.route

    var isDeleting by remember {
        mutableStateOf(false)
    }

    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")
    val formatterDesign: DateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM")

    if(isDeleting) {
        val currentCaller = app.navController.currentDestination?.route
        DeleteItem(
            title = "Deleting item",
            message = """Do you want to delete ${app.food.name} from ${
                if (currentCaller == Screen.Menu.name) Moments.entries[app.food.momentSelector].name.lowercase() + " of " + LocalDate.parse(
                    app.food.date,
                    formatterSql
                ).format(formatterDesign) else "inventory"
            }?"""
        ) {
            if (it)
                app.dbManager.deleteFood(app.food.id, currentCaller == Screen.Menu.name)
            isDeleting = false
        }
    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.1f)
            .clickable {

            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = {
                        app.isNewFood = false
                        app.setFood(food)
                        isDeleting = true
                    },
                    onTap = {
                        app.isNewFood = false
                        app.setFood(food)
                        app.navController.navigate(Screen.UpdateItem.name)
                    }
                )
            }
            .background(
                color = if (screen == Screen.ShoppingCart.name && food.amountInventory >= food.amount)
                    Color.Green
                else
                    Color.Transparent
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = food.name,
            fontSize = 30.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = (if (screen == Screen.ShoppingCart.name)
                    food.amountInventory.toString()+"/"
                else "")
                        +
                        food.amount.toString()
                        +
                        food.unit.symbol,
                fontSize = 30.sp
            )
            if (screen == Screen.Menu.name)
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        app.dbManager.updateEaten(food.id, it)
                    }
                )
        }
    }
}