package com.example.administrator.newchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.databinding.ContactRcItemBinding

class ContactAdapter:PagedListAdapter<Contact,ContactHolder>(contactDiff){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        val bind = ContactRcItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return ContactHolder(bind)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.bind(getItem(position)!!)
    }

    companion object {
        val contactDiff = object :DiffUtil.ItemCallback<Contact>(){
            override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
                return oldItem == newItem
            }

        }
    }
}