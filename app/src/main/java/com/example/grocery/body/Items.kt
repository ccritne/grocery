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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.Checkbox
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.grocery.App
import com.example.grocery.utilities.Moments
import com.example.grocery.utilities.Screen
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import com.example.grocery.utilities.Item

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
            app.isNewItem.value = true
            app.setItem(Item())
            app.navController.navigate(Screen.UpdateItem.name)
        }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ItemUI(
    app: App,
    item: Item
){

    var checked by remember {
        mutableStateOf(item.checked)
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
            message = """Do you want to delete ${app.item.name} from ${
                if (currentCaller == Screen.Plan.name) Moments.entries[app.item.momentSelector].name.lowercase() + " of " + LocalDate.parse(
                    app.item.date,
                    formatterSql
                ).format(formatterDesign) else "inventory"
            }?"""
        ) {
            if (it)
                app.dbManager.deleteItem(app.item.id, currentCaller == Screen.Plan.name)
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
                        app.isNewItem.value = false
                        app.setItem(item)
                        isDeleting = true
                    },
                    onTap = {
                        app.isNewItem.value = false
                        app.setItem(item)
                        app.navController.navigate(Screen.UpdateItem.name)
                    }
                )
            }
            .background(
                color = if (screen == Screen.ShoppingCart.name && item.amountInventory >= item.amount)
                    Color.Green
                else
                    Color.Transparent
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = item.name,
            fontSize = 30.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = (if (screen == Screen.ShoppingCart.name)
                    item.amountInventory.toString()+"/"
                else "")
                        +
                        item.amount.toString()
                        +
                        item.unit.symbol,
                fontSize = 30.sp
            )
            if (screen == Screen.Plan.name)
                Checkbox(
                    checked = checked,
                    onCheckedChange = {
                        checked = it
                        app.dbManager.updatePlanChecked(item.id, it)
                    }
                )
        }
    }
}