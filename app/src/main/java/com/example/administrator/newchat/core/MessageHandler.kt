package com.example.administrator.newchat.core

import android.util.Log
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.utilities.TEXT_MESSAGE

class MessageHandler:AVIMMessageHandler(){

    override fun onMessage(message: AVIMMessage?, conversation: AVIMConversation?, client: AVIMClient?) {
        when(message){
            is AVIMTextMessage->cacheTextMessage(message,conversation!!)
            is AVIMImageMessage->cacheImageMessage(message,conversation!!)
        }
    }



    private fun cacheTextMessage(m: AVIMTextMessage,conversation: AVIMConversation){
        val message = Message(m.messageId,conversation.conversationId,m.text,conversation.name,
            TEXT_MESSAGE, m.from,conversation.unreadMessagesCount,m.timestamp,"")
        Log.d("messsage",message.toString())
        CoreChat.cacheMessage(message)
    }

    private fun cacheImageMessage(m:AVIMImageMessage,conversation: AVIMConversation){
        val message = Message(m.messageId,conversation.conversationId,m.fileUrl,conversation.name,
            TEXT_MESSAGE, m.from,conversation.unreadMessagesCount,m.timestamp,"")
        Log.d("messsage",message.toString())
        CoreChat.cacheMessage(message)
    }
}