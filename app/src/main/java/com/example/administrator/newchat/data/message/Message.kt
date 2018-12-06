package com.example.administrator.newchat.data.message

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "message")
data class Message(
    @PrimaryKey(autoGenerate = true)
    val id:Int = 1,
    val clientId:String,
    val message:String,
    val ownerId:String,
    val type:Int,
    val from:String,
    val isMe:Boolean,
    val avator:String?
)