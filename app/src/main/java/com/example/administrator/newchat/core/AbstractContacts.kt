package com.example.administrator.newchat.core

import androidx.lifecycle.LiveData
import com.example.administrator.newchat.data.contacts.Contact

interface AbstractContacts{

    fun getAllContactsId(contact_list_id:String,callback:(contact:List<String>)->Unit)

    fun addContact(contact_list_id: String,contact: Contact)

    fun removeContact(contact_list_id: String,contact: Contact)

    fun markContactName(contact_list_id: String,contactId:String,markName:String)

    fun cacheContactByNet(contact_list_id: String)
}