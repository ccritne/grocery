package com.example.grocery.screens.updateitem.ui.referenceSetup.moments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.uielements.date.Date
import java.util.Date

@Composable
fun UiMomentsDateReference(
    momentsMap: Map<Long, String>,
    date: Date,
    momentSelector: Long,
    onChangeDate: (Long, Date) -> Unit
){

    var momentLongSelector by remember {
        mutableLongStateOf(momentSelector)
    }

    var dateSelector by remember {
        mutableStateOf(date)
    }

    Column(
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        ChoiceMoment(
            momentsMap = momentsMap,
            momentState = momentLongSelector,
            onChange = {
                momentLongSelector = it
                onChangeDate(it, date)
            }
        )

        Date(
            date = dateSelector,
            enableLeft = true,
            enableRight = true,
            modifier = Modifier.fillMaxWidth(),
            modifierIcons = Modifier.size(25.dp),
            fontSizeText = 35
        ) {
            dateSelector = it
            onChangeDate(momentSelector, it)
        }
    }

}