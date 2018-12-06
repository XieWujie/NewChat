package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.administrator.newchat.data.message.MessageRepository

class ChatModelFactory(private val messageRepository: MessageRepository):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ChatModel(messageRepository) as T
    }
}