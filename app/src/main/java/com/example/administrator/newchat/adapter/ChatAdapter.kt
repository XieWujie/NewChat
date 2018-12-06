package com.example.administrator.newchat.adapter


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagedListAdapter
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.LeftLayoutTextBinding
import com.example.administrator.newchat.databinding.RightLayoutTextBinding
import com.example.administrator.newchat.utilities.TEXT_MESSAGE

class ChatAdapter:PagedListAdapter<Message,BaseHolder>(MessageDiffCallBack()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val inflater = LayoutInflater.from(parent.context)
        val baseHolder:BaseHolder =  when(viewType){
            TYPE_TEXT_RIGHT->RightTextHolder(RightLayoutTextBinding.inflate(inflater,parent,false))
            TYPE_TEXT_LEFT->LeftTextHolder(LeftLayoutTextBinding.inflate(inflater,parent,false))
          else ->throw Throwable("have not find this holder")
        }
        return baseHolder
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        val item = getItem(position)
        if (item!=null){
            holder.bind(item)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position)!!.let {
            when(it.type){
                TEXT_MESSAGE ->if (it.isMe) TYPE_TEXT_RIGHT else TYPE_TEXT_LEFT
                else ->throw Throwable("have not find this type")
        }
        }
    }

    companion object {

        const val TYPE_TEXT_RIGHT = 10
        const val TYPE_TEXT_LEFT = 11
    }
}