package com.example.administrator.newchat.adapter

import android.view.View
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightLayoutImageBinding
import com.example.administrator.newchat.utilities.SENDING

class RightImageHolder(val bind:RightLayoutImageBinding):BaseHolder(bind.root){

    override fun bind(any: Any) {
        if (any is Message){
            when(any.sendState){
                SENDING->{
                    bind.chatRightProgressbar.visibility = View.VISIBLE
                    bind.chatRightTvError.visibility = View.GONE
                }
            }
            bind.message = any
        }
    }
}