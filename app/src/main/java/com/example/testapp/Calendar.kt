package com.example.testapp

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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


fun jsonStringToEasyMap(string: String) : Map<String, Int> {
    /*
        Easy Json
        {
            "key1": value1,
            "key2": value2,
            .
            .
            .
            "keyN": valueN
        }

     */

    var passedQMarks = false
    var passedTwoPoints = false

    var keyName = ""
    var valueName = ""

    val returnMap : MutableMap<String, Int> = emptyMap<String, Int>().toMutableMap()

    for (i in string){
        when (i) {
            '\n' -> {}
            ' ' -> {}
            '{' -> {}
            '}' -> {}
            '"' -> passedQMarks = !passedQMarks
            ':' -> passedTwoPoints = true
            ',' -> {
                    if(passedQMarks)
                        keyName += i
                    else {
                        returnMap[keyName] = valueName.toInt()
                        keyName = ""
                        valueName = ""
                        passedTwoPoints = false
                    }
                }
            else -> {
                valueName += if (!passedQMarks || passedTwoPoints) i else ""
                keyName += if (passedQMarks && !passedTwoPoints) i else ""

                /***
                 * Quotation    TwoPoints   |   Result
                 * False            False   |    Value
                 * False            True    |    Value
                 * True             False   |    Key
                 * True             True    |    Value
                 ***/
            }
        }
    }

    if(keyName != "" && valueName != "")
        returnMap[keyName] = valueName.toInt()

    return returnMap
}

fun findClosureBracket(string: String, index: Int): Int{

    var openBracket = 1
    var closeBracket = 0

    var iterator = index

    while (openBracket > closeBracket && iterator < string.length){
        iterator++
        when (string[iterator]) {
            '{' -> openBracket++
            '}' -> closeBracket++
            else -> {}
        }
    }

    return iterator

}

fun jsonStringToComplicatedMap(string: String) : Map<String, Any> {
    var i = 0
    var deep = 0
    while( i < string.length){
        when (string[i]) {
            '{' -> {
                val endClosureBracket = findClosureBracket(string, i)
                var subString = string.substring(i, endClosureBracket)
                i += endClosureBracket
            }
        }
    }

    return mapOf("Ciao" to "Pippo")
}


@Composable
fun Calendar() {

    val dialogShow = remember {
        mutableStateOf(false)
    }

    var foodDialog = remember{ mutableStateOf("") }
    var gramsDialog  = remember{ mutableIntStateOf(0) }

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
        val todayStr = SimpleDateFormat("d MMM y", Locale.getDefault()).format(Date())
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically

        ) {
            IconButton(onClick = { /*TODO*/ }) {
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
                    text = todayStr,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.ExtraBold,
                    fontFamily = FontFamily.Monospace,
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
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

            val jsonEatMoments = """
                {
                    "Breakfast": {
                        "Milk":444,
                        "Biscuit":254
                    },
                    "1° Snack": {
                        "Apple": 804 
                    },
                    "Lunch":{
                        "Pasta":717,
                        "Tomato sauce":578,
                        "Salad":889,
                        "Carrots":610
                    },
                    "2° Snack":{
                        "Yogurt":580,
                        "Muesli":276
                    },
                    "Dinner":{
                        "Pepper":652,
                        "Oil":79,
                        "Mozzarella":492
                    },
                    "3° Snack":{
                        "Ice Cream":356,
                        "Cocoa":507,
                        "Smarties":695
                    }
                }
            """.trimIndent()



            val mapEatMoments = mapOf(
                "Breakfast" to mapOf("Milk" to 444, "Biscuits" to 254),
                "1° Snack" to mapOf("Apple" to 804),
                "Lunch" to mapOf("Pasta" to 717, "Tomato sauce" to 578, "Salad" to 889, "Carrots" to 610),
                "2° Snack" to mapOf("Yogurt" to 580, "Muesli" to 276),
                "Dinner" to mapOf("Pepper" to 652, "Oil" to 79, "Mozzarella" to 492),
                "3° Snack" to mapOf("Ice Cream" to 356, "Cocoa" to 507, "Smarties" to 695),
            )


            mapEatMoments.forEach { moment ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Column(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.SpaceAround
                    ) {
                        Text(
                            text = moment.key.uppercase(),
                            modifier = Modifier.padding(10.dp),
                            fontSize = 35.sp,
                            fontWeight = FontWeight.ExtraBold,
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(start = 10.dp, end = 10.dp),
                            horizontalAlignment = Alignment.Start,
                            verticalArrangement = Arrangement.SpaceAround
                        ) {
                            moment.value.forEach { food ->

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

                }
            }


        }
    }
}

