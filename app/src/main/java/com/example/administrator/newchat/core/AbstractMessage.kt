package com.example.administrator.newchat.core


import com.example.administrator.newchat.data.message.Message
import java.lang.Exception

interface AbstractMessage{

    fun sendMessage(message:Message,e:(e:Exception?)->Unit)

    fun cacheMessage(message: Message,isUpdate:Boolean = false)

    fun deleteMessages(conversationId:String)

    fun queryMessageByConversationId(id:String,limit:Int)

    fun queryMessageByTime(id:String,timeStamp:Long)

    fun deleteMessage(message: Message)

}