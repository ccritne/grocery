package com.example.testapp.body.menu

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Checkbox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.DbManager
import com.example.testapp.Food
import androidx.compose.ui.window.Dialog
import com.example.testapp.MainActivity

@Composable
fun ItemMenu(app: MainActivity, food: Food, dbManager: DbManager, dialogShow: MutableState<Boolean>, foodDialog: MutableState<String>, gramsDialog: MutableIntState, idDialog: MutableIntState){
    var checked by remember {
        mutableStateOf(food.eaten)
    }

    var isDeleting by remember {
        mutableStateOf(false)
    }


    if(isDeleting){
        Dialog(
            onDismissRequest = {},
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(0.95f).background(color = Color.White),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceAround
            ) {
                Text(text = "Deleting ${food.name}", modifier = Modifier.fillMaxWidth().padding(15.dp), fontSize = 35.sp, textAlign = TextAlign.Center)
                Text(text = "Are you sure?", modifier = Modifier.fillMaxWidth().padding(15.dp), fontSize = 25.sp, textAlign = TextAlign.Center)

                Row(modifier = Modifier.fillMaxWidth().padding(15.dp), horizontalArrangement = Arrangement.SpaceAround) {
                    TextButton(onClick = {
                        isDeleting = false
                    }) {
                        Text("No")
                    }
                    TextButton(onClick = {
                        dbManager.deleteFood(food.id)
                        app.recreate()
                        isDeleting = false
                    }) {
                        Text("Yes")
                    }
                }
            }
        }

    }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                dialogShow.value = true
                foodDialog.value = food.name
                gramsDialog.intValue = food.grams
                idDialog.intValue = food.id
            },
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = food.name,
            fontSize = 20.sp
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if(food.grams == 0) "1pz" else food.grams.toString() + "g",
                fontSize = 20.sp
            )
            Checkbox(checked = checked, onCheckedChange = {
                checked = it
                dbManager.updateEaten(food.id, it)
            })

            IconButton(onClick = {
                isDeleting = true
            }) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete food")
            }
        }
    }
}