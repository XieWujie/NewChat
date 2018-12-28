package com.example.administrator.newchat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.avos.avoscloud.AVObject

import com.example.administrator.newchat.databinding.FragmentUserMessageBinding
import com.example.administrator.newchat.presenter.UserHomePresenter


class UserMessageFragment : BaseFragment(){

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val binding = FragmentUserMessageBinding.inflate(inflater,container,false)
        val o = arguments?.getParcelable<AVObject>("user")
        if (o is AVObject){
            binding.presenter = UserHomePresenter(o, findNavController())
        }
        return binding.root
    }

}
