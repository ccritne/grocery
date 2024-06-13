package com.example.testapp.body.menu

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
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
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
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
import com.example.testapp.DbManager
import com.example.testapp.Food
import java.time.LocalDate
import java.time.format.DateTimeFormatter

enum class Moments(){
    Breakfast,
    FirstSnack,
    Lunch,
    SecondSnack,
    Dinner,
    ThirdSnack
}

fun fromListFoodToMapBySelector(list: ArrayList<Food>) : MutableMap<Int, ArrayList<Food>>{

    val returnedMap : MutableMap<Int, ArrayList<Food>> = mutableMapOf()

    for (i in list){
        if(!returnedMap.containsKey(i.momentSelector))
            returnedMap[i.momentSelector] = arrayListOf()

        returnedMap[i.momentSelector]!!.add(i)
    }

    return returnedMap
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Menu(context: Context, dbManager: DbManager) {


    Column(
        modifier = Modifier
            .fillMaxHeight(0.92f)
//                            .background(color = Color.Green)
            .fillMaxWidth(0.92f),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        val formatterSql = DateTimeFormatter.ofPattern("y/MM/dd")
        val date = remember { mutableStateOf(LocalDate.now()) }
        val formattedDateSQL = date.value.format(formatterSql)

        Date(date, true, true)

        Column(
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .fillMaxWidth(0.92f)
                .verticalScroll(ScrollState(0))
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            var momentFood : Map<Int, ArrayList<Food>> = mapOf()

            val mapMomentSelector = mapOf(
                0 to "Breakfast",
                1 to "First snack",
                2 to "Lunch",
                3 to "Second snack",
                4 to "Dinner",
                5 to "Third snack"
            )

            dbManager.selectFromDay(formattedDateSQL) { listFood ->
                momentFood = fromListFoodToMapBySelector(listFood)
            }

            if(momentFood.isNotEmpty()) {
                momentFood.forEach { moment ->
                    Moment(
                        momentName = mapMomentSelector[moment.key].toString(),
                        ingredientsCollection = moment.value,
                        idMoment = moment.key,
                        dbManager = dbManager
                    )
                }
            }
            else
                Text(text = "No data found!")



        }
    }
}

