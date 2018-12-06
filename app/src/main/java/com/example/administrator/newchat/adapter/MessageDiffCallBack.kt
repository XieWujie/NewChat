package com.example.administrator.newchat.adapter

import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.newchat.data.message.Message

class MessageDiffCallBack:DiffUtil.ItemCallback<Message>(){
    override fun areItemsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem.message == newItem.message
    }

    override fun areContentsTheSame(oldItem: Message, newItem: Message): Boolean {
        return oldItem == newItem
    }


}