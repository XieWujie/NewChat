package com.example.administrator.newchat.custom

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.administrator.newchat.R
import java.lang.StringBuilder
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

        const val a = 8*3600000
        @JvmStatic
        @BindingAdapter("time")
        fun setTime(view: TextView,timeStamp:Long){
            val d = Date(timeStamp+ a)
            val f = SimpleDateFormat("dd日 HH:mm:ss")
            val s = f.format(d)
            view.text = s
        }

        @JvmStatic
        @BindingAdapter("right_time")
        fun setRightTime(view: TextView,timeStamp: Long){
            val d = Date(timeStamp)
            val f = SimpleDateFormat("dd日 HH:mm:ss")
            val s = f.format(d)
            view.text = s
        }

        @JvmStatic
        @BindingAdapter("unReadCount")
        fun setUnReadCount(view: TextView,count:Int){
            if (count>0) {
                view.text = count.toString()
            }
        }

        @JvmStatic
        @BindingAdapter("voiceTime")
        fun setVoiceTime(view: TextView,time:Double){
            val l = time.toString().split(".")
            if (l.size == 2){
                val t = """${l[0]}"${l[1].toInt()/100}"""
                view.text = t
            }else{
                view.text = time.toString()
            }
        }
    }
}