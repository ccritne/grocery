package com.example.grocery.screens.updateitem.ui.referenceSetup.moments

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grocery.items.Item
import com.example.grocery.items.MutableItem
import com.example.grocery.uielements.date.Date
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.utilities.getFormatterDateSql
import java.nio.channels.Selector
import java.util.Date

@Composable
fun UiMomentsDateReference(
    momentsMap: Map<Long, String>,
    date: MutableState<Date>,
    momentSelector: MutableLongState,
    onChangeDate: (Date) -> Unit
){

    Column(
        modifier = Modifier.fillMaxWidth(0.95f)
    ) {
        ChoiceMoment(
            momentsMap = momentsMap,
            momentState = momentSelector,
            onChange = {
                momentSelector.longValue = it
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
            onChangeDate(it)
        }
    }

}