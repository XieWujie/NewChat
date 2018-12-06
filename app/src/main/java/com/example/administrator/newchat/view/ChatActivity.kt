package com.example.administrator.newchat.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.administrator.newchat.R
import com.example.administrator.newchat.utilities.CLIENT_ID
import java.lang.RuntimeException

class ChatActivity : AppCompatActivity() {

    private lateinit var chatFragment: ChatFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

    }

    override fun onStart() {
        super.onStart()

    }
}
