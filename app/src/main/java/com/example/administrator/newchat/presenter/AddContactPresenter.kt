package com.example.administrator.newchat.presenter

import com.avos.avoscloud.AVObject
import com.avos.avoscloud.AVQuery
import com.avos.avoscloud.FindCallback
import com.example.administrator.newchat.utilities.USER_NAME


class AddContactPresenter{

    val query = AVQuery<AVObject>("_User")

    fun getUserIdByNet(key:String,callback: FindCallback<AVObject>){
        query.whereContains(USER_NAME,key)
        query.findInBackground(callback)
    }
}