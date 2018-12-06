package com.example.administrator.newchat.utilities

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide


class ViewAdapter{

    companion object {

        @JvmStatic
        @BindingAdapter("imageSrc")
        fun setImage(view:ImageView,src:String){
            Glide.with(view)
                .load(src)
                .into(view)
        }
    }
}