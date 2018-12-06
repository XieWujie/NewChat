package com.example.administrator.newchat.adapter

import android.view.View
import androidx.recyclerview.widget.RecyclerView

abstract class BaseHolder(item:View):RecyclerView.ViewHolder(item){

    abstract fun bind(any: Any)
}