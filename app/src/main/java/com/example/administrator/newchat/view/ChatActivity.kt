package com.example.administrator.newchat.view

import android.content.res.Configuration
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.databinding.DataBindingUtil
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.ActivityChatBinding
import com.example.administrator.newchat.utilities.AVATAR
import com.example.administrator.newchat.utilities.CONVERSATION_ID
import com.example.administrator.newchat.utilities.CONVERSATION__NAME


class ChatActivity : AppCompatActivity() {

    private lateinit var chatFragment: ChatFragment
    private lateinit var binding:ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView<ActivityChatBinding>(this,R.layout.activity_chat)
        chatFragment = supportFragmentManager.findFragmentById(R.id.chat_fragment) as ChatFragment
        initView()
        val conversationId = intent.getStringExtra(CONVERSATION_ID)
        val conversationName = intent.getStringExtra(CONVERSATION__NAME)
        val avatar = intent.getStringExtra(AVATAR)
        chatFragment.begin(conversationId,conversationName,avatar)
        setTitle(conversationName)
    }

    private fun initView(){
        setSupportActionBar(binding.toolbar)
        setStatusBar()
        binding.toolbar.setTitleTextColor(Color.WHITE)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#ff303030")
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration?) {
        super.onConfigurationChanged(newConfig)
    }
}
