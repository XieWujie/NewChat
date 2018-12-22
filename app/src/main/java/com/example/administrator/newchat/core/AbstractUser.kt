package com.example.administrator.newchat.core

import com.avos.avoscloud.AVException
import com.avos.avoscloud.im.v2.AVIMClient
import com.example.administrator.newchat.data.user.User


interface AbstractUser{

   fun register(username:String,password:String,mailBox:String,callback:(any:Any,client:AVIMClient?)->Unit)

    fun login(username: String,password: String,callback: (any:Any,client: AVIMClient?) -> Unit)

    fun logout(user: User)

    fun setAvatar(path:String)

}