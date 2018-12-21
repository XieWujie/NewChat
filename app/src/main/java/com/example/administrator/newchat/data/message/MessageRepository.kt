package com.example.administrator.newchat.data.message

import android.util.Log
import com.example.administrator.newchat.utilities.runOnNewThread

class MessageRepository private constructor(private val messageDao: MessageDao){

    fun queryById(id:String) = messageDao.queryById(id)

    fun insert(message: Message){
        runOnNewThread {
            messageDao.insert(message)
        }
    }

    fun insert(messages:List<Message>){
        runOnNewThread {
            messageDao.insert(messages)
        }
    }

    fun getNewMessage(owner:String) = messageDao.getNewMessage(owner)

    companion object {

        @Volatile private var instance:MessageRepository? = null

        fun getInstance(messageDao: MessageDao):MessageRepository{
            return instance?: synchronized(this){
                instance?:MessageRepository(messageDao).also { instance = it }
            }
        }
    }
}