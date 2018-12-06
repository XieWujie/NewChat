package com.example.administrator.newchat.utilities

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.AddDialogLayoutBinding

class UserHomePresenter(val o:AVObject,val nav: NavController){

    private var dialog:Dialog? = null
    val username = o.getString(USER_NAME)

    fun add(view:View){
        createDialog(view)
    }

    fun sendMessage(view: View){

    }

    private fun createDialog(view: View){
        if (dialog == null) {
            val context = view.context
            dialog = Dialog(context, R.style.DialogTheme)
            val binding = AddDialogLayoutBinding.inflate(LayoutInflater.from(context), null)
            binding.helper = AddDialogHelper(o,nav,dialog!!)
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