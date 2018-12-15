package com.example.administrator.newchat.core

import android.util.Log
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.*
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.utilities.IMAGE_MESSAGE
import com.example.administrator.newchat.utilities.TEXT_MESSAGE


class MessageManage(
    private val repository: MessageRepository,
    private val client: AVIMClient,
    private val owner:User
):AbstractMessage{

    private val avimMessageOption = AVIMMessageOption()
    private val conversationMap = HashMap<String,AVIMConversation>()

    init {
        avimMessageOption.isReceipt = true
        fetchNewMessage()
    }



    override fun sendMessage(message: Message) {
        getConversation(message.conversationId) {
           when(message.type){
               TEXT_MESSAGE ->sendTextMessage(it,message)
               IMAGE_MESSAGE ->sendImageMessage(it,message)
           }
        }
    }

    private fun getConversation(id:String,callback:(conversation:AVIMConversation)->Unit){

        if (conversationMap.containsKey(id)){
            callback(conversationMap[id]!!)
        }else{
           val conversation =  client.getConversation(id)
            conversationMap[id] = conversation
            callback(conversation)
        }
    }

    private fun sendTextMessage(conversation: AVIMConversation,message: Message){
        val m = AVIMTextMessage()
        m.text = message.message
        conversation.sendMessage(m,avimMessageOption,object :AVIMConversationCallback(){
            override fun done(e: AVIMException?) {
                if (e == null){
                    val new = message.copy(id = m.messageId,createAt = m.timestamp)
                    repository.insert(new)
                }else{
                    e.printStackTrace()
                }
            }
        })
    }

    private fun sendImageMessage(conversation: AVIMConversation,message: Message){
        val imageMessage = AVIMImageMessage(message.message)
        conversation.sendMessage(imageMessage,avimMessageOption,object :AVIMConversationCallback(){
            override fun done(e: AVIMException?) {
                if (e == null){
                    val message = message.copy(id = imageMessage.messageId,createAt = imageMessage.timestamp)
                    repository.insert(message)
                }
            }
        })
    }

    override fun cacheMessage(message: Message) {
        repository.insert(message)
    }



    fun fetchNewMessage(){
        val query = client.conversationsQuery
        query.findInBackground(object :AVIMConversationQueryCallback(){
            override fun done(list: MutableList<AVIMConversation>?, e: AVIMException?) = if (e == null){
                val messages = ArrayList<Message>()
                list!!.forEach{
                    queryMessageByConversationId(it.conversationId)
                }
                repository.insert(messages)
            }else{
                e.printStackTrace()
            }
        })
    }



    override fun queryMessageByConversationId(id: String) {
        getConversation(id){
            conversation->
            val unReadCount = conversation.unreadMessagesCount
            val name = conversation.name
            val conId = conversation.conversationId
            conversation.queryMessages(object :AVIMMessagesQueryCallback(){

                override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                    if (e==null&&!list!!.isEmpty()){
                        val messages = list!!.asReversed()
                            .map {
                                if (it is AVIMTextMessage) {
                                    Message(
                                        it.messageId,conId, it.text,name , TEXT_MESSAGE, it.from, unReadCount, it.timestamp,  ""
                                    )
                                }else if ( it is AVIMImageMessage){
                                    Message(
                                        it.messageId,conId, it.fileUrl,name , IMAGE_MESSAGE, it.from, unReadCount, it.timestamp,  ""
                                    )
                                } else {
                                    throw Throwable("can not find this type")
                                }
                            }.toList()
                        Log.d("query result",messages.toString())
                        repository.insert(messages)
                    }
                }
            })

        }
    }

    override fun queryMessageByTime(id: String, timeStamp: Long) {
        getConversation(id){
            conversation->
            val unReadCount = conversation.unreadMessagesCount
            val name = conversation.name
            val conId = conversation.conversationId
           conversation.queryMessages(id,timeStamp,20,object :AVIMMessagesQueryCallback(){

               override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                   if (e==null&&!list!!.isEmpty()){
                       val messages = list!!.asReversed()
                           .map {
                               if (it is AVIMTextMessage) {
                                   Message(
                                       it.messageId,conId, it.text,name , TEXT_MESSAGE, it.from, unReadCount, it.timestamp,  ""
                                   )
                               }else if (it is AVIMImageMessage){
                                   Message(
                                       it.messageId,conId, it.fileUrl,name ,
                                       IMAGE_MESSAGE, it.from, unReadCount, it.timestamp,  ""
                                   )
                               } else {
                                   throw Throwable("can not find this type")
                               }
                           }.toList()
                       repository.insert(messages)
                   }
               }
           })
        }
    }

}