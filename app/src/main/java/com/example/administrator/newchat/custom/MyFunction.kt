package com.example.administrator.newchat.custom

fun String.H():Pair<String?,String?>{
    val l = this.split("-")
    var avatar:String? = null
    var name:String? = null
    if (l.size == 2){
        avatar = l[1]
        name = l[0]
    }
    return name to avatar
}

fun getKey(value1:String,value2:String):String{
    return (value1+value2)
}