package com.example.administrator.newchat.data.contacts

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "contact")
data class Contact(
    @PrimaryKey val id:String,
    val nickName:String,
    val name:String = nickName,
    val conversationId:String,
    val ownerId:String,
    val avatar:String?
){

    override fun toString(): String {
        return "$id-$nickName-$conversationId"
    }
}


