package com.example.administrator.newchat

import android.app.Application
import com.avos.avoscloud.AVOSCloud
import com.avos.avoscloud.PushService
import com.avos.avoscloud.im.v2.AVIMClient
import com.avos.avoscloud.im.v2.AVIMClientEventHandler
import com.avos.avoscloud.im.v2.AVIMMessageManager
import com.example.administrator.newchat.core.MessageHandler
import com.example.administrator.newchat.custom.VerifyMessage
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
        AVIMMessageManager.registerAVIMMessageType(VerifyMessage::class.java)
        CoreChat.init(this)
        AVIMClient.setClientEventHandler(object :AVIMClientEventHandler(){
            override fun onConnectionResume(p0: AVIMClient?) {

            }

            override fun onConnectionPaused(p0: AVIMClient?) {

            }

            override fun onClientOffline(p0: AVIMClient?, p1: Int) {

            }

        })
    }
}