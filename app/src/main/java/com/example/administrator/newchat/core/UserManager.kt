package com.example.administrator.newchat.core

import com.avos.avoscloud.*
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMClientCallback
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.data.user.UserRepository
import com.example.administrator.newchat.utilities.CONTACT
import com.example.administrator.newchat.utilities.CONTACTS_LIST_ID
import java.util.*


class UserManager(private val repository: UserRepository):AbstractUser{

    override fun register(username:String,password:String,mailBox:String,callback:(any:Any,client:AVIMClient?)->Unit) {
        val user = AVUser()
        user.username = username
        user.setPassword(password)
        user.email = mailBox
        user.signUpInBackground(object : SignUpCallback(){
            override fun done(e: AVException?) {
                if (e!=null){
                    callback(e,null)
                }else{
                    val c = AVObject.create(CONTACT)
                    c.saveInBackground(object :SaveCallback(){
                        override fun done(p0: AVException?) {
                            if (p0==null){
                               login(username,password,c.objectId,callback)
                            }else{
                                callback(p0,null)
                                p0.printStackTrace()
                            }
                        }
                    })
                }
            }

        })
    }

    private fun login(username: String, password: String,contactListId:String,callback: (any:Any,client:AVIMClient?) -> Unit){
        AVUser.logInInBackground(username,password,object :LogInCallback<AVUser>(){

            override fun done(a: AVUser?, p1: AVException?) {
                if (p1==null&&a!=null){
                    a.put(CONTACTS_LIST_ID,contactListId)
                    a.saveInBackground(object :SaveCallback(){
                        override fun done(e: AVException?) {
                            if (e==null){
                                val client = AVIMClient.getInstance(a)
                                client.open(object :AVIMClientCallback(){
                                    override fun done(p0: AVIMClient?, p1: AVIMException?) {
                                        if (p1==null){
                                            val user = User(a.objectId,username,password,Date().time,false,contactListId)
                                            repository.addUser(user)
                                            callback(user,p0)
                                        }
                                    }
                                })
                            }else{
                                callback(e,null)
                            }
                        }

                    })
                }else{
                    callback(p1!!,null)
                }
            }
        })
    }

    override fun login(username: String,password: String,callback: (any:Any,client:AVIMClient?) -> Unit) {
        AVUser.logInInBackground(username,password,object :LogInCallback<AVUser>(){
            override fun done(avUser: AVUser?, p1: AVException?) {
                if (p1==null&&avUser!=null){
                    val client = AVIMClient.getInstance(avUser.objectId)
                    client.open(object :AVIMClientCallback(){
                        override fun done(c: AVIMClient?, e: AVIMException?) {
                            if (e==null){
                                val user = User(avUser.objectId,username,password,Date().time,false,avUser.getString(
                                    CONTACTS_LIST_ID))
                                repository.addUser(user)
                                callback(user,c)
                            }else{
                                e.printStackTrace()
                                callback(e,null)
                            }
                        }
                    })

                }else{
                    callback(p1!!,null)
                }
            }

        })
    }

   override fun logout(user: User){
        repository.updata(user)
    }
}