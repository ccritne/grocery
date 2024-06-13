package com.example.testapp.body

import android.app.Dialog
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.DbManager
import com.example.testapp.Food
import com.example.testapp.ShoppingCartItem
import com.example.testapp.body.menu.Date
import com.example.testapp.body.menu.DoubleDate
import java.text.SimpleDateFormat
import java.time.DayOfWeek
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.temporal.TemporalAdjuster
import java.time.temporal.TemporalAdjusters
import java.util.Date
import java.util.Locale
import kotlin.random.Random
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties

fun fromGoogleToApp(dbManager: DbManager){
    val string = "SD2024/06/10;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta125g;Pumpkin200g;Banana;SM3;SM4;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/11;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta50g;Peas80g;JobLunch;Apple;SM3;SM4;Bread100g;Chickpeas125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/12;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;BroadBeans100g;Spinach400g;Bread150g;Apple;SM3;SM4;PizzaTeglia300g;SD2024/06/13;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta50g;Beans125g;JobLunch;Banana;SM3;SM4;Potatoes500g;VegetablePeppers200g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/14;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta80g;Soia130g;Tomatoes160g;Zucchini100g;Carrots100g;Banana;SM3;SM4;Flatbreads250g;PestoSauce95g;Mozzarella125g;Tomatoes250g;Carrots200g;Fennel100g;SD2024/06/15;SM0;Bread100g;Philadelphia60g;YogurtFruit125g;Blueberries25g;SM1;SM2;Pasta125g;Eggs150g;Parmisan25g;Apple;SM3;SM4;Pizza250g;Mozzarella125g;SD2024/06/16;SM0;Bread100g;Eggs60g;YogurtFruit125g;Strawberry25g;SM1;SM2;Pasta125g;TomatoSauce80g;JobLunch;Banana;SM3;SM4;Bread250g;Chickpeas125g;Zucchini100g;Iceberg/Batavia50g;Tomatoes100g"
    val filterByDay = string.split("SD").toMutableList()

    val foods: MutableList<Food> = mutableListOf()

    filterByDay.forEach{ listDay->
        if (listDay.isNotEmpty()){
            val date = listDay.subSequence(0 .. 9).toString()
            val moments = listDay.subSequence(11, listDay.length).split("SM")


            moments.forEach{ moment ->
                if(moment.isNotEmpty()){
                    val ingredients = moment.split(";")
                    val momentSelector = ingredients[0].toInt()
                    val ingredientsPop= ingredients.subList(1, ingredients.size-1)

                    ingredientsPop.forEach{
                            ingredient ->

                        if (ingredient.isNotEmpty()) {
                            var changeIngredient = ingredient

                            if(ingredient.last() == 'g' && ingredient[ingredient.length-2].isDigit())
                                changeIngredient = ingredient.subSequence(0, ingredient.length-1).toString()

                            val name = changeIngredient.filter { !it.isDigit() }
                            val grams = changeIngredient.filter { it.isDigit() }

                            val gramsInt = if(grams.isNotEmpty()) grams.toInt() else 0

                            foods.add(
                                Food(
                                    date=date,
                                    momentSelector=momentSelector,
                                    name=name,
                                    grams=gramsInt,
                                    price = 0.0f,
                                    idParent = -1,
                                )
                            )
                        }

                    }

                }

            }

        }


    }

    foods.forEach {
            food ->

        dbManager.insertFood(
            price = food.price,
            date = food.date,
            name = food.name,
            grams = food.grams,
            momentSelector = food.momentSelector
        )
    }
}

@Composable
fun DialogShopping(
    defaultValue: Int,
    actionOnClosure: (Int, Boolean?) -> Unit
){

    var textValue by remember {
        mutableStateOf(defaultValue.toString())
    }

    Dialog(
        onDismissRequest = { actionOnClosure(0, null) },
    ){
    Column(
        modifier = Modifier.background(Color.White),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {

        TextField(
            label = { Text(text = "Change the value", fontSize = 15.sp) },
            modifier = Modifier.padding(15.dp),
            textStyle = TextStyle(textAlign = TextAlign.Center),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
            value = textValue, onValueChange = {
                textValue = if(it.isNotEmpty() && it.toInt() >= 0)
                    it.toInt().toString()
                else
                    defaultValue.toString()
        } )

        Row(
            horizontalArrangement = Arrangement.SpaceAround
        ) {
            IconButton(onClick = { actionOnClosure(0, false) }) {
                Icon(imageVector = Icons.Default.Close, contentDescription = "Close dialog")
            }
            IconButton(onClick = {
                actionOnClosure(textValue.toInt(), true)
            }) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Open dialog")
            }
        }
    }
    }

}


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ShoppingCart(context: Context, dbManager: DbManager){


    val formatterDesign = DateTimeFormatter.ofPattern("dd/MM")

    val formatterSql = DateTimeFormatter.ofPattern("y/MM/dd")

    var startDate by remember { mutableStateOf(LocalDate.now().with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))) }
    val endDate = startDate.plusDays(6)

    val formattedStartDateDesign = startDate.format(formatterDesign)

    val formattedEndDateDesign = endDate.format(formatterDesign)


    val formattedStartDateSQL = startDate.format(formatterSql)
    val formattedEndDateSQL = endDate.format(formatterSql)

    var foods : List<ShoppingCartItem> = listOf()


    dbManager.selectUniqueAggregate(formattedStartDateSQL, formattedEndDateSQL){
        foods = it
    }

    var isDialogVisible by remember {
        mutableStateOf(false)
    }

    var maxValue : Int by remember {
        mutableIntStateOf(0)
    }

    var defaultValue : Int by remember {
        mutableIntStateOf(0)
    }

    var name by remember {
        mutableStateOf("")
    }

    if(isDialogVisible) {
        DialogShopping(
            defaultValue
        ) { value, state ->
            if (state == true) {
                dbManager.updateCart(
                    name,
                    formattedStartDateSQL,
                    formattedEndDateSQL,
                    value >= maxValue,
                    value
                )
                dbManager.selectUniqueAggregate(formattedStartDateSQL, formattedEndDateSQL){
                    foods = it
                }
            }
            isDialogVisible = false
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
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(onClick = {
                startDate = startDate.minusDays(7)
            }) {
                Icon(imageVector = Icons.Default.ArrowBack, contentDescription = "backdate")
            }
            Text("$formattedStartDateDesign - $formattedEndDateDesign", fontSize = 25.sp)
            IconButton(onClick = {
                startDate = startDate.plusDays(7)

            }) {
                Icon(imageVector = Icons.Default.ArrowForward, contentDescription = "forwarddate")
            }
        }


        if (foods.isNotEmpty()) {

            Column(
                modifier = Modifier
                    .fillMaxHeight(1f)
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.SpaceAround
            ) {
                foods.forEach {
                    Row(
                        modifier = Modifier.fillMaxWidth(0.95f),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = it.name, fontSize = 25.sp)

                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = it.checkedTotal.toString() + "/" + it.total.toString() + " " + if (it.total < 15) "pz" else "g",
                                fontSize = 25.sp,
                                color = if(it.checkedTotal >= it.total) Color.Green else Color.Black,
                                modifier = Modifier.padding(15.dp)
                            )
                            IconButton(
                                onClick = {
                                    name = it.name
                                    maxValue = it.total
                                    defaultValue = it.checkedTotal
                                    isDialogVisible = true
                                }
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "addChecked"
                                )
                            }
                        }



                    }
                }
            }
        } else {
            Text(text = "No shopping cart!")
        }
    }

}