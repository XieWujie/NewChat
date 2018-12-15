package com.example.administrator.newchat.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.paging.PagedList
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.adapter.ContactAdapter
import com.example.administrator.newchat.custom.RcDecoration
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.databinding.ContactListLayoutBinding
import com.example.administrator.newchat.utilities.ViewModelFactoryUtil
import com.example.administrator.newchat.viewmodel.ContactModel
import com.google.android.material.snackbar.Snackbar

class ContactFragment:Fragment(){

    private lateinit var binding:ContactListLayoutBinding
    private lateinit var adapter:ContactAdapter
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.contact_list_layout,container,false)
        val factory = ViewModelFactoryUtil.getContactModelFactory(requireContext())
        val model = ViewModelProviders.of(this,factory).get(ContactModel::class.java)
        binding.setLifecycleOwner(this)
        val recyclerView = binding.cRecyclerview
        recyclerView.addItemDecoration(RcDecoration())
        adapter = ContactAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        model.getAllContacts(CoreChat.userId!!).observe(this, Observer {
            if (!it.isNullOrEmpty()){
                adapter.submitList(it)
            }
        })
        return binding.root
    }

    override fun onResume() {
        super.onResume()
        adapter.notifyDataSetChanged()
    }
}