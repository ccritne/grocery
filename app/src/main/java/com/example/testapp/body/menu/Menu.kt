package com.example.testapp.body.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.FloatingActionButtonElevation
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import com.example.testapp.App
import com.example.testapp.body.ButtonAdd
import com.example.testapp.utilities.Food
import com.example.testapp.utilities.Screen
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
fun Menu(app: App) {
    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")

    val date: MutableState<LocalDate> = remember {
        mutableStateOf(LocalDate.now())
    }

    val formattedDateSQL = date.value.format(formatterSql)

    app.dateOperation = formattedDateSQL

    var momentFood : Map<Int, ArrayList<Food>> = mapOf()

    app.dbManager.selectMenuForDay(formattedDateSQL) { listFood ->
        momentFood = fromListFoodToMapBySelector(listFood)
    }

    Scaffold(
        floatingActionButtonPosition = FabPosition.Center,
        floatingActionButton = { ButtonAdd(app = app) }
    ) {
        Column(
            modifier = Modifier
                .padding(it)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Date(
                date = date,
                enableLeft = true,
                enableRight = true,
                modifierIcons = Modifier.size(25.dp),
                fontSizeText = 35
            )

            Column(
                modifier = Modifier
                    .fillMaxHeight(0.92f)
                    .fillMaxWidth(0.92f)
                    .verticalScroll(ScrollState(0))
                    .padding(top = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Top,
            ) {

                if (momentFood.isNotEmpty()) {
                    momentFood.forEach { moment ->
                        Moment(
                            app = app,
                            momentName = Moments.entries[moment.key].name,
                            foodOfMoments = moment.value
                        )
                    }
                } else
                    Text(text = "No data found!")


            }
        }
    }
}

