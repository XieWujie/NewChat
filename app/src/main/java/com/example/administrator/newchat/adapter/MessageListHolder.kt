package com.example.administrator.newchat.adapter

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.VerifyMessage
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.MessageItemLayoutBinding
import com.example.administrator.newchat.utilities.*
import com.example.administrator.newchat.view.ChatActivity

class MessageListHolder(val binding:MessageItemLayoutBinding):BaseHolder(binding.root){

    override fun bind(any: Any) {
        if (any is Message){
            binding.message = any
            val t = binding.contentText
            when(any.type){
                IMAGE_MESSAGE ->t.text = "图片"
                TEXT_MESSAGE ->t.text = any.message
                VERIFY_MESSAGE->{
                    when(any.message){
                        VerifyMessage.AGREE->{
                            binding.contentText.text = "已同意好友请求"
                        }
                        VerifyMessage.REQUEST->{
                            binding.contentText.text = "请求添加好友"
                            if (any.unReadCount<0) {
                                binding.root.setOnClickListener {
                                    createAddContactDialog(any, binding.root.context)
                                }
                                return
                            }
                        }
                    }
                }
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

    private fun createAddContactDialog(message: Message,context: Context){
        val dialog = AlertDialog.Builder(context)
            .setTitle("${message.message}\n是否同意？")
            .setPositiveButton("同意"){ a,b->
                CoreChat.addContactById(message.from,message.name)
                val message = message.copy(message = VerifyMessage.AGREE.toString())
                CoreChat.sendMessage(message.name,message.conversationId, VERIFY_MESSAGE,VerifyMessage.AGREE){
                    if (it == null){
                        CoreChat.deleteMessage(message)
                    }
                }
                a.dismiss()
            }
            .setNegativeButton("不同意"){ a,b->
                a.dismiss()
            }
            .show()
    }
}