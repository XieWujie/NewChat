package com.example.administrator.newchat.data.message

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "message")
data class Message(
    @PrimaryKey()
    val id:String,
    val conversationId:String,
    val message:String,
    val name:String,
    val type:Int,
    val from:String,
    val unReadCount:Int,
    val createAt:Long,
    val owner:String,
    val avatar:String?
)