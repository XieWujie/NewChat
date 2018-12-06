package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactRespository

class ContactModel internal constructor(private val respository: ContactRespository):ViewModel(){


    fun getAllContacts(ownerId:String) = LivePagedListBuilder<Int,Contact>(respository.getAllContacts(ownerId), PagedList.Config.Builder()
        .setPageSize(20)
        .setEnablePlaceholders(false)
        .setInitialLoadSizeHint(20)
        .build())
        .build()

}