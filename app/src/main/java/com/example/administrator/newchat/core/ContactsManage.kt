package com.example.administrator.newchat.core

import android.content.Context
import com.avos.avoscloud.*
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.utilities.CONTACT
import com.example.administrator.newchat.utilities.CONTACTS
import com.example.administrator.newchat.utilities.USER_NAME
import com.example.administrator.newchat.utilities.runOnNewThread
import org.json.JSONObject

class ContactsManage(context: Context):AbstractContacts{

    private lateinit var contactRespository: ContactRespository

    init {
        contactRespository = ContactRespository.getInstance(AppDatabase.getInstance(context).getContactsDao())
    }

    override fun addContact(contact_list_id: String, contact: Contact) {
        val o = AVObject.createWithoutData(CONTACT,contact_list_id)
        o.add(CONTACTS,contact.toString())
        o.saveInBackground()
        contactRespository.addContact(contact)
    }

    override fun removeContact(contact_list_id: String, contact: Contact) {

    }

    override fun markContactName(contact_list_id: String, contactId: String, markName: String) {

    }


    override fun getAllContactsId(contact_list_id: String, callback: (contact: List<String>) -> Unit) {
       val list =  contactRespository.getContactId()
        if (list==null || list.isEmpty()) {
            getContactIdByNet(contact_list_id){
               //contactRespository.addContacts(list)
                callback(it)
        }
        }else{
            callback(list)
        }
    }

    private fun getContactIdByNet(ownerId: String,callback: (contact: List<String>) -> Unit){
         val o = AVObject.createWithoutData(CONTACT,ownerId)
         val newList = o.getList(CONTACTS)
             .asReversed()
             .filter { it == null || (!(it is String) )}
             .map {
                 it as String
             }
             .toList()
        callback(newList)
    }

    override fun cacheContactByNet(contact_list_id: String) {
            val o = AVObject.createWithoutData(CONTACT,contact_list_id)
            val q = AVQuery<AVObject>(CONTACT)
            q.whereEqualTo("objectId",contact_list_id)
            q.findInBackground(object :FindCallback<AVObject>(){
                override fun done(p0: MutableList<AVObject>?, e: AVException?) {
                    if (e==null&&p0!=null&&p0.size>0){
                        val list = p0[0].getList(CONTACTS)
                        list?.forEach {
                            if (it is String){
                                val (id,name) = it.divide()
                                val o = AVObject.createWithoutData("_User",id)
                                o.fetchInBackground(object :GetCallback<AVObject>(){
                                    override fun done(p0: AVObject?, p1: AVException?) {
                                        if (p1==null&&p0!=null){
                                            val nickname = p0.getString(USER_NAME)
                                            val contact = Contact(id,nickname,name,CoreChat.userId!!)
                                            contactRespository.addContact(contact)
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

    private fun String.divide():Pair<String,String>{
        val list = this.split("-")
        if (list.size==2){
            return list[0] to list[1]
        }else{
            throw Throwable("params is not two")
        }
    }
}