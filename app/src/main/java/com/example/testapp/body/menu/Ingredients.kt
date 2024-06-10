package com.example.testapp.body.menu

import android.database.sqlite.SQLiteDatabase
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.testapp.DbManager
import com.example.testapp.Food

@Composable
fun Dialog(foodDialog: String, gramsDialog: MutableIntState, dialogShow: MutableState<Boolean>, database: DbManager, id: Int){

    AlertDialog(
        onDismissRequest = { dialogShow.value = false },
        confirmButton = {
            IconButton(
                onClick = { dialogShow.value = false }
            ) {
                Icon(imageVector = Icons.Default.Check, contentDescription = "Okay")
            }
        },
        dismissButton = {
            IconButton(
                onClick = {
                    database.deleteFood(id)
                    dialogShow.value = false
                }
            ) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = "Delete")
            }
        },
        title = { Text(text = foodDialog, color = Color.Black) },

        text = { TextField(value = gramsDialog.intValue.toString(), onValueChange = {
            gramsDialog.intValue = it.toInt()
        }) },
    )
}

@Composable
fun Ingredients(
    ingredients: List<Food>,
    db : DbManager
){

    val foodDialog = remember {
        mutableStateOf("")
    }

    val gramsDialog = remember {
        mutableIntStateOf(-1)
    }

    val idDialog = remember {
        mutableIntStateOf(-1)
    }

    val dialogShow : MutableState<Boolean> = remember {
        mutableStateOf(false)
    }

    if (dialogShow.value)
        Dialog(
            foodDialog = foodDialog.value,
            gramsDialog = gramsDialog,
            dialogShow = dialogShow,
            database = db,
            id = idDialog.intValue
        )


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 10.dp, end = 10.dp),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.SpaceAround
    ) {
        ingredients.forEach { food ->

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
                    fontSize = 25.sp
                )

                Text(
                    text = food.grams.toString() + "g",
                    fontSize = 25.sp
                )
            }

        }
    }
}
