package com.example.administrator.newchat

import android.content.Context
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.avos.avoscloud.im.v2.callback.AVIMConversationCallback
import com.example.administrator.newchat.core.*
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.data.user.UserRepository
import com.example.administrator.newchat.utilities.SENDING
import com.example.administrator.newchat.utilities.TempCountGet
import com.example.administrator.newchat.utilities.VERIFY_MESSAGE
import java.util.*


object CoreChat{

    var userId:String? = null
    var owner:User? = null
    var client:AVIMClient? = null
    private var appDatabase:AppDatabase? = null
    private var contacts:AbstractContacts? = null
    private var abstractUser:AbstractUser? = null
    private var abstractMessage:AbstractMessage? = null
    private var counter:TempCountGet? = null

    fun init(context: Context){
        counter = TempCountGet(context)
        appDatabase = AppDatabase.getInstance(context)
        abstractUser = UserManager(UserRepository.getInstance(appDatabase!!.getUserDao()))
    }

    fun addContactManage(manage: AbstractContacts){
        this.contacts = manage
    }

    fun addUserManage(userManager: AbstractUser){
        this.abstractUser = userManager
    }

    fun addContact(contact:Contact){
        contacts?.addContact(contact)
    }

    fun addMessageManage(messageManage: AbstractMessage){
        this.abstractMessage = messageManage
    }

    fun getTempMessageId() = counter?.get()?:"0"

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

    fun loginWithoutNet(user: User,loginCallback:()->Unit){
        owner = user
        userId = user.userId
        client = AVIMClient.getInstance(user.userId)
        init(user, client!!)
        client!!.open(object :AVIMClientCallback(){
            override fun done(e: AVIMClient?, p1: AVIMException?) {
                if (e == null){
                    loginCallback()
                }
            }
        })
    }

    fun findConversation(name: String,conversationId: String,avatar: String?,findCallback:(c:AVIMConversation)->Unit){
        contacts?.findConversation(conversationId,name,avatar,findCallback)
    }

    fun finConversationById(conversationId: String,getCallback:(c:AVIMConversation)->Unit){
       val conversation =  client?.getConversation(conversationId)
        if (conversation == null)return
        if (conversation?.isShouldFetch){
            conversation?.fetchInfoInBackground(object :AVIMConversationCallback(){
                override fun done(e: AVIMException?) {
                    if (e == null){
                        getCallback(conversation)
                    }
                }
            })
        }else{
            getCallback(conversation)
        }
    }

    private fun check(){
        if (abstractUser==null){
            throw Throwable("have not init data")
        }
    }

    fun logout(){
        check()
        owner?.isLogout = true
        abstractUser!!.logout(owner!!)
    }

    fun addContact(id:String,markName: String,avatar:String?, addCallback:(e:Exception?)->Unit){
        contacts?.findConversation(id,markName,avatar){
            sendMessage(markName,it.conversationId, VERIFY_MESSAGE, VerifyMessage.REQUEST,addCallback)
        }
    }

    fun sendMessage(name:String,conversationId: String,type:Int,content:String,exception: (e:Exception?)->Unit){
      sendMessage(name,conversationId,type,content,0.0,exception)
    }

    fun sendMessage(name:String,conversationId: String,type:Int,content:String,voiceTime:Double,exception: (e:Exception?)->Unit){
        val ownerName = owner?.name!!
        val message = Message(getTempMessageId(),conversationId,name,
            content,ownerName, userId!!,type, voiceTime,0,Date().time, userId!!, SENDING, owner?.avatar)
        abstractMessage?.sendMessage(message.copy(id = getTempMessageId()),exception)
    }

    fun cacheMessage(message: Message,isUpdate:Boolean = false){
        abstractMessage?.cacheMessage(message,isUpdate)
    }

    fun queryMessageByConversationId(id:String,limit:Int){
        abstractMessage?.queryMessageByConversationId(id,limit)
    }

    fun queryMessageByTime(id: String,timeStamp:Long){
        abstractMessage?.queryMessageByTime(id,timeStamp)
    }

    fun findConversationId(contactId:String, callback:(conversationId:String)->Unit){
        contacts?.findConversationId(listOf(contactId),callback)
    }

    fun setAvatar(path:String){
        abstractUser?.setAvatar(path)
    }

    fun addContactById(id:String,markName:String){
        contacts?.addContactById(id,markName)
    }

    fun deleteMessage(message: Message){
        abstractMessage?.deleteMessage(message)
    }

    fun sendMessage(message:Message){
        abstractMessage?.sendMessage(message){

        }
    }
}