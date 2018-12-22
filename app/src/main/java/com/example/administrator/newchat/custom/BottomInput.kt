package com.example.administrator.newchat.custom

import android.content.Context
import android.text.TextUtils
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.BottomInputLayoutBinding
import com.example.administrator.newchat.utilities.SENDING
import com.example.administrator.newchat.utilities.TEXT_MESSAGE
import kotlinx.android.synthetic.main.fragment_main_draw_layout.view.*
import java.util.*

class BottomInput:LinearLayout{

    private lateinit var binding:BottomInputLayoutBinding
    private var conversationId:String? = null
    private var conversationName:String? = null
    private var listener:BottomInputListener? = null
    private val date = Date()
    private val userId = CoreChat.owner!!.userId
    constructor(context: Context):super(context){
        init(context)
    }

    fun init(conversationId:String,conversationName:String){
        this.conversationId = conversationId
        this.conversationName = conversationName
    }

    constructor(context: Context,attributeSet: AttributeSet):super(context,attributeSet){
        init(context)
    }

    private fun init(context: Context){
        binding = DataBindingUtil.inflate(LayoutInflater.from(context),R.layout.bottom_input_layout,this,true)
        send()
        sendImage()
    }

    fun setBottomInputListener(listener: BottomInputListener){
        if (this.listener == null){
            this.listener = listener
        }
    }

    private fun send(){
        binding.inputSendText.setOnClickListener {
            val text = binding.centerText.text.toString()
            if (!TextUtils.isEmpty(text)){
                sendText(text)
                binding.centerText.setText("")
            }
        }
    }

    fun sendImage(){
        binding.image.setOnClickListener {
            listener?.onClick(TYPE_IMAGE)
        }
    }



    private fun sendText(content:String){
        check()
        CoreChat.sendMessage(conversationName!!,conversationId!!, TEXT_MESSAGE,content){

        }
    }

    private fun check(){
        if (conversationId == null){
            throw Throwable("the client id is not found")
        }

        if (CoreChat.userId==null){
            throw Throwable("the user id is not found")
        }
    }

    interface BottomInputListener{

        fun onClick(type:Int)
    }

    companion object {
        const val TYPE_IMAGE = 1
    }
}