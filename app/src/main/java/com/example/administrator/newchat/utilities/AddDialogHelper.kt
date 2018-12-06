package com.example.administrator.newchat.utilities

import android.app.Dialog
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.contacts.Contact

class AddDialogHelper(val o:AVObject,val  navController: NavController,val dialog: Dialog):BaseObservable(){

    @Bindable var markName:String = ""

    fun send(view:View){
        CoreChat.addContact(convertObjectToContact(o))
        navController.navigate(R.id.action_userMessageFragment_to_contactFragment)
        dialog.dismiss()
    }

    fun cancel(view: View){

    }

    private fun convertObjectToContact(o:AVObject):Contact{
        val name = o.getString(USER_NAME)
        val id = o.objectId
        val contact = Contact(id,name,markName,CoreChat.userId!!)
        return contact
    }
}