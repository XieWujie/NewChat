package com.example.administrator.newchat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.RegisterLayoutBinding
import com.example.administrator.newchat.utilities.RegisterHelper

class RegisterFragment:Fragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = DataBindingUtil.inflate<RegisterLayoutBinding>(inflater, R.layout.register_layout,container,false)
        val registerHelper = RegisterHelper()
        binding.registerhelper = registerHelper
        return binding.root
    }
}