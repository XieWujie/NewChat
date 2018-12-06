package com.example.administrator.newchat.core

import com.avos.avoscloud.AVException
import com.example.administrator.newchat.data.user.User


interface AbstractUser{

   fun register(username:String,password:String,mailBox:String,callback:(any:Any)->Unit)

    fun login(username: String,password: String,callback: (any:Any) -> Unit)

    fun logout(user: User)
}