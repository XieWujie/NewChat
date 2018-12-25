package com.example.administrator.newchat.adapter


import android.view.View
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightLayoutTextBinding
import com.example.administrator.newchat.utilities.SENDING
import com.example.administrator.newchat.utilities.SEND_FAIL
import com.example.administrator.newchat.utilities.SEND_SUCCEED

class RightTextHolder(val bind:RightLayoutTextBinding):BaseHolder(bind.root){

    override fun bind(any: Any) {
        if (any is Message){
            when(any.sendState){
                SENDING ->{
                    bind.chatRightProgressbar.visibility = View.VISIBLE
                    bind.chatRightTvError.visibility = View.GONE
                }
                SEND_FAIL ->{
                    bind.chatRightProgressbar.visibility = View.GONE
                    bind.chatRightTvError.visibility = View.VISIBLE
                    bind.chatRightTvError.setOnClickListener {
                        resendEvent(any)
                        it.visibility = View.GONE
                        bind.chatRightProgressbar.visibility = View.VISIBLE
                    }
                }
                SEND_SUCCEED ->{
                    bind.chatRightProgressbar.visibility = View.GONE
                    bind.chatRightTvError.visibility = View.GONE
                }
            }
            bind.message = any
        }
    }

    private fun resendEvent(message: Message){
        CoreChat.sendMessage(message)
    }
}