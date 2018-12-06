package com.example.administrator.newchat.utilities

import android.content.Context
import com.example.administrator.newchat.data.AppDatabase
import com.example.administrator.newchat.data.contacts.ContactRespository
import com.example.administrator.newchat.data.message.MessageRepository
import com.example.administrator.newchat.data.user.UserRepository
import com.example.administrator.newchat.viewmodel.ChatModelFactory
import com.example.administrator.newchat.viewmodel.ContactModelFactory
import com.example.administrator.newchat.viewmodel.UserModelFactory

object ViewModelFactoryUtil{

    fun getMainModelFactory(context: Context) =
        UserModelFactory(UserRepository.getInstance(getDatabase(context).getUserDao()))

    fun getContactModelFactory(context: Context) =
            ContactModelFactory(ContactRespository.getInstance(getDatabase(context).getContactsDao()))

    private fun getDatabase(context: Context) = AppDatabase.getInstance(context)

    fun getChatModelFactory(context: Context) = ChatModelFactory(MessageRepository.getInstance(getDatabase(context).getMessageDao()))

}