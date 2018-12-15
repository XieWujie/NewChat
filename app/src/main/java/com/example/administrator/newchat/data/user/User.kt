package com.example.administrator.newchat.data.user

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class User(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val userId:String,
    val name:String,
    val password:String,
    val loginTime:Long,
    var isLogout:Boolean = true,
    val contactListId:String
)