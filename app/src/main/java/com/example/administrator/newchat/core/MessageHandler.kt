package com.example.administrator.newchat.core

import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.avos.avoscloud.im.v2.messages.AVIMVideoMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.custom.getKey
import com.example.administrator.newchat.data.cache.DownloadUtil
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.utilities.*

class MessageHandler:AVIMMessageHandler(){

    override fun onMessage(message: AVIMMessage?, conversation: AVIMConversation?, client: AVIMClient?) {
        when (message) {
            is AVIMTextMessage -> cacheTextMessage(message, conversation!!)
            is AVIMImageMessage -> cacheImageMessage(message, conversation!!)
            is VerifyMessage->handlerVerifyMessage(message,conversation!!)
            is AVIMVideoMessage->handlerVoiceMessage(message,conversation!!)
        }
    }

    private fun handlerVoiceMessage(m:AVIMVideoMessage,c:AVIMConversation){
        cacheMessage(m,c,m.localFilePath?:m.fileUrl, VOICE_MESSAGE)
    }
    private fun handlerVerifyMessage(m:VerifyMessage,c:AVIMConversation){
        val owner = CoreChat.owner!!
        val map = c["Info"] as Map<String,String>
        val id = m.from
        val name = map[getKey(id, USER_NAME)] as String
        val avatar = map[getKey(id, AVATAR)]
        when(m.type){
            VerifyMessage.REQUEST->{
                val message = Message(m.messageId,c.conversationId,VerifyMessage.REQUEST,name,
                    VERIFY_MESSAGE, m.from,c.unreadMessagesCount,m.timestamp,CoreChat.userId!!, SENDING ,avatar)
                CoreChat.cacheMessage(message)
            }

            VerifyMessage.AGREE->{
                val ownerId = owner.userId
                val contact = Contact(id,name,name,c.conversationId,ownerId,avatar)
                CoreChat.addContact(contact)
            }
        }
    }

    private fun cacheMessage(m:AVIMMessage,c:AVIMConversation,content:String,type:Int){
        val map = c["Info"] as Map<String,String>
        val id = m.from
        val name = map[getKey(id, USER_NAME)] as String
        val avatar = map[getKey(id, AVATAR)]
        val message = Message(m.messageId,c.conversationId,content,name,
            type, m.from,c.unreadMessagesCount,m.timestamp,CoreChat.userId!!, SENDING,avatar)
        CoreChat.cacheMessage(message)
    }



    private fun cacheTextMessage( m: AVIMTextMessage,c: AVIMConversation){
        cacheMessage(m,c,m.text, TEXT_MESSAGE)
    }

    private fun cacheImageMessage(m:AVIMImageMessage,c: AVIMConversation){
        cacheMessage(m,c,m.fileUrl, IMAGE_MESSAGE)
    }
}