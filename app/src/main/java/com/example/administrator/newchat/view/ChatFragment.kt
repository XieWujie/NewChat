package com.example.administrator.newchat.view


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

import com.example.administrator.newchat.R
import com.example.administrator.newchat.adapter.ChatAdapter
import com.example.administrator.newchat.databinding.FragmentChatBinding
import com.example.administrator.newchat.utilities.CLIENT_ID
import com.example.administrator.newchat.utilities.ViewModelFactoryUtil
import com.example.administrator.newchat.viewmodel.ChatModel
import java.lang.RuntimeException


class ChatFragment : Fragment() {

    lateinit var binding:FragmentChatBinding
    lateinit var model:ChatModel
    private val adapter = ChatAdapter()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        val factory = ViewModelFactoryUtil.getChatModelFactory(requireContext())
        model = ViewModelProviders.of(this,factory).get(ChatModel::class.java)
        binding.setLifecycleOwner(this)
        initView()
        return binding.root
    }

    fun initView(){
        binding.chatRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRcView.adapter = adapter

    }

   fun begin(id:String){
       binding.chatBottom.init(id)
       model.getMessage(id).observe(this, Observer {
           adapter.submitList(it)
       })
       adapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){
           override fun onChanged() {
               binding.chatRcView.scrollToPosition(adapter.itemCount-1)
           }
       })
   }


    override fun onStart() {
        super.onStart()
        val id = activity?.intent?.getStringExtra(CLIENT_ID)
        if (id==null){
            throw RuntimeException("have not find this id")
        }else{
            begin(id)
        }
    }
}
