package com.example.testapp.body

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.testapp.DbManager
import com.example.testapp.Food
import com.example.testapp.MainActivity
import com.example.testapp.ShoppingCartItem
import java.time.format.DateTimeFormatter

enum class Units{
    Grams,
    Pieces,
    Kilograms,
    Liters,
    Milliliters
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun House(app: MainActivity, dbManager: DbManager){
    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")

    val formatterDesign = DateTimeFormatter.ofPattern("dd/MM")

    val formattedStartDateSQL: String = app.startDate.format(formatterSql)
    val formattedEndDateSQL: String = app.endDate.format(formatterSql)

    val formattedStartDateDesign: String = app.startDate.format(formatterDesign)
    val formattedEndDateDesign: String = app.endDate.format(formatterDesign)


    var foods : List<ShoppingCartItem> = listOf()

    dbManager.selectBoughtAggregate(formattedStartDateSQL, formattedEndDateSQL){
        foods = it
    }
    
    var isCameraAvailable: Boolean by remember{ 
        mutableStateOf(false)
    }


    if (isCameraAvailable){
        val foodAdded  = Food()

        var expanded by remember { mutableStateOf(false) }
        var selectedText by remember { mutableStateOf(Units.entries[0].name) }
        val items = Units.entries

        Dialog(onDismissRequest = { isCameraAvailable = false }) {
            Column(
                modifier = Modifier.fillMaxWidth().background(Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                OutlinedTextField(
                    shape = RectangleShape,
                    value = foodAdded.name,
                    maxLines = 1,
                    label = {
                        Text(
                            text = "Name",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center
                        )
                    },
                    textStyle = TextStyle(textAlign = TextAlign.Center),
                    onValueChange = {
                        foodAdded.name = it
                    },
                    modifier = Modifier.fillMaxWidth(0.8f)
                )
                Row(
                    modifier = Modifier.fillMaxHeight(0.1f).fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    OutlinedTextField(
                        shape = RectangleShape,
                        value = foodAdded.grams.toString(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                        label = {
                            Text(
                                text = "Grams",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = TextAlign.Center
                            )
                        },
                        textStyle = TextStyle(textAlign = TextAlign.Center),
                        onValueChange = {
                            foodAdded.grams = it.toInt()
                        },
                        modifier = Modifier.fillMaxWidth(0.5f)
                        )
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
                            items.forEach { item ->
                                DropdownMenuItem(
                                    text = {Text(text = item.name)},
                                    onClick = {
                                    selectedText = item.name
                                    expanded = false
                                })
                            }
                        }
                    }
                }
                Text(text = "Scan Barcode")
                Text(text = "Camera box", modifier = Modifier
                    .size(300.dp, 500.dp)
                    .background(color = Color.Black))
            }
        }
    }
    

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row (
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ){
                IconButton(onClick = {
                    app.startDate = app.startDate.minusDays(7)
                    app.endDate = app.endDate.minusDays(7)
                }) {
                    Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "backdate")
                }
                Text("$formattedStartDateDesign - $formattedEndDateDesign", fontSize = 25.sp)
                IconButton(onClick = {
                    app.startDate = app.startDate.plusDays(7)
                    app.endDate = app.endDate.plusDays(7)

                }) {
                    Icon(
                        imageVector = Icons.Default.ArrowForward,
                        contentDescription = "forwarddate"
                    )
                }
            }
            IconButton(onClick = { isCameraAvailable = true }) {
                Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
            }
        }


        if (foods.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth(0.95f)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly
            ) {
                foods.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.name, fontSize = 25.sp)

                        Text(
                            text = it.checkedTotal.toString() + " " + if (it.checkedTotal < 15) "pz" else "g",
                            fontSize = 25.sp,
                            modifier = Modifier
                        )

                    }
                }
            }
        } else {
            Text(text = "No items found!")
        }
    }
}