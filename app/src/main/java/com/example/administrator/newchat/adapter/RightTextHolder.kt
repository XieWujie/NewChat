package com.example.administrator.newchat.adapter


import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightLayoutTextBinding

class RightTextHolder(val binding:RightLayoutTextBinding):BaseHolder(binding.root){

    override fun bind(any: Any) {
        if (any is Message){
            binding.message = any
        }
    }
}