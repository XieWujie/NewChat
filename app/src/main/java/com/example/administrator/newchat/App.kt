package com.example.administrator.newchat

import android.app.Application
import android.content.Context
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.PushService
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMMessageManager
import com.example.administrator.newchat.core.ContactsManage
import com.example.administrator.newchat.core.MessageHandler
import com.example.administrator.newchat.core.MessageManage
import com.example.administrator.newchat.core.UserManager
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.utilities.APP_ID
import com.example.administrator.newchat.utilities.APP_KEY
import com.example.administrator.newchat.view.MainActivity



class App:Application(){
    override fun onCreate() {
        super.onCreate()
        AVOSCloud.initialize(this, APP_ID, APP_KEY)
        AVOSCloud.setDebugLogEnabled(true)
        PushService.setDefaultPushCallback(this, MainActivity::class.java)
        PushService.setAutoWakeUp(true)
        PushService.setDefaultChannelId(this, "default")
        AVIMMessageManager.registerDefaultMessageHandler(MessageHandler())
        AVIMClient.setMessageQueryCacheEnable(false)
        AVIMClient.setTimeoutInSecs(8)
        AVIMClient.setMessageQueryCacheEnable(false)
        CoreChat.init(this)
    }
}