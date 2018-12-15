package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository

class MessageModel internal constructor (private val messageRepository: MessageRepository):ViewModel(){


     var ownerId:String? = null

    init {
        ownerId = CoreChat.userId
    }
    private val config =  PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .build()

    val newMessage = LivePagedListBuilder<Int,Message>(messageRepository.getNewMessage(ownerId!!),config).build()

    fun getMessage(id:String) = LivePagedListBuilder<Int,Message>(messageRepository.queryById(id,ownerId!!),config)
        .build()
}