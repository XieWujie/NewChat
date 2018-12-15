package com.example.administrator.newchat.adapter

import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightLayoutImageBinding

class RightImageHolder(val bind:RightLayoutImageBinding):BaseHolder(bind.root){

    override fun bind(any: Any) {
        if (any is Message){
            bind.message = any
        }
    }
}