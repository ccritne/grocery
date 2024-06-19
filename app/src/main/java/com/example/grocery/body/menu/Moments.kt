package com.example.grocery.body.menu

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.body.Item
import com.example.grocery.utilities.Food


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Moment(
    app: App,
    momentName: String,
    foodOfMoments: List<Food> = listOf()
){

    val subMenuShow = remember { mutableStateOf(false) }

    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        Column(
            modifier = Modifier.fillMaxWidth().clickable(onClick = {
                subMenuShow.value = !subMenuShow.value
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

            if(subMenuShow.value){
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 10.dp, end = 10.dp),
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.SpaceAround
                ) {
                    foodOfMoments.forEach { food ->
                        Item(
                            app = app,
                            food = food
                        )

                    }
                }
            }


        }

    }
}