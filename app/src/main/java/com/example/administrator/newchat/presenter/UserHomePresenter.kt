package com.example.administrator.newchat.presenter

import android.app.Dialog
import android.content.Intent
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.navigation.NavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.AddDialogLayoutBinding
import com.example.administrator.newchat.utilities.*
import com.example.administrator.newchat.view.ChatActivity

class UserHomePresenter(val o:AVObject,val nav: NavController){

    private var dialog:Dialog? = null
    val username = o.getString(USER_NAME)
    val avatar = o.getString(AVATAR)

    fun add(view:View){
        createDialog(view)
    }

    fun sendMessage(view: View){
        val context = view.context
        val intent = Intent(context,ChatActivity::class.java)
        CoreChat.findConversationId(o.objectId){
            intent.putExtra(CONVERSATION__NAME,username)
            intent.putExtra(CONVERSATION_ID,it)
            context.startActivity(intent)
        }
    }

    private fun createDialog(view: View){
        if (dialog == null) {
            val context = view.context
            dialog = Dialog(context, R.style.DialogTheme)
            val binding = AddDialogLayoutBinding.inflate(LayoutInflater.from(context), null)
            binding.helper = AddDialogHelper(o, nav, dialog!!)
            val view = binding.root
            dialog?.setContentView(view)
            val window = dialog?.window
            window?.setGravity(Gravity.BOTTOM)
            val layoutParams = window?.attributes
            val displayMetrics = view.resources.displayMetrics
            layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
            window?.decorView?.setPadding(0, 0, 0, 0)
            layoutParams?.height = displayMetrics.heightPixels / 2
            window?.attributes = layoutParams
        }
        dialog?.show()
    }
}