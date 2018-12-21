package com.example.administrator.newchat.custom

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.administrator.newchat.R
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


class ViewAdapter{

    companion object {

        @JvmStatic
        @BindingAdapter("imageSrc")
        fun setImage(view:ImageView,src:String?){
            if (src.isNullOrBlank()){
                view.setImageResource(R.drawable.user_default_avator)
            }else {
                Glide.with(view)
                    .load(src)
                    .into(view)
            }
        }
        const val cn = 8*3600000
        @JvmStatic
        @BindingAdapter("time")
        fun setTime(view: TextView,timeStamp:Long){
            val d = Date(timeStamp+ cn)
            val f = SimpleDateFormat("dd日 HH:mm:ss")
            val s = f.format(d)
            view.text = s
        }
    }
}