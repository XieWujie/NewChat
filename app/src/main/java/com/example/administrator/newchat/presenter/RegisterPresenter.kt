package com.example.administrator.newchat.presenter

import android.app.Dialog
import android.content.Intent
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable

import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.utilities.ChatUtil
import com.example.administrator.newchat.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.Exception


data class RegisterPresenter(
    @Bindable var userName:String = "",
    @Bindable var firstPassword:String = "",
    @Bindable var secondPassword:String = "",
    @Bindable var mailBox:String = ""
):BaseObservable(){

    private var progressDialog:Dialog? = null

    fun register(view: View){
        if (checkPasswordSame(view)&&checkMailBox(view)){
            if (progressDialog == null){
                progressDialog = ChatUtil.createProgressDialog(view.context)
            }
            progressDialog?.show()
            view.isClickable = false
            CoreChat.register(userName,firstPassword,mailBox){
                progressDialog?.dismiss()
                view.isClickable = true
                if (it is Exception){
                    Snackbar.make(view,it.message as CharSequence,Snackbar.LENGTH_LONG).show()
                }else if (it is User){
                    val intent = Intent(view.context,MainActivity::class.java)
                    view.context.startActivity(intent)
                }
            }
        }
    }

    private fun firstLogin(view: View){
        CoreChat.loginByPassword(userName,firstPassword){
            if (it is Exception){
                Snackbar.make(view,it.message as CharSequence,Snackbar.LENGTH_LONG).show()
            }else{
                val intent = Intent(view.context,MainActivity::class.java)
                view.context.startActivity(intent)
            }
        }
    }

    private fun checkPasswordSame(view: View):Boolean{
        if (firstPassword != secondPassword){
            Snackbar.make(view,"两次密码不一样",Snackbar.LENGTH_LONG).show()
            return false
        }
        return true
    }
    private fun checkMailBox(view: View):Boolean{
//        if (mailBox.isEmpty()){
//            return false
//        }
        return true
    }
}