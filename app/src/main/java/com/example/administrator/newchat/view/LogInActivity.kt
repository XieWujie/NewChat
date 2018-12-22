package com.example.administrator.newchat.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.Navigation
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.ActivityStartLayoutBinding


class LogInActivity : AppCompatActivity() {

    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityStartLayoutBinding>(this,R.layout.activity_start_layout)
        navController = Navigation.findNavController(this,R.id.start_nav_fragment)
    }

    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp()
    }
}
