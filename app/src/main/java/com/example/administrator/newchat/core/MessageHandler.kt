package com.example.administrator.newchat.core

import android.util.Log
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.custom.getKey
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.utilities.AVATAR
import com.example.administrator.newchat.utilities.TEXT_MESSAGE
import com.example.administrator.newchat.utilities.USER_NAME
import com.example.administrator.newchat.utilities.VERIFY_MESSAGE

class MessageHandler:AVIMMessageHandler(){

    override fun onMessage(message: AVIMMessage?, conversation: AVIMConversation?, client: AVIMClient?) {
        when (message) {
            is AVIMTextMessage -> cacheTextMessage(message, conversation!!)
            is AVIMImageMessage -> cacheImageMessage(message, conversation!!)
            is VerifyMessage->handlerVerifyMessage(message,conversation!!)
        }
    }

    private fun handlerVerifyMessage(m:VerifyMessage,c:AVIMConversation){
        val owner = CoreChat.owner!!
        val map = c["Info"] as Map<String,String>
        val id = m.from
        val name = map[getKey(id, USER_NAME)] as String
        val avatar = map[getKey(id, AVATAR)]
        Log.d("map-","type${m.type}")
        when(m.type){
            VerifyMessage.REQUEST->{
                val message = Message(m.messageId,c.conversationId,VerifyMessage.REQUEST,name,
                    VERIFY_MESSAGE, m.from,c.unreadMessagesCount,m.timestamp,CoreChat.userId!!, avatar)
                Log.d("map-",message.toString())
                CoreChat.cacheMessage(message)
            }

            VerifyMessage.AGREE->{
                val ownerId = owner.userId
                val contact = Contact(id,name,name,c.conversationId,ownerId,avatar)
                CoreChat.addContact(contact)
                val message = Message(m.messageId,c.conversationId,VerifyMessage.AGREE,owner.name,
                    VERIFY_MESSAGE,ownerId,c.unreadMessagesCount,m.timestamp,ownerId, avatar)
                CoreChat.sendMessage(message)
            }
        }
    }



    private fun cacheTextMessage( m: AVIMTextMessage,c: AVIMConversation){
        val map = c["Info"] as Map<String,String>
        val id = m.from
        val name = map[getKey(id, USER_NAME)] as String
        val avatar = map[getKey(id, AVATAR)]
        val message = Message(m.messageId,c.conversationId,m.text,name,
            TEXT_MESSAGE, m.from,c.unreadMessagesCount,m.timestamp,CoreChat.userId!!,avatar)
        CoreChat.cacheMessage(message)
    }

    private fun cacheImageMessage(m:AVIMImageMessage,c: AVIMConversation){
        val map = c["Info"] as Map<String,String>
        val id = m.from
        val name = map[getKey(id, USER_NAME)] as String
        val avatar = map[getKey(id, AVATAR)]
        val message = Message(m.messageId,c.conversationId,m.fileUrl,name,
            TEXT_MESSAGE, m.from,c.unreadMessagesCount,m.timestamp,CoreChat.userId!!, avatar)
        CoreChat.cacheMessage(message)
    }
}