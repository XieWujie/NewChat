package com.example.administrator.newchat.core

import com.example.administrator.newchat.data.message.Message

interface AbstractMessage{

    fun sendMessage(message:Message)

    fun cacheMessage(message: Message)
}