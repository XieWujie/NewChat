package com.example.administrator.newchat.core

import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationCreatedCallback
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.utilities.CONTACT
import com.example.administrator.newchat.utilities.CONTACTS
import com.example.administrator.newchat.utilities.USER_NAME



class ContactsManage(private val respository: ContactRespository,
                     private val client: AVIMClient,
                     private val contact_list_id: String,
                     private val owner:User
):AbstractContacts{


    init {
        cacheContactByNet()
    }
    override fun addContact(contact: Contact) {
        val o = AVObject.createWithoutData(CONTACT,contact_list_id)
        o.add(CONTACTS,contact.toString())
        o.saveInBackground()
        respository.addContact(contact)
    }

    override fun removeContact(contact: Contact) {

    }

    override fun markContactName( contactId: String, markName: String) {

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
                                            val contact = Contact(id,nickname,name,conversationId,owner.userId)
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

    override fun findConversationId(contactIds: List<String>,name:String, callback: (conversationId: String) -> Unit) {
        client.createConversation(contactIds,name,null,object :AVIMConversationCreatedCallback(){

            override fun done(c: AVIMConversation?, e: AVIMException?) {
                if (e == null){
                    callback(c!!.conversationId)
                }
            }
        })
    }
}