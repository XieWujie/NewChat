package com.example.administrator.newchat.core

import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMMessage
import com.avos.avoscloud.im.v2.AVIMMessageHandler
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.utilities.TEXT_MESSAGE

class MessageHandler:AVIMMessageHandler(){

    override fun onMessage(message: AVIMMessage?, conversation: AVIMConversation?, client: AVIMClient?) {
        if (message is AVIMTextMessage){
            cacheTextMessage(message)
        }
    }

    private fun cacheTextMessage(m: AVIMTextMessage){
        val message = Message(0,m.from,m.text,CoreChat.userId!!, TEXT_MESSAGE,m.from,false,"")
        CoreChat.cacheMessage(message)
    }
}