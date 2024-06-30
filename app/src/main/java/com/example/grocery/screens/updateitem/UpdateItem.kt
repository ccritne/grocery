package com.example.grocery.screens.updateitem

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.grocery.items.Item
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.utilities.getFormatterDateSql
import java.util.Date

class UpdateItem(
    val forSetup: Boolean,
    val forShoppingCart: Boolean,
    val amountInventory: Int,
    val itemsMap: Map<Long, Item>,
    val unitsMap: Map<Long, Pair<String, String>>,
    val areThereMomentsDate: Boolean,
    val momentsMap: Map<Long, String>,
    val isNewItem: Boolean
) {
    var id : Long by mutableLongStateOf(-1)
        private set

    var idItem : Long by mutableLongStateOf(-1)
        private set

    var name : String by mutableStateOf(
        if(!isNewItem || !forSetup)
            itemsMap.entries.first().value.name
        else
            ""

    )
        private set

    var amount : Int by mutableIntStateOf(0)
        private set

    var idUnit by mutableLongStateOf(-1)
        private set

    var idMoment by mutableLongStateOf(-1)
        private set

    var date : Date by mutableStateOf(Date())
        private set

    fun changeNameFromDropDown(
        idItem: Long
    ){
        val item = itemsMap[idItem]!!

        this.idItem = idItem
        this.name = item.name
        this.idUnit = item.idUnit
    }

    fun setValues(
        id: Long? = null,
        idItem: Long? = null,
        name : String? = null,
        amount: Int? = null,
        idUnit: Long? = null,
        idMoment: Long? = null,
        date: Date? = null
    ){
        if(id != null)
            this.id = id

        if(idItem != null)
            this.idItem = idItem

        if(name != null) {
            this.name = name
        }

        if(amount != null)
            this.amount = amount

        if(idUnit != null)
            this.idUnit = idUnit

        if(idMoment != null)
            this.idMoment = idMoment

        if(date != null)
            this.date = date
    }

    fun toItem(idPlace: Long) : Item{
        val item = Item()

        item.update(
            id = id,
            amount = amount,
            idUnit = idUnit,
            idPlace = idPlace,
            idItem = idItem
        )

        if (areThereMomentsDate)
            item.update(
                idMoment = idMoment,
                date = getFormatterDateSql().format(date)
            )

        if(!isNewItem || !forSetup)
            item.update(name = itemsMap[idItem]!!.name)
        else
            item.update(name = name)



        return item
    }



}