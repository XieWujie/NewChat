package com.example.administrator.newchat.core

import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.*
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.custom.getKey
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.utilities.*
import java.lang.reflect.Member


class ContactsManage(private val respository: ContactRespository,
                     private val client: AVIMClient,
                     private val contact_list_id: String,
                     private val owner:User
):AbstractContacts{

    private val contactMap = HashMap<String,Contact>()
    init {
        cacheContactByNet()
    }

    override fun addContact(contact: Contact) {
        val o = AVObject.createWithoutData(CONTACT,owner.contactListId)
        o.addUnique(CONTACTS,contact.toString())
        o.saveInBackground()
        respository.addContact(contact)
    }

    override fun addContactById(id:String,markName: String) {
        getUserObjectById(id) {
            val avatar = it!!.getString(AVATAR)
            val name = it!!.getString(USER_NAME)
            findConversation(id,name,avatar?:"") { conversation ->
                val m = VerifyMessage()
                m.type = VerifyMessage.REQUEST
                val op = AVIMMessageOption()
                op.isReceipt = true
                conversation.sendMessage(m, op, object : AVIMConversationCallback() {
                    override fun done(e: AVIMException?) {
                        if (e == null) {

                        }
                    }
                })
            }
        }
    }

    private fun getUserObjectById(id:String,callback:(o:AVObject)->Unit){
        val o = AVObject.createWithoutData("_User",id)
        o.fetchInBackground(object :GetCallback<AVObject>(){
            override fun done(a: AVObject?, e: AVException?) {
                if (e == null){
                    callback(a!!)
                }else{
                    e.printStackTrace()
                }
            }
        })
    }

    override fun addContactById(id: String, markName: String, conversationId: String) {
        val c = client.getConversation(conversationId)
        fetchConversation(c) {
            val map = c["Info"] as Map<String, String>
            val name = map[getKey(id, USER_NAME)] as String
            val avatar = map[getKey(id, AVATAR)]
            val contact = Contact(id, name, markName, conversationId, owner.userId, avatar)
            addContact(contact)
            c.read()
        }
    }

    override fun findConversation(id:String,name:String,avatar:String?,callback: (conversation: AVIMConversation) -> Unit){
        client.createConversation(listOf(id),name,null,false,true,object :AVIMConversationCreatedCallback(){
            override fun done(c: AVIMConversation?, e: AVIMException?) {
                if (e == null && c!=null) {
                    if (c["Info"] == null) {
                        val map = mapOf(
                            getKey(owner.userId, USER_NAME) to owner.name,
                            getKey(owner.userId, AVATAR) to owner.avatar,
                            getKey(id, USER_NAME) to name,
                            getKey(id, AVATAR) to avatar,
                            getKey(owner.userId, OTHER_NAME) to name,
                            getKey(id, OTHER_NAME) to name
                        )
                        c["Info"] = map
                        c.updateInfoInBackground(object : AVIMConversationCallback() {
                            override fun done(p0: AVIMException?) {
                                if (p0 == null) {
                                    callback(c)
                                }
                            }
                        })
                    }else{
                        callback(c)
                    }
                }
            }
        })
    }

    override fun removeContact(contact: Contact) {
        val o = AVObject.createWithoutData(CONTACT,owner.contactListId)
        o.fetchInBackground(object :GetCallback<AVObject>(){
            override fun done(obj: AVObject?, e: AVException?) {
                if (e == null ){
                  val list =   obj!![CONTACTS]
                    if (list == null)return
                    list as List<String>
                    val l = ArrayList<String>()
                    val str = contact.toString()
                    list.forEach {
                        if (it != str){
                            l.add(it)
                        }
                    }
                    obj.remove(CONTACTS)
                    obj.addAllUnique(CONTACTS,l)
                    obj.saveInBackground()
                    respository.removeContact(contact)
                }
            }
        })
        o.saveInBackground()
        respository.addContact(contact)
    }

    override fun markContactName( contactId: String, markName: String) {

    }

    private fun fetchConversation(c: AVIMConversation,fetchCallback:()->Unit){
        if (c.isShouldFetch){
            c.fetchInfoInBackground(object :AVIMConversationCallback(){
                override fun done(p0: AVIMException?) {
                    if (p0 == null){
                        fetchCallback()
                    }else{
                        p0.printStackTrace()
                    }
                }
            })
        }else{
            fetchCallback()
        }
    }


    override fun cacheContactByNet() {
            val q = AVQuery<AVObject>(CONTACT)
            q.whereEqualTo("objectId",contact_list_id)
            q.findInBackground(object :FindCallback<AVObject>(){
                override fun done(p0: MutableList<AVObject>?, e: AVException?) {
                    if (e==null&&p0!=null&&p0.size>0){
                        val list = p0[0].getList(CONTACTS)
                        list?.forEach {
                            if (it is String){
                                val (id,name,conversationId) = it.divide()
                                val o = AVObject.createWithoutData("_User",id)
                                o.fetchInBackground(object :GetCallback<AVObject>(){
                                    override fun done(p0: AVObject?, p1: AVException?) {
                                        if (p1==null&&p0!=null){
                                            val nickname = p0.getString(USER_NAME)
                                            val avatar = p0.getString(AVATAR)
                                            val contact = Contact(id,nickname,name,conversationId,owner.userId,avatar)
                                            contactMap[id] = contact
                                            respository.addContact(contact)
                                        }else{
                                            p1?.printStackTrace()
                                        }
                                    }
                                })

                            }
                        }
                    }
                }

            })

    }

    private fun String.divide():Triple<String,String,String>{
        val list = this.split("-")
        if (list.size==3){
            return Triple(list[0],list[1],list[2])
        }else{
            throw Throwable("params is not two")
        }
    }

    override fun findConversationId(contactIds: List<String>, callback: (conversationId: String) -> Unit) {
        val id = contactIds[0]
        getUserObjectById(id){
            val avatar = it!!.getString(AVATAR)
            val name = it!!.getString(USER_NAME)
            findConversation(id,name,avatar) { conversation ->
                callback(conversation.conversationId)
            }
        }
    }

}