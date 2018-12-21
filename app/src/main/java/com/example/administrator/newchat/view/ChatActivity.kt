package com.example.administrator.newchat.view

import android.content.Context
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.ActivityChatBinding
import com.example.administrator.newchat.utilities.CONVERSATION_ID
import com.example.administrator.newchat.utilities.CONVERSATION__NAME


class ChatActivity : AppCompatActivity() {

    private lateinit var chatFragment: ChatFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityChatBinding>(this,R.layout.activity_chat)
        chatFragment = supportFragmentManager.findFragmentById(R.id.chat_fragment) as ChatFragment
        setSupportActionBar(binding.toolbar)
        setStatusBar()
    }


    fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#ff5CACFC")
        }
    }
    override fun onStart() {
        super.onStart()
        val conversationId = intent.getStringExtra(CONVERSATION_ID)
        val conversationName = intent.getStringExtra(CONVERSATION__NAME)
        chatFragment.begin(conversationId,conversationName)
        setTitle(conversationName)
    }
}
