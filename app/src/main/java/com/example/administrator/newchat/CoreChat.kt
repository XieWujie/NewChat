package com.example.administrator.newchat

import android.content.Context
import com.avos.avoscloud.AVException
import com.avos.avoscloud.im.v2.AVIMClient
import com.example.administrator.newchat.core.*
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.data.user.UserRepository
import com.example.administrator.newchat.utilities.IMAGE_MESSAGE


object CoreChat{

    var userId:String? = null
    var owner:User? = null
    var client:AVIMClient? = null
    private var appDatabase:AppDatabase? = null
    private var contacts:AbstractContacts? = null
    private var abstractUser:AbstractUser? = null
    private var abstractMessage:AbstractMessage? = null

    fun init(context: Context){
        appDatabase = AppDatabase.getInstance(context)
        abstractUser = UserManager(UserRepository.getInstance(appDatabase!!.getUserDao()))
    }

    fun addContactManage(manage: AbstractContacts){
        this.contacts = manage
    }

    fun addUserManage(userManager: AbstractUser){
        this.abstractUser = userManager
    }

    fun addMessageManage(messageManage: AbstractMessage){
        this.abstractMessage = messageManage
    }

    fun loginByPassword(userName:String,password:String,callback: (e:Any) -> Unit){
        check()
        abstractUser!!.login(userName,password){
            user,client->
            when(user){
                is User->{
                    init(user,client!!)
                }
            }
            callback(user)
        }
    }

   private fun init(user:User,client:AVIMClient){
        userId = user.userId
        owner = user
        this.client = client
       abstractMessage = MessageManage(MessageRepository.getInstance(appDatabase!!.getMessageDao()),client,user)
       contacts = ContactsManage(ContactRespository.getInstance(appDatabase!!.getContactsDao()),client,user.contactListId,user)
    }

    fun register(userName: String,password: String,mailBox:String,callback: (e: Any) -> Unit){
        check()
        abstractUser!!.register(userName,password,mailBox){
            user,client->
            when(user) {
                is User -> {
                    init(user, client!!)
                }
            }
            callback(user)
        }
    }

    fun initContactByNet(contact_list_id:String){
        contacts!!.cacheContactByNet()
    }

    private fun check(){
        if (abstractUser==null){
            throw Throwable("have not init data")
        }
    }

    fun addContact(contact:Contact){
        contacts?.addContact(contact)
    }

    fun logout(){
        check()
        owner?.isLogout = true
        abstractUser!!.logout(owner!!)
    }

    fun sendText(message:Message){
        abstractMessage?.sendMessage(message)
    }

    fun sendImage(path:String,conversationId: String,conversationName:String){
        val message = Message("",conversationId,path,conversationName, IMAGE_MESSAGE, userId!!,1,0,"")
        abstractMessage?.sendMessage(message)
    }

    fun cacheMessage(message: Message){
        abstractMessage?.cacheMessage(message)
    }

    fun queryMessageByConversationId(id:String){
        abstractMessage?.queryMessageByConversationId(id)
    }

    fun queryMessageByTime(id: String,timeStamp:Long){
        abstractMessage?.queryMessageByTime(id,timeStamp)
    }

    fun findConversationId(contactId:String,name:String, callback:(conversationId:String)->Unit){
        contacts?.findConversationId(listOf(contactId),name,callback)
    }
}