package com.example.administrator.newchat.viewmodel

import androidx.lifecycle.ViewModel
import com.example.administrator.newchat.data.user.UserRepository

class MainModel(
   private val userRepository: UserRepository
):ViewModel(){

    val lastUser = userRepository.getLastUser()
    fun getUserById(id:String) = userRepository.getUserById(id)
}