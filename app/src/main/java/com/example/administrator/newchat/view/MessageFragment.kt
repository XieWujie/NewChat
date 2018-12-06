package com.example.administrator.newchat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.MessageListLayoutBinding

class MessageFragment:Fragment(){

    private lateinit var binding: MessageListLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.message_list_layout,container,false)
        return binding.root
    }
}
