package com.example.grocery.screens.updateitem.ui

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.grocery.items.Item
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
    val isNewItem: Boolean,
    val idItem: Long
) {
    var id : Long by mutableLongStateOf(-1)
        private set

    var name : String by mutableStateOf("")
        private set

    var amount : Int by mutableIntStateOf(0)
        private set

    // Units never empty
    // TODO BETTER
    var unit by mutableStateOf(Pair(unitsMap.entries.first().toPair().first, unitsMap.entries.first().toPair().second.second))
        private set

    // Moments never empty
    var moment by mutableStateOf(momentsMap.entries.first().toPair())
        private set

    var date : Date by mutableStateOf(Date())
        private set

    fun changeNameFromDropDown(
        idItem: Long
    ){
        val item = itemsMap[idItem]!!

        this.name = item.name
        this.unit = Pair(item.idUnit, unitsMap[item.idUnit]!!.second)
    }

    fun setDefaultValues(
        idItem: Long
    ){
        val item = itemsMap[idItem]!!

        this.id = item.id
        this.name = item.name
        this.unit = Pair(item.idUnit, unitsMap[item.idUnit]!!.second)
        if(areThereMomentsDate) {
            this.moment = Pair(item.idMoment, momentsMap[item.idMoment]!!)
            val formatter = getFormatterDateSql()
            this.date = formatter.parse(item.date)!!
        }
    }

    fun setValues(
        id: Long? = null,
        name : String? = null,
        amount: Int? = null,
        idUnit: Long? = null,
        idMoment: Long? = null,
        date: Date? = null,
        isNewItem: Boolean? = null
    ){
        if(id != null)
            this.id = id

        if(name != null) {
            this.name = name
        }

        if(amount != null)
            this.amount = amount

        if(idUnit != null)
            this.unit = Pair(idUnit, unitsMap[idUnit]!!.second)

        if(idMoment != null && areThereMomentsDate)
            this.moment = Pair(idMoment, momentsMap[idMoment]!!)

        if(date != null && areThereMomentsDate)
            this.date = date
    }

    fun reset(){
        this.id = -1
        this.name = ""
        this.amount = 0
        this.moment = momentsMap.entries.first().toPair()
        this.unit = Pair(unitsMap.entries.first().toPair().first, unitsMap.entries.first().toPair().second.second)
    }

    fun toItem(idPlace: Long) : Item{
        val item = Item()

        item.update(
            amount = amount,
            idUnit = unit.first,
            idPlace = idPlace
        )

        if(!isNewItem || !forSetup)
            item.update(name = itemsMap[idItem]!!.name, idItem = idItem)

        if (areThereMomentsDate)
            item.update(
                idMoment = moment.first,
                date = getFormatterDateSql().format(date)
            )

        return item
    }



}