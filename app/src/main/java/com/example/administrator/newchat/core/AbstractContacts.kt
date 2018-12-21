package com.example.administrator.newchat.core

import androidx.lifecycle.LiveData
import com.avos.avoscloud.im.v2.AVIMClient
import com.example.administrator.newchat.data.contacts.Contact

interface AbstractContacts{

    fun addContact(contact: Contact)

    fun addContactById(id: String,markName: String,conversationId: String)

    fun addContactById(id:String,markName: String)

    fun removeContact(contact: Contact)

    fun markContactName(contactId:String,markName:String)

    fun cacheContactByNet()

    fun findConversationId(contactIds: List<String>, callback: (conversationId: String) -> Unit)

}