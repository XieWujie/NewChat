package com.example.administrator.newchat.adapter

import android.util.Log
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.LeftLayoutTextBinding



class LeftTextHolder(val binding: LeftLayoutTextBinding):BaseHolder(binding.root){

    override fun bind(any: Any) {
        if (any is Message){
            binding.message = any
        }
    }
}