package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository

class MessageModel internal constructor (private val messageRepository: MessageRepository):ViewModel(){

    private val config =  PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .build()

    val newMessage = LivePagedListBuilder<Int,Message>(messageRepository.getNewMessage(CoreChat.userId!!),config).build()

    fun getMessage(id:String) = LivePagedListBuilder<Int,Message>(messageRepository.queryById(id),config)
        .build()
}