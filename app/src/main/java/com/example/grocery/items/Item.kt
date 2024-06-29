package com.example.grocery.items

import android.database.Cursor
import android.util.Log

class Item() {

    constructor(cursor: Cursor? = null) : this() {
        if(cursor != null) {
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
                idParent = cursor.getLong(cursor.getColumnIndexOrThrow("idParent"))
            val idUnitColumnIndex = cursor.getColumnIndex("idUnit")
            if (idUnitColumnIndex != -1)
                idUnit = cursor.getLong(cursor.getColumnIndexOrThrow("idUnit"))
            val idMomentColumnIndex = cursor.getColumnIndex("idMoment")
            if (idMomentColumnIndex != -1)
                idMoment = cursor.getLong(cursor.getColumnIndexOrThrow("idMoment"))
            val idPlaceColumnIndex = cursor.getColumnIndex("idPlace")
            if (idPlaceColumnIndex != -1)
                idPlace = cursor.getLong(cursor.getColumnIndexOrThrow("idPlace"))



        }else{
            Log.e("CURSOR", "The cursor is null.")
        }
    }

    var id: Long = -1
        private set
    var idItem: Long = -1
        private set

    var name: String = ""
        private set

    var amount: Int = 0
        private set
    var amountInventory : Int = -1
        private set

    var date: String = ""
        private set

    var price: Float = -1.0f
        private set

    var checked : Boolean = false
        private set

    var idParent: Long = -1
        private set
    var idUnit: Long = -1
        private set
    var idMoment: Long = -1
        private set
    var idPlace: Long = -1
        private set


    fun update(
        id: Long? = null,
        idItem: Long? = null,
        name: String? = null,
        amount: Int? = null,
        amountInventory: Int?=null,
        date: String? = null,
        price: Float? = null,
        checked: Boolean? = null,
        idParent: Long? = null,
        idUnit: Long? = null,
        idMoment: Long? = null,
        idPlace: Long? = null
    ){
        if(id != null)
            this.id = id
        if(idItem != null)
            this.idItem = idItem

        if(name != null)
            this.name = name

        if(amount != null)
            this.amount = amount
        if(amountInventory != null)
            this.amountInventory = amountInventory

        if(date != null)
            this.date = date

        if(price != null)
            this.price = price

        if(checked != null)
            this.checked = checked

        if(idParent != null)
            this.idParent = idParent
        if(idUnit != null)
            this.idUnit = idUnit
        if(idMoment != null)
            this.idMoment = idMoment
        if(idPlace != null)
            this.idPlace = idPlace

    }

    fun reset(){
        id  = -1

        name = ""

        amount  = 0
        amountInventory   = -1

        date = ""

        price = -1.0f

        checked = false

        idParent  = -1
        idMoment  = -1
        idUnit  = -1
        idPlace = -1

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