package com.example.grocery.items

import android.database.Cursor
import android.util.Log
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableLongState
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

class MutableItem(){

    val id: MutableLongState
    val name: MutableState<String>
    val amount: MutableIntState
    val amountInventory : MutableIntState
    val date: MutableState<String>
    val price: MutableFloatState
    val checked : MutableState<Boolean>
    val idParent: MutableLongState
    val idItem: MutableLongState
    val idUnit: MutableLongState
    val idMoment: MutableLongState
    val idPlace: MutableLongState

    constructor(
        id: Long,
        idItem: Long,
        name: String,
        amount: Int,
        amountInventory : Int,
        date: String,
        price: Float,
        checked : Boolean,
        idParent: Long,
        idUnit: Long,
        idMoment: Long,
        idPlace: Long,
    ): this(
        id = id,
        name = name,
        amount = -1,
        amountInventory = -1,
        date = "",
        price = -1f,
        checked = false,
        idParent = -1,
        idItem = -1,
        idUnit = -1,
        idMoment = -1,
        idPlace = -1
    )

    fun toItem() : Item{
        return Item(
            id = id.longValue,
            name = name.value,
            amount = amount.intValue,
            amountInventory = amountInventory.intValue,
            date = date.value,
            price = price.floatValue,
            checked = checked.value,
            idParent = idParent.longValue,
            idItem = idItem.longValue,
            idUnit = idUnit.longValue,
            idMoment = idMoment.longValue,
            idPlace = idPlace.longValue
        )
    }


    override fun toString(): String {
        return """
            {
                "id":$id,
                "name": "$name",
                "amount": $amount,
                "amountInventory": $amountInventory,
                "date": "$date",
                "price": $price,
                "checked": $checked,
                "idParent": $idParent,
                "idItem": $idItem,
                "idUnit": $idUnit,
                "idMoment": $idMoment,
                "idPlace": $idPlace
            }
        """.trimIndent()
    }
}

fun fromCursorToMutableItem(cursor: Cursor) : MutableItem{
    var id: Long = -1
    var idItem: Long = -1
    var name: String = ""
    var amount: Int = -1
    var amountInventory : Int = -1
    var date: String = ""
    var price: Float = -1f
    var checked : Boolean = false
    var idParent: Long = -1
    var idUnit: Long = -1
    var idMoment: Long = -1
    var idPlace: Long = -1

    val idColumnIndex = cursor.getColumnIndex("id")
    if (idColumnIndex != -1)
        id = cursor.getLong(idColumnIndex)

    val idItemColumnIndex = cursor.getColumnIndex("idItem")
    if (idItemColumnIndex != -1)
        idItem = cursor.getLong(idItemColumnIndex)

    val nameColumnIndex = cursor.getColumnIndex("name")
    if (nameColumnIndex != -1)
        name = cursor.getString(nameColumnIndex)

    val amountColumnIndex = cursor.getColumnIndex("amount")
    if (amountColumnIndex != -1)
        amount = cursor.getInt(amountColumnIndex)

    val amountInventoryColumnIndex = cursor.getColumnIndex("amountInventory")
    if (amountInventoryColumnIndex != -1)
        amountInventory = cursor.getInt(amountInventoryColumnIndex)

    val dateColumnIndex = cursor.getColumnIndex("date")
    if (dateColumnIndex != -1)
        date = cursor.getString(dateColumnIndex)

    val priceColumnIndex = cursor.getColumnIndex("price")
    if (priceColumnIndex != -1)
        price = cursor.getFloat(priceColumnIndex)

    val checkedColumnIndex = cursor.getColumnIndex("checked")
    if (dateColumnIndex != -1)
        checked = cursor.getInt(checkedColumnIndex) == 1

    val idParentColumnIndex = cursor.getColumnIndex("idParent")
    if (idParentColumnIndex != -1)
        idParent = cursor.getLong(idParentColumnIndex)
    val idUnitColumnIndex = cursor.getColumnIndex("idUnit")
    if (idUnitColumnIndex != -1)
        idUnit = cursor.getLong(idUnitColumnIndex)
    val idMomentColumnIndex = cursor.getColumnIndex("idMoment")
    if (idMomentColumnIndex != -1)
        idMoment = cursor.getLong(idMomentColumnIndex)
    val idPlaceColumnIndex = cursor.getColumnIndex("idPlace")
    if (idPlaceColumnIndex != -1)
        idPlace = cursor.getLong(idPlaceColumnIndex)

    return MutableItem(
        id = id,
        idItem = idItem,
        name = name,
        amount = amount,
        amountInventory = amountInventory,
        date = date,
        price = price,
        checked = checked,
        idParent = idParent,
        idUnit = idUnit,
        idMoment = idMoment,
        idPlace = idPlace
    )
}