package com.example.testapp.body.permissions

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

abstract class Permissions(){
    private var isPermissionAvailable : Boolean by mutableStateOf(false)

    fun getStatus() : Boolean{
        return isPermissionAvailable
    }

    abstract fun recheck()

}