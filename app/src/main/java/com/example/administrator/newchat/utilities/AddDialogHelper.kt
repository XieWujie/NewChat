package com.example.administrator.newchat.utilities

import android.app.Dialog
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.navigation.NavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.data.message.Message
import com.google.android.material.snackbar.Snackbar

class AddDialogHelper(val o:AVObject,val  navController: NavController,val dialog: Dialog):BaseObservable(){

    @Bindable var markName:String = ""

    fun send(view:View){
        CoreChat.findConversationId(o.objectId){
            val ownerId = CoreChat.userId!!
            val message = Message("",it,VerifyMessage.REQUEST.toString(),o.getString(USER_NAME),
                VERIFY_MESSAGE,ownerId,1,0,ownerId, o.getString(
                AVATAR))
            CoreChat.sendMessage(message)
        }
        navController.navigate(R.id.action_userMessageFragment_to_contactFragment)
        Snackbar.make(view,"已发送",Snackbar.LENGTH_LONG).show()
        dialog.dismiss()
    }

    fun cancel(view: View){

    }

}