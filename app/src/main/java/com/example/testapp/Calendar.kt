package com.example.testapp

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Calendar() {

    val dialogShow = remember {
        mutableStateOf(false)
    }

    val foodDialog = remember{ mutableStateOf("") }
    val gramsDialog  = remember{ mutableIntStateOf(0) }

    @Composable
    fun Dialog(){
        if(dialogShow.value)
            AlertDialog(
                onDismissRequest = { dialogShow.value = false },
                confirmButton = {
                    IconButton(
                        onClick = { dialogShow.value = false }) {
                        Icon(imageVector = Icons.Default.Check, contentDescription = "Okay")
                    }
                },
                title = { Text(text = foodDialog.value, color = Color.Black) },
                text = { TextField(value = gramsDialog.intValue.toString(), onValueChange = {/*TODO*/ }) },
            )
    }
    Dialog()

    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
//                            .background(color = Color.Green)
            .fillMaxWidth(0.92f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        val formatter = DateTimeFormatter.ofPattern("dd MMM y")
        val date = remember { mutableStateOf(LocalDate.now())}
        val formattedDate = date.value.format(formatter)

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(onClick = { date.value = date.value.minusDays(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "backDate",
                    modifier = Modifier.size(25.dp)
                )
            }
            TextButton(
                onClick = { /*TODO*/ },
                colors = ButtonDefaults.textButtonColors(contentColor = Color.Black)
            ) {
                Text(
                    text = formattedDate,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                )
            }
            IconButton(onClick = { date.value = date.value.plusDays(1) }) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "forwardDate",
                    modifier = Modifier.size(25.dp)
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight(0.92f)
//                                .background(color = Color.Red)
                .fillMaxWidth(0.92f)
                .verticalScroll(ScrollState(0)),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround,
        ) {

            val mapEatMoments = mapOf(
                "Breakfast" to mapOf("Milk" to 444, "Biscuits" to 254),
                "1° Snack" to mapOf("Apple" to 804),
                "Lunch" to mapOf("Pasta" to 717, "Tomato sauce" to 578, "Salad" to 889, "Carrots" to 610),
                "2° Snack" to mapOf("Yogurt" to 580, "Muesli" to 276),
                "Dinner" to mapOf("Pepper" to 652, "Oil" to 79, "Mozzarella" to 492),
                "3° Snack" to mapOf("Ice Cream" to 356, "Cocoa" to 507, "Smarties" to 695),
            )

            // Breakfast()
            // FirstSnack()
            // Lunch()
            // SecondSnack()
            // Dinner
            // ThirdSnack()

            val subMenuShow = remember { mutableStateOf(false) }
            val listIngredients = remember { mutableStateOf(mapOf<String, Int>()) }
            val idSelectedShow = remember { mutableIntStateOf(-1) }

            @Composable
            fun Ingredients(moment: Map<String, Int>){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    moment.forEach { food ->

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    dialogShow.value = true
                                    foodDialog.value = food.key
                                    gramsDialog.intValue = food.value
                                },
                            horizontalArrangement = Arrangement.SpaceBetween,
                        ) {
                            Text(text = food.key, fontSize = 25.sp)
                            Text(
                                text = food.value.toString() + "g",
                                fontSize = 25.sp
                            )
                        }

                    }
                }
            }

            @Composable
            fun Moment(momentName: String, ingredients: Map<String, Int>, idSelected : Int){
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            subMenuShow.value = !subMenuShow.value
                            listIngredients.value = ingredients
                            idSelectedShow.intValue = idSelected
                        },
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = momentName.uppercase(),
                            modifier = Modifier.padding(10.dp),
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )

                        if(subMenuShow.value && idSelectedShow.intValue == idSelected)
                            Ingredients(moment = ingredients)


                    }

                }
            }


            var iterator = 0
            mapEatMoments.forEach { moment ->
                Moment(momentName = moment.key, ingredients = moment.value, idSelected = iterator)
                iterator++
            }



        }
    }
}

