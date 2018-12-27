package com.example.administrator.newchat.view

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.ActivityChatBinding
import com.example.administrator.newchat.utilities.*


class ChatActivity : AppCompatActivity(){

    private lateinit var chatFragment: ChatFragment
    private lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_chat)
        chatFragment = supportFragmentManager.findFragmentById(R.id.chat_fragment) as ChatFragment
        initView()
        initConversation()
    }

    private fun initConversation(){
        val conversationId = intent.getStringExtra(CONVERSATION_ID)
        val conversationName = intent.getStringExtra(CONVERSATION__NAME)
        val avatar = intent.getStringExtra(AVATAR)
        CoreChat.finConversationById(conversationId) { conversation ->
            ChatUtil.findConversationTitle(conversation, conversationName) {
                setTitle(it)
                chatFragment.begin(conversationId, it, avatar, conversation)
            }
        }
    }

    private fun initView(){
        setSupportActionBar(binding.toolbar)
        setStatusBar()
        binding.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeAsUpIndicator(R.drawable.home_back_icon)
    }


    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }
    fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#ff303030")
        }
    }
}
