package com.example.administrator.newchat

import com.avos.avoscloud.AVException
import com.example.administrator.newchat.core.AbstractContacts
import com.example.administrator.newchat.core.AbstractMessage
import com.example.administrator.newchat.core.AbstractUser
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.user.User


object CoreChat{

    var userId:String? = null
    var owner:User? = null
    private var contacts:AbstractContacts? = null
    private var abstractUser:AbstractUser? = null
    private var abstractMessage:AbstractMessage? = null

    fun init(abstractUser: AbstractUser,abstractContacts: AbstractContacts,abstractMessage: AbstractMessage){
        contacts = abstractContacts
        this.abstractUser = abstractUser
        this.abstractMessage = abstractMessage
    }

    fun init(user:User){
        userId = user.userId
        owner = user
    }

    fun loginByPassword(userName:String,password:String,callback: (e:Any) -> Unit){
        check()
        abstractUser!!.login(userName,password){
            when(it){
                is User->{
                    userId = it.userId
                    owner = it
                    initContactByNet(it.contactListId)
                    callback(it)
                }
                is AVException->{
                    callback(it)
                }
            }
        }
    }

    fun register(userName: String,password: String,mailBox:String,callback: (e: Any) -> Unit){
        check()
        abstractUser!!.register(userName,password,mailBox){
            when(it){
                is User->{
                    userId = it.userId
                    owner = it
                    initContactByNet(it.contactListId)
                    callback(it)
                }
                is AVException->{
                    callback(it)
                }
            }
        }
    }

    fun initContactByNet(contact_list_id:String){
        contacts!!.cacheContactByNet(contact_list_id)
    }

    private fun check(){
        if (abstractUser==null){
            throw Throwable("have not init data")
        }
    }

    fun addContact(contact: Contact){
        contacts?.addContact(owner!!.contactListId,contact)
    }

    fun logout(){
        check()
        owner!!.isLogout = true
        abstractUser!!.logout(owner!!)
    }

    fun sendText(message:Message){
        abstractMessage?.sendMessage(message)
    }

    fun cacheMessage(message: Message){
        abstractMessage?.cacheMessage(message)
    }
}