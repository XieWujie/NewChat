package com.example.administrator.newchat.core

import android.content.Context
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.utilities.TEXT_MESSAGE
import java.lang.RuntimeException

class MessageManage(context: Context):AbstractMessage{

    private lateinit var messageRepository: MessageRepository
    private val conversationMap = HashMap<String,AVIMConversation>()

    private var cilent:AVIMClient? = null

    init {
        messageRepository = MessageRepository.getInstance(AppDatabase.getInstance(context).getMessageDao())
    }

    fun initClient(ownerId:String){
        val callback = object :AVIMClientCallback(){
            override fun done(c: AVIMClient?, e: AVIMException?) {
                if (e == null){
                    cilent = c!!
                }else{
                    e.printStackTrace()
                }
            }

        }
        AVIMClient.getInstance(ownerId).open(callback)
    }

    override fun sendMessage(message: Message) {
        if (cilent!=null) {
            getConversation(message) {
                if (message.type == TEXT_MESSAGE) {
                    sendTextMessage(it, message)
                }
            }
        }else{
            val callback = object :AVIMClientCallback(){
                override fun done(c: AVIMClient?, e: AVIMException?) {
                    if (e == null){
                        cilent = c!!
                        sendMessage(message)
                    }else{
                        e.printStackTrace()
                    }
                }

            }
            AVIMClient.getInstance(CoreChat.userId!!).open(callback)
        }
    }

    private fun getConversation(message: Message,callback:(conversation:AVIMConversation)->Unit){
        val id = message.clientId
        if (conversationMap.containsKey(id)){
            callback(conversationMap[id]!!)
        }else{
            checkClient()
            cilent?.createConversation(listOf(message.clientId),message.from,null,object :AVIMConversationCreatedCallback(){
                override fun done(p0: AVIMConversation?, p1: AVIMException?) {
                    if (p1 == null){
                        callback(p0!!)
                    }else{
                        p1.printStackTrace()
                        throw RuntimeException("get conversation fail")
                    }
                }
            })
        }
    }

    private fun sendTextMessage(conversation: AVIMConversation,message: Message){
        val m = AVIMTextMessage()
        m.text = message.message
        conversation.sendMessage(m,object :AVIMConversationCallback(){
            override fun done(e: AVIMException?) {
                if (e == null){
                    messageRepository.insert(message)
                }else{
                    e.printStackTrace()
                }
            }
        })
    }

    override fun cacheMessage(message: Message) {
        messageRepository.insert(message)
    }

    private fun checkClient(){
        if (cilent==null){
            initClient(CoreChat.userId!!)
        }
    }

}