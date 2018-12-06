package com.example.administrator.newchat.adapter

import android.content.Intent
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.databinding.ContactRcItemBinding
import com.example.administrator.newchat.utilities.CLIENT_ID
import com.example.administrator.newchat.view.ChatActivity

class ContactHolder(val bind:ContactRcItemBinding):BaseHolder(bind.root){

    override fun bind(any: Any) {
        if (any is Contact){
            bind.contact = any
            bind.root.setOnClickListener {
                val intent = Intent(it.context,ChatActivity::class.java)
                intent.putExtra(CLIENT_ID,any.id)
                it.context.startActivity(intent)
            }
        }
    }
}