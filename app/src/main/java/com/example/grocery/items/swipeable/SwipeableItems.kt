package com.example.grocery.items.swipeable

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxState
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableItems(
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
            when (value) {
                SwipeToDismissBoxValue.StartToEnd -> {
                    if (!stayWhenStartEnd) {
                        isRemoved = true
                        return@rememberSwipeToDismissBoxState true
                    }
                }

                SwipeToDismissBoxValue.EndToStart -> {
                    if (!stayWhenEndStart) {
                        isRemoved = true
                        return@rememberSwipeToDismissBoxState true
                    }
                }

                SwipeToDismissBoxValue.Settled -> return@rememberSwipeToDismissBoxState false
            }
            changeState = !changeState
            false
        }
    )

    LaunchedEffect(key1 = changeState) {
        if (isRemoved) {
            scope.launch {
                delay(animationDuration.toLong())
            }
        }

        if (state.dismissDirection == SwipeToDismissBoxValue.StartToEnd)
            onStartEnd()
        if (state.dismissDirection == SwipeToDismissBoxValue.EndToStart)
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
        SwipeToDismissBoxValue.StartToEnd -> Color.Red
        SwipeToDismissBoxValue.EndToStart -> Color.Green
        else -> Color.Transparent
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