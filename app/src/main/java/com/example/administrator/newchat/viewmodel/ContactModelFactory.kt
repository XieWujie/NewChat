package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.administrator.newchat.data.contacts.ContactRespository

class ContactModelFactory(private val respository: ContactRespository):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ContactModel(respository) as T
    }
}