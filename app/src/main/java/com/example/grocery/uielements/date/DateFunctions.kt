package com.example.grocery.uielements.date

import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

fun getInstance(): Calendar {
    return Calendar.getInstance()
}

fun getDateNow() : Date {
    return getInstance().time
}

fun getUpdateDate(date: Date, days: Int) : Date {
    val instance = Calendar.getInstance()
    instance.time = date
    instance.add(Calendar.DATE, days)
    return instance.time
}

fun getDateTime(simpleDateFormat: SimpleDateFormat) : String {

    val calendarTime = getDateNow()
    val formattedTime = simpleDateFormat.format(calendarTime)

    return formattedTime.toString()

}