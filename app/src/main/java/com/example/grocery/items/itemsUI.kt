package com.example.grocery.items

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.grocery.App
import com.example.grocery.screens.Screen

@Composable
fun ItemUI(
    app: App,
    item: Map.Entry<Long, Item>,
    checkedState : Boolean = false
){

    val itemObject = item.value


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                app.setItem(item = item)
                app.isNewItem.value = false
                app.navController.navigate(Screen.UpdateItem.name)
            }
            .background(
                color = if (app.screen == Screen.ShoppingCart && itemObject.amountInventory >= itemObject.amount)
                    Color.Green
                else
                    Color.Transparent
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (checkedState)
                Icon(
                    imageVector = Icons.Default.Check, contentDescription = "Check",
                    modifier = Modifier.padding(start = 15.dp)
                )

            Text(
                text = itemObject.name,
                fontSize = 30.sp,
                modifier = Modifier.padding(start = 15.dp)
            )
        }

        if (app.screen != Screen.Items) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = (if (app.screen == Screen.ShoppingCart)
                        itemObject.amountInventory.toString() + "/"
                    else "")
                            +
                            itemObject.amount.toString()
                            +
                            app.unitsMap.value[itemObject.idUnit]?.second,
                    fontSize = 30.sp,
                    modifier = Modifier.padding(end = 15.dp)
                )

            }
        }

    }


}