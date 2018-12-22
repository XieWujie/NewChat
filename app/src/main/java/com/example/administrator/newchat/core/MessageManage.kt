package com.example.administrator.newchat.core

import android.util.Log
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.*
import com.avos.avoscloud.im.v2.messages.AVIMImageMessage
import com.avos.avoscloud.im.v2.messages.AVIMTextMessage
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.H
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.custom.getKey
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.utilities.*
import java.lang.Exception


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



    override fun sendMessage(message: Message,e:(e:Exception?)->Unit) {
        getConversation(message.conversationId) {
           when(message.type){
               TEXT_MESSAGE ->sendTextMessage(it,message,e)
               IMAGE_MESSAGE ->sendImageMessage(it,message,e)
               VERIFY_MESSAGE->sendVerifyMessage(it,message.message,e)
           }
        }
    }

     fun sendVerifyMessage(c:AVIMConversation,type:String,exception:(e:Exception?)->Unit){
         val m = VerifyMessage()
         m.type = type
         c.sendMessage(m,avimMessageOption,object :AVIMConversationCallback(){
             override fun done(e: AVIMException?) {
                 if (e == null) {
                     c.read()
                     exception(e)
                    } else {
                     exception(e)
                        e.printStackTrace()
                    }
                }
            })
    }

    private fun getConversation(id:String,callback:(conversation:AVIMConversation)->Unit){

        if (conversationMap.containsKey(id)){
            callback(conversationMap[id]!!)
        }else{
           val conversation =  client.getConversation(id)
            if (conversation.isShouldFetch){
                conversation.fetchInfoInBackground(object :AVIMConversationCallback(){
                    override fun done(e: AVIMException?) {
                        if (e == null){
                            conversationMap[id] = conversation
                            callback(conversation)
                        }else{
                            e.printStackTrace()
                        }
                    }
                })
            }else{
                conversationMap[id] = conversation
                callback(conversation)
            }

        }
    }

    private fun sendTextMessage(conversation: AVIMConversation,message: Message,exeption:(e:Exception?)->Unit){
        val m = AVIMTextMessage()
        m.text = message.message
        checkConversation(conversation) {
            conversation.sendMessage(m, avimMessageOption, object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        val new = message.copy(id = m.messageId, createAt = m.timestamp)
                        repository.insert(new)
                        conversation.read()
                        exeption(e)
                    } else {
                        e.printStackTrace()
                        exeption(e)
                    }
                }
            })
        }

    }

    private fun checkConversation(c: AVIMConversation,checkCallback:()->Unit){
        val map = c["Info"] as Map<String,String?>
        val id = owner.userId
        val key = getKey(id, AVATAR)
        val avatar = map[key]
        Log.d("map-", map.toString())
        if (avatar == null&&owner.avatar!=null){
            val hashMap = HashMap<String,String?>()
            hashMap.putAll(map)
            hashMap[key] = owner.avatar
            c["Info"] = hashMap.toMap()
            Log.d("map-h",hashMap.toString())
            c.updateInfoInBackground(object :AVIMConversationCallback(){
                override fun done(p0: AVIMException?) {
                    if (p0 == null){
                        checkCallback()
                    }else{
                        p0.printStackTrace()
                    }
                }
            })
        }else{
            checkCallback()
        }
    }

    private fun sendImageMessage(conversation: AVIMConversation,message: Message,exeption:(e:Exception?)->Unit){
        repository.insert(message)
        val imageMessage = AVIMImageMessage(message.message)
        conversation.sendMessage(imageMessage,avimMessageOption,object :AVIMConversationCallback(){
            override fun done(e: AVIMException?) {
                if (e == null){
                    val message = message.copy(id = imageMessage.messageId,createAt = imageMessage.timestamp,sendState = SEND_SUCCEED)
                    repository.insert(message)
                    conversation.read()
                    exeption(e)
                }else{
                    val message = message.copy(id = imageMessage.messageId?:message.id,sendState = SEND_FAIL)
                    repository.insert(message)
                    exeption(e)
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
                list!!.forEach{
                    queryMessageByConversationId(it.conversationId,1)
                }
            }else{
                e.printStackTrace()
            }
        })
    }



    override fun queryMessageByConversationId(id: String,limit:Int) {
        getConversation(id){
            conversation->
            conversation.queryMessages(limit,object :AVIMMessagesQueryCallback(){
                override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                    if (e==null){
                        convertMessages(conversation, list!!){
                            repository.insert(it)
                        }
                    }
                }
            })
        }
    }


    override fun queryMessageByTime(id: String, timeStamp: Long) {
        getConversation(id){
            conversation->
           conversation.queryMessages(id,timeStamp,20,object :AVIMMessagesQueryCallback(){

               override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                   if (e==null){
                       convertMessages(conversation,list!!){
                           repository.insert(it)
                       }
                   }
               }
           })
        }
    }

    private fun convertMessages(conversation: AVIMConversation, messages:List<AVIMMessage>,convertCallback:(m:List<Message>)->Unit){
        if (messages.isEmpty()){
            return
        }
        val from = messages[0].from
        val ownerId = owner.userId
        val unReadCount = conversation.unreadMessagesCount
        val conId = conversation.conversationId
        val map = conversation["Info"] as Map<String,String?>
        val  name = map[getKey(from, USER_NAME)] ?:""
        val avatar = map[getKey(from, AVATAR)]
        val list = messages.asReversed()
            .map {
                when(it){
                    is AVIMTextMessage->{
                        Message(
                            it.messageId,conId, it.text,name, TEXT_MESSAGE, it.from, unReadCount, it.timestamp,ownerId ,SEND_SUCCEED,avatar
                        )
                    }
                    is  AVIMImageMessage->{
                        Message(
                            it.messageId,conId, it.fileUrl,name, IMAGE_MESSAGE, it.from, unReadCount, it.timestamp,ownerId,SEND_SUCCEED,avatar
                        )
                    }
                    is VerifyMessage->{
                        Message(
                            it.messageId,conId, it.type,name, VERIFY_MESSAGE, it.from, unReadCount, it.timestamp,ownerId, SEND_SUCCEED,avatar
                        )
                    }
                    else->{
                        Message(it.messageId,conId,it.content,name, UNKNOW_TYPE,from,0,it.timestamp, owner.userId, SEND_SUCCEED,avatar)
                    }
                }
            }.toList()
        convertCallback(list)
    }

    override fun deleteMessage(message: Message) {
        repository.delete(message)
    }
}