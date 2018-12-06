package com.example.administrator.newchat.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import androidx.navigation.fragment.findNavController
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.R

import com.example.administrator.newchat.databinding.FragmentUserMessageBinding
import com.example.administrator.newchat.utilities.USER_NAME
import com.example.administrator.newchat.utilities.UserHomePresenter


class UserMessageFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserMessageBinding.inflate(inflater,container,false)
        val o = arguments?.getParcelable<AVObject>("user")
        if (o is AVObject){
            binding.presenter = UserHomePresenter(o,findNavController())
        }
        return binding.root
    }
}
