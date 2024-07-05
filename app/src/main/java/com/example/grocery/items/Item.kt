package com.example.grocery.items

import android.database.Cursor
import com.example.grocery.uielements.date.getDateNow
import com.example.grocery.utilities.getFormatterDateSql
import com.example.grocery.utilities.toListNeedItem
import org.json.JSONArray
import org.json.JSONObject
import java.util.Date

data class NeedItem(
    val id: Long,
    val amount: Int,
){
    constructor(jsonObject: JSONObject): this(
        id = jsonObject["id"].toString().toLong(),
        amount = jsonObject["amount"].toString().toInt()
    )

    override fun toString(): String {
        return """
            {
                "id":$id,
                "amount": $amount
            }
        """.trimIndent()
    }
}

data class Item(
    val id: Long,
    val idItem: Long,
    val name: String,
    val amount: Int,
    var amountInventory : Int,
    var date: Date,
    val price: Float,
    val checked : Boolean,
    val children: List<NeedItem>,
    val idUnit: Long,
    val idMoment: Long,
    val idPlace: Long,
) {
    constructor(): this(
        id = 0,
        name = "",
        amount = 0,
        amountInventory = 0,
        date = getDateNow(),
        price = -1f,
        checked = false,
        children = emptyList(),
        idItem = -1,
        idUnit = -1,
        idMoment = -1,
        idPlace = -1
    )

    constructor(jsonObject: JSONObject): this(
        id = jsonObject["id"].toString().toLong(),
        name = jsonObject["name"].toString(),
        amount = jsonObject["amount"].toString().toInt(),
        amountInventory = jsonObject["amountInventory"].toString().toInt(),
        date = getFormatterDateSql().parse(jsonObject["date"].toString())!!,
        price = jsonObject["price"].toString().toFloat(),
        checked = jsonObject["checked"].toString().toBoolean(),
        children = JSONArray(jsonObject["children"].toString()).toListNeedItem(),
        idItem = jsonObject["idItem"].toString().toLong(),
        idUnit = jsonObject["idUnit"].toString().toLong(),
        idMoment = jsonObject["idMoment"].toString().toLong(),
        idPlace = jsonObject["idPlace"].toString().toLong(),
    )

    override fun toString(): String {
        return """
            {
                "id":$id,
                "name": "$name",
                "amount": $amount,
                "amountInventory": $amountInventory,
                "date": "${getFormatterDateSql().format(date)}",
                "price": $price,
                "checked": $checked,
                "children": $children,
                "idItem": $idItem,
                "idUnit": $idUnit,
                "idMoment": $idMoment,
                "idPlace": $idPlace
            }
        """.trimIndent()
    }
}

fun fromCursorToItem(cursor: Cursor) : Item{
    var id: Long = -1
    var idItem: Long = -1
    var name: String = ""
    var amount: Int = 0
    var amountInventory : Int = 0
    var date: Date = Date()
    var price: Float = 0f
    var checked : Boolean = false
    var children: List<NeedItem> = emptyList()
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

    val amountInventoryColumnIndex = cursor.getColumnIndex("amount_inventory")
    if (amountInventoryColumnIndex != -1)
        amountInventory = cursor.getInt(amountInventoryColumnIndex)

    val dateColumnIndex = cursor.getColumnIndex("date")
    if (dateColumnIndex != -1)
        date = getFormatterDateSql().parse(cursor.getString(dateColumnIndex))!!

    val priceColumnIndex = cursor.getColumnIndex("price")
    if (priceColumnIndex != -1)
        price = cursor.getFloat(priceColumnIndex)

    val checkedColumnIndex = cursor.getColumnIndex("checked")
    if (dateColumnIndex != -1)
        checked = cursor.getInt(checkedColumnIndex) == 1

    val childrenColumnIndex = cursor.getColumnIndex("children")
    if (childrenColumnIndex != -1)
        children = JSONArray(cursor.getString(childrenColumnIndex)).toListNeedItem()

    val idUnitColumnIndex = cursor.getColumnIndex("idUnit")
    if (idUnitColumnIndex != -1)
        idUnit = cursor.getLong(idUnitColumnIndex)
    val idMomentColumnIndex = cursor.getColumnIndex("idMoment")
    if (idMomentColumnIndex != -1)
        idMoment = cursor.getLong(idMomentColumnIndex)
    val idPlaceColumnIndex = cursor.getColumnIndex("idPlace")
    if (idPlaceColumnIndex != -1)
        idPlace = cursor.getLong(idPlaceColumnIndex)

    return Item(
        id = id,
        idItem = idItem,
        name = name,
        amount = amount,
        amountInventory = amountInventory,
        date = date,
        price = price,
        checked = checked,
        children = children,
        idUnit = idUnit,
        idMoment = idMoment,
        idPlace = idPlace
    )
}