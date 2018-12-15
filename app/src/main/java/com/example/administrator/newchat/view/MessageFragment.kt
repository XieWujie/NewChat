package com.example.administrator.newchat.view

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.administrator.newchat.R
import com.example.administrator.newchat.adapter.MessageListAdapter
import com.example.administrator.newchat.custom.RcDecoration
import com.example.administrator.newchat.databinding.MessageListLayoutBinding
import com.example.administrator.newchat.utilities.ViewModelFactoryUtil
import com.example.administrator.newchat.viewmodel.MessageModel
import com.google.android.material.snackbar.Snackbar

class MessageFragment:Fragment(){

    private lateinit var binding: MessageListLayoutBinding
    private lateinit var model: MessageModel
    private val adapter = MessageListAdapter()
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.message_list_layout,container,false)
        val factory = ViewModelFactoryUtil.getChatModelFactory(requireContext())
        model = ViewModelProviders.of(this,factory).get(MessageModel::class.java)
        binding.setLifecycleOwner(this)
        initUI()
        return binding.root
    }

    private fun initUI(){
        val recyclerView = binding.messageRc
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(RcDecoration())
        model.newMessage.observe(this, Observer {
            if (it!=null){
                adapter.submitList(it)
            }
        })
    }
}
