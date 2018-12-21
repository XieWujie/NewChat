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
               VERIFY_MESSAGE->sendVerifyMessage(it,message.message)
           }
        }
    }

     fun sendVerifyMessage(c:AVIMConversation,type:String){
         val m = VerifyMessage()
         m.type = type
         c.sendMessage(m,avimMessageOption,object :AVIMConversationCallback(){
             override fun done(e: AVIMException?) {
                 if (e == null) {

                    } else {
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

    private fun sendTextMessage(conversation: AVIMConversation,message: Message){
        val m = AVIMTextMessage()
        m.text = message.message
        checkConversation(conversation) {
            conversation.sendMessage(m, avimMessageOption, object : AVIMConversationCallback() {
                override fun done(e: AVIMException?) {
                    if (e == null) {
                        val new = message.copy(id = m.messageId, createAt = m.timestamp)
                        repository.insert(new)
                    } else {
                        e.printStackTrace()
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
                list!!.forEach{
                    queryMessageByConversationId(it.conversationId)
                }
            }else{
                e.printStackTrace()
            }
        })
    }



    override fun queryMessageByConversationId(id: String) {
        getConversation(id){
            conversation->
            val unReadCount = conversation.unreadMessagesCount
            val conId = conversation.conversationId
            var avatar:String? = null
            var name:String? = null
            conversation.queryMessages(object :AVIMMessagesQueryCallback(){
                override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                    if (e==null&&!list!!.isEmpty()){
                        if (avatar == null){
                            val m = list[0]
                            val map = conversation["Info"] as Map<String,String>
                            val id = m.from
                            name = map[getKey(id, USER_NAME)] as String
                            avatar = map[getKey(id, AVATAR)]
                        }
                        val messages = list!!.asReversed()
                            .map {
                                if (it is AVIMTextMessage ) {
                                    Message(
                                        it.messageId,conId, it.text,name?:conversation.name, TEXT_MESSAGE, it.from, unReadCount, it.timestamp,owner.userId ,avatar
                                    )
                                }else if ( it is AVIMImageMessage){
                                    Message(
                                        it.messageId,conId, it.fileUrl,name?:conversation.name, IMAGE_MESSAGE, it.from, unReadCount, it.timestamp,CoreChat.userId!!,  avatar
                                    )
                                } else if (it is VerifyMessage) {
                                    Message(
                                        it.messageId,conId, it.type,name?:conversation.name, VERIFY_MESSAGE, it.from, unReadCount, it.timestamp,owner.userId ,avatar
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


    override fun queryMessageByTime(id: String, timeStamp: Long) {
        getConversation(id){
            conversation->
            val unReadCount = conversation.unreadMessagesCount
            val conId = conversation.conversationId
            var avatar:String? = null
            var name:String? = null
           conversation.queryMessages(id,timeStamp,20,object :AVIMMessagesQueryCallback(){

               override fun done(list: MutableList<AVIMMessage>?, e: AVIMException?) {
                   if (e==null&&!list!!.isEmpty()){
                       if (avatar == null){
                           val get = conversation.get(list[0].from) as String
                           val(n,a) = get.H()
                           name = n
                           avatar = a
                       }
                       val messages = list!!.asReversed()
                           .map {
                               if (it is AVIMTextMessage) {
                                   Message(
                                       it.messageId,conId, it.text,name?:conversation.name , TEXT_MESSAGE, it.from, unReadCount, it.timestamp,owner.userId,  avatar
                                   )
                               }else if (it is AVIMImageMessage){
                                   Message(
                                       it.messageId,conId, it.fileUrl,name?:conversation.name,
                                       IMAGE_MESSAGE, it.from, unReadCount, it.timestamp,CoreChat.userId!!,  avatar
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