package com.example.administrator.newchat.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.avos.avoscloud.AVObject
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.QueryItemLayoutBinding

class QueryAdapter:RecyclerView.Adapter<BaseHolder>(){

    private val queryList = ArrayList<AVObject>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BaseHolder {
        val binding = DataBindingUtil.inflate<QueryItemLayoutBinding>(LayoutInflater.from(parent.context), R.layout.query_item_layout,parent,false)
        return OneTextHolder(binding)
    }

    override fun getItemCount(): Int {
        return queryList.size
    }

    override fun onBindViewHolder(holder: BaseHolder, position: Int) {
        holder.bind(queryList[position])
    }

    fun setList(list: List<AVObject>){
        queryList.clear()
        queryList.addAll(list)
    }


}