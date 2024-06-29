package com.example.grocery.body

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.App
import com.example.grocery.utilities.Screen

@Composable
fun ChoiceMoment(
    app: App,
    momentState: MutableLongState,
    onChange: (Long) -> Unit
){

    val momentCheckedStates = remember(app.momentsMap) {
        app.momentsMap.value.map { item -> mutableLongStateOf(item.key) }
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxWidth()
            .padding(15.dp),
    ) {
        items(app.momentsMap.value.size) { index ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {

                RadioButton(
                    selected = momentState.longValue == momentCheckedStates[index].longValue,
                    onClick = { onChange(momentCheckedStates[index].longValue) })

                app.momentsMap.value[momentCheckedStates[index].longValue]?.let { Text(text = it) }
            }
        }
}
}

@Composable
fun ButtonAdd(
    app: App
){
    FloatingActionButton(
        shape = CircleShape,
        onClick = {
            app.isNewItem.value = true

            if (app.itemsMap.value.isNotEmpty())
                app.setItem(app.itemsMap.value.entries.first())
            else
                app.setItem(app.voidMapEntry)

            app.navController.navigate(Screen.UpdateItem.name)
        }) {
        Icon(imageVector = Icons.Default.Add, contentDescription = "Add item")
    }
}


