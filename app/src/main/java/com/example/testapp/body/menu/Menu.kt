package com.example.testapp.body.menu

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.testapp.DbManager
import com.example.testapp.Food
import com.example.testapp.MainActivity
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
fun Menu(app: MainActivity, context: Context, dbManager: DbManager) {

    val formatterSql = DateTimeFormatter.ofPattern("y/MM/dd")
    val formattedDateSQL = app.date.format(formatterSql)

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

    Column(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround
    ) {


        Date(app, true, true)

        Column(
            modifier = Modifier
                .fillMaxHeight(0.92f)
                .fillMaxWidth(0.92f)
                .verticalScroll(ScrollState(0))
                .padding(top = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top,
        ) {

            if(momentFood.isNotEmpty()) {
                momentFood.forEach { moment ->
                    Moment(
                        app = app,
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

