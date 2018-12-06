package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.administrator.newchat.data.user.UserRepository

class UserModelFactory(private val userRepository: UserRepository):ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainModel(userRepository) as T
    }
}