package com.example.administrator.newchat.utilities

import android.content.Context
import android.content.SharedPreferences

class TempCountGet(context: Context){

    private val name = "tempCountGet"
    private val key = "count"
    private val preferences = context.getSharedPreferences(name,Context.MODE_PRIVATE)
    private val editor = preferences.edit()
    private var begin = 0

    init {
        begin = preferences.getInt(key,0)
    }
    fun get():String{
        val newValue = begin+1
        editor.putInt(key,newValue)
        return newValue.toString()
    }
}