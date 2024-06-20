package com.example.grocery.body.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import com.example.grocery.body.ButtonAdd
import com.example.grocery.utilities.Item
import com.example.grocery.utilities.Moments
import com.example.grocery.utilities.Screen
import java.time.format.DateTimeFormatter


fun fromListItemToMapBySelector(list: MutableList<Item>) : MutableMap<Int, MutableList<Item>>{

    val returnedMap : MutableMap<Int, MutableList<Item>> = mutableMapOf()

    for (i in list){
        if(!returnedMap.containsKey(i.momentSelector))
            returnedMap[i.momentSelector] = mutableListOf()

        returnedMap[i.momentSelector]!!.add(i)
    }

    return returnedMap
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Plan(app: App) {

    app.screen = Screen.Plan
    val formatterSql: DateTimeFormatter = DateTimeFormatter.ofPattern("y/MM/dd")


    var momentItem : Map<Int, MutableList<Item>> = mapOf()

    app.dbManager.selectPlanForDay(app.dateOperation.value.format(formatterSql)) { listItem ->
        momentItem = fromListItemToMapBySelector(listItem)
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
                date = app.dateOperation,
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

                if (momentItem.isNotEmpty()) {
                    momentItem.forEach { moment ->
                        Moment(
                            app = app,
                            momentName = Moments.entries[moment.key].name,
                            itemOfMoments = moment.value
                        )
                    }
                } else
                    Text(text = "No data found!")


            }
        }
    }
}

