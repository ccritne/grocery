package com.example.grocery.screens.updateitem.ui.referenceSetup.moments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.screens.updateitem.UpdateItem
import com.example.grocery.uielements.date.Date
import java.util.Date

@Composable
fun UiMomentsDateReference(
    updateItem: UpdateItem,
    onChangeDate: (Date) -> Unit
){
    val momentSelector = remember {
        mutableLongStateOf(updateItem.moment.first)
    }

    val date = remember {
        mutableStateOf(updateItem.date)
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        ChoiceMoment(
            momentsMap = updateItem.momentsMap,
            momentState = momentSelector,
            onChange = {
                momentSelector.longValue = it
                updateItem.setValues(idMoment = it)
            }
        )

        Date(
            date = date,
            enableLeft = true,
            enableRight = true,
            modifier = Modifier.fillMaxWidth(),
            modifierIcons = Modifier.size(25.dp),
            fontSizeText = 35
        ) {
            date.value = it
            updateItem.setValues(date = it)
            onChangeDate(it)
        }
    }

}