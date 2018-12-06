package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageRepository

class ChatModel internal constructor (private val messageRepository: MessageRepository):ViewModel(){

    fun getMessage(id:String) = LivePagedListBuilder<Int,Message>(messageRepository.queryById(id), PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .build())
        .build()
}