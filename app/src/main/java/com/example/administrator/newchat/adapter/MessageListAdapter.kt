package com.example.administrator.newchat.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.MessageItemLayoutBinding


class MessageListAdapter:PagedListAdapter<Message,BaseHolder>(MessageDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = MessageItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return MessageListHolder(binding)
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = getItem(position)
        if (item==null){
            return
        }
        holder.bind(item)
    }

}