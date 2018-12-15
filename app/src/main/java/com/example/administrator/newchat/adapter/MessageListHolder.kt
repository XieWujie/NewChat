package com.example.administrator.newchat.adapter

import android.content.Intent
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.MessageItemLayoutBinding
import com.example.administrator.newchat.utilities.CONVERSATION_ID
import com.example.administrator.newchat.utilities.CONVERSATION__NAME
import com.example.administrator.newchat.utilities.IMAGE_MESSAGE
import com.example.administrator.newchat.view.ChatActivity

class MessageListHolder(val binding:MessageItemLayoutBinding):BaseHolder(binding.root){

    override fun bind(any: Any) {
        if (any is Message){
            binding.message = any
            if (any.type == IMAGE_MESSAGE){
                binding.contentText.text = "图片"
            }
            binding.root.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context,ChatActivity::class.java)
                intent.putExtra(CONVERSATION_ID,any.conversationId)
                intent.putExtra(CONVERSATION__NAME,any.name)
                context.startActivity(intent)
            }
        }
    }
}