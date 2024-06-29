package com.example.grocery.body

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Checkbox
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.grocery.App
import com.example.grocery.database.deleteItem
import com.example.grocery.database.updatePlanChecked
import com.example.grocery.utilities.Item
import com.example.grocery.utilities.Screen
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue.EndToStart
import androidx.compose.material3.SwipeToDismissBoxValue.Settled
import androidx.compose.material3.SwipeToDismissBoxValue.StartToEnd
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import androidx.compose.runtime.LaunchedEffect

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



@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteContainer(
    stayWhenStartEnd: Boolean,
    onStartEnd: () -> Unit,
    stayWhenEndStart: Boolean,
    onEndStart: () -> Unit,
    animationDuration: Int = 500,
    content: @Composable () -> Unit
) {
    var isRemoved by remember {
        mutableStateOf(false)
    }

    var changeState by remember {
        mutableStateOf(false)
    }

    val scope = rememberCoroutineScope()

    val state = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            when (value){
                StartToEnd -> {
                    if (!stayWhenStartEnd) {
                        isRemoved = true
                        return@rememberSwipeToDismissBoxState true
                    }
                }

                EndToStart -> {
                    if (!stayWhenEndStart) {
                        isRemoved = true
                        return@rememberSwipeToDismissBoxState true
                    }
                }
                Settled -> return@rememberSwipeToDismissBoxState false
            }
            changeState = !changeState
            false
        }
    )

    LaunchedEffect(key1 = changeState) {
        if(isRemoved) {
            scope.launch {
                delay(animationDuration.toLong())
            }
        }

        if (state.dismissDirection == StartToEnd)
            onStartEnd()
        if (state.dismissDirection == EndToStart)
            onEndStart()
    }

    AnimatedVisibility(
        visible = !isRemoved,
        enter = fadeIn(),
        exit = shrinkVertically(
            animationSpec = tween(durationMillis = animationDuration),
            shrinkTowards = Alignment.Top
        ) + fadeOut()
    ) {
        SwipeToDismissBox(
            state = state,
            backgroundContent = {
                DeleteBackground(swipeDismissState = state)
            },
            content = { content() }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeleteBackground(
    swipeDismissState: SwipeToDismissBoxState
) {
    val color = when (swipeDismissState.dismissDirection){
        StartToEnd -> Color.Red
        EndToStart -> Color.Green
        else -> Color.White
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color)
            .padding(16.dp),
        contentAlignment = Alignment.CenterEnd
    ) {
        Icon(
            imageVector = Icons.Default.Delete,
            contentDescription = null,
            tint = Color.White
        )
        Spacer(modifier = Modifier.fillMaxWidth())
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color.White
        )

    }
}


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
                    Color.White
            ),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if(checkedState)
                Icon(imageVector = Icons.Default.Check, contentDescription = "Check",
                        modifier = Modifier.padding(start = 15.dp))

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
                        itemObject.amountInventory.toString()+"/"
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