package com.example.administrator.newchat.core

import android.content.Context
import com.avos.avoscloud.*
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.data.user.UserRepository
import com.example.administrator.newchat.utilities.CONTACT
import com.example.administrator.newchat.utilities.CONTACTS_LIST_ID
import java.util.*


class UserManager(context: Context):AbstractUser{

    lateinit var userRepository: UserRepository

    init {
        val database = AppDatabase.getInstance(context)
        userRepository = UserRepository.getInstance(database.getUserDao())
    }

    override fun register(username:String,password:String,mailBox:String,callback:(any:Any)->Unit) {
        val user = AVUser()
        user.username = username
        user.setPassword(password)
        user.email = mailBox
        user.signUpInBackground(object : SignUpCallback(){
            override fun done(e: AVException?) {
                if (e!=null){
                    callback(e)
                }else{
                    val c = AVObject.create(CONTACT)
                    c.saveInBackground(object :SaveCallback(){
                        override fun done(p0: AVException?) {
                            if (p0==null){
                               login(username,password,c.objectId,callback)
                            }else{
                                callback(p0)
                                p0.printStackTrace()
                            }
                        }
                    })
                }
            }

        })
    }

    private fun login(username: String, password: String,contactListId:String,callback: (any:Any) -> Unit){
        AVUser.logInInBackground(username,password,object :LogInCallback<AVUser>(){

            override fun done(p0: AVUser?, p1: AVException?) {
                if (p1==null&&p0!=null){
                    p0.put(CONTACTS_LIST_ID,contactListId)
                    p0.saveInBackground(object :SaveCallback(){
                        override fun done(e: AVException?) {
                            if (e==null){
                                val user = User(p0.objectId,username,password,Date().time.toInt(),false,contactListId)
                                userRepository.deleteAlluser()
                                userRepository.addUser(user)
                                callback(user)
                            }else{
                                callback(e)
                            }
                        }

                    })
                }else{
                    callback(p1!!)
                }
            }
        })
    }

    override fun login(username: String,password: String,callback: (any:Any) -> Unit) {
        AVUser.logInInBackground(username,password,object :LogInCallback<AVUser>(){
            override fun done(avUser: AVUser?, p1: AVException?) {
                if (p1==null&&avUser!=null){
                    val user = User(avUser.objectId,username,password,Date().time.toInt(),false,avUser.getString(
                        CONTACTS_LIST_ID))
                    userRepository.addUser(user)
                    callback(user)
                }else{
                    callback(p1!!)
                }
            }

        })
    }

   override fun logout(user: User){
        userRepository.updata(user)
    }
}