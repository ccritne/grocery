package com.example.testapp.body.menu

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.DbManager
import com.example.testapp.Food

@Composable
fun Moment(
    momentName : String,
    ingredientsCollection: List<Food>,
    idMoment : Int,
    dbManager: DbManager
){

    val subMenuShow = remember { mutableStateOf(false) }
    val idSelectedShow = remember { mutableIntStateOf(-1) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().clickable(onClick = {
                subMenuShow.value = !subMenuShow.value
                idSelectedShow.intValue = idMoment
            }),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {

            Text(
                text = momentName,
                modifier = Modifier.padding(10.dp),
                fontSize = 35.sp,
                fontWeight = FontWeight.ExtraBold,
            )

            if(subMenuShow.value && idSelectedShow.intValue == idMoment)
                Ingredients(ingredients = ingredientsCollection, db = dbManager)


        }

    }
}