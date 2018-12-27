package com.example.administrator.newchat.adapter

import android.app.Dialog
import android.content.Context
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightLayoutImageBinding
import com.example.administrator.newchat.utilities.SENDING
import com.example.administrator.newchat.utilities.SEND_FAIL
import com.example.administrator.newchat.utilities.SEND_SUCCEED
import kotlin.math.abs

class RightImageHolder(val bind:RightLayoutImageBinding):BaseHolder(bind.root){

    override fun bind(any: Any) {
        if (any is Message){
            when(any.sendState){
            SENDING->{
                bind.chatRightProgressbar.visibility = View.VISIBLE
                bind.chatRightTvError.visibility = View.GONE
            }
            SEND_FAIL->{
                bind.chatRightProgressbar.visibility = View.GONE
                bind.chatRightTvError.visibility = View.VISIBLE
                bind.chatRightTvError.setOnClickListener {
                    resendEvent(any)
                    it.visibility = View.GONE
                    bind.chatRightProgressbar.visibility = View.VISIBLE
                }
            }
            SEND_SUCCEED->{
                bind.chatRightProgressbar.visibility = View.GONE
                bind.chatRightTvError.visibility = View.GONE
            }
        }
        bind.message = any
        bind.chatRightLayoutContent.setOnClickListener {
            seeBigImage(any.message)
        }
    }
    }

    private fun resendEvent(message: Message){
        CoreChat.sendMessage(message)
    }

    private fun seeBigImage(src:String){
        val dialog = Dialog(bind.root.context,R.style.bigImage_dialog_style)
        val p = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT)
        val view = ImageView(bind.root.context)
        Glide.with(bind.root).load(src).into(view)
        dialog.setContentView(view,p)
        val window = dialog?.window
        window?.setGravity(Gravity.CENTER)
        val layoutParams = window?.attributes
        val displayMetrics = bind.root.resources.displayMetrics
        layoutParams?.width = WindowManager.LayoutParams.MATCH_PARENT
        window?.decorView?.setPadding(0, 0, 0, 0)
        layoutParams?.height = displayMetrics.heightPixels
        window?.attributes = layoutParams
        dialog.show()
        view.setOnClickListener {
            dialog.dismiss()
        }
    }
}