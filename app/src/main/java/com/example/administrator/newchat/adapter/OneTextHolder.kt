package com.example.administrator.newchat.adapter

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.room.Query
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.QueryItemLayoutBinding
import com.example.administrator.newchat.utilities.USER_NAME

class OneTextHolder(val binding:QueryItemLayoutBinding):BaseHolder(binding.root){

    override fun bind(any: Any) {
        if (any is AVObject){
            val q = any.get(USER_NAME) as String
            binding.newText = q
            binding.root.setOnClickListener {
                val bundle = Bundle()
                bundle.putParcelable("user",any)
                it.findNavController().navigate(R.id.action_addContactFragment_to_userMessageFragment,bundle)
            }
        }
    }
}