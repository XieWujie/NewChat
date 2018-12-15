package com.example.administrator.newchat.utilities

import android.app.Dialog
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.navigation.NavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.contacts.Contact

class AddDialogHelper(val o:AVObject,val  navController: NavController,val dialog: Dialog):BaseObservable(){

    @Bindable var markName:String = ""

    fun send(view:View){
        convertObjectToContact(o){
            CoreChat.addContact(it)
        }
        navController.navigate(R.id.action_userMessageFragment_to_contactFragment)
        dialog.dismiss()
    }

    fun cancel(view: View){

    }

    private fun convertObjectToContact(o:AVObject,callback:(contact:Contact)->Unit){
        val name = o.getString(USER_NAME)
        val id = o.objectId
        CoreChat.findConversationId(id,markName){
            callback(Contact(id,markName,name,it,CoreChat.userId!!))
        }
    }
}