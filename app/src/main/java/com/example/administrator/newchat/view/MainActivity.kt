package com.example.administrator.newchat.view

import android.content.Intent
import android.graphics.Color
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.ActivityMainBinding
import com.example.administrator.newchat.utilities.*


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        setStatusBar()
        navController = findNavController(R.id.main_nav_fragment)
        initUI()
    }

    fun setStatusBar() {
        if (Build.VERSION.SDK_INT >= 21) {
            val window = this.window
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION)
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.statusBarColor = Color.parseColor("#ff5CACFC")
            window.decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN  or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            binding.drawerLayout.setStatusBarBackgroundColor(Color.TRANSPARENT)
            binding.drawerLayout.fitsSystemWindows = true
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        CoreChat.logout()
    }

    private fun initUI(){
        binding.mainviewhelper = MainViewHelper()
        setSupportActionBar(binding.toolbar)
        binding.bottomNav.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        navController.addOnNavigatedListener { controller, destination ->
            when(destination.id){
                R.id.addContactFragment,R.id.userMessageFragment->{
                 binding.bottomNav.visibility = View.GONE
                 binding.toolbar.visibility = View.GONE
             }
                R.id.messageFragment->{
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    setTitle("消息")
                }
                R.id.contactFragment->{
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                    setTitle("联系人")
                }
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.onNavDestinationSelected(navController)?:false||super.onOptionsItemSelected(item)
    }

    override fun onBackPressed() {
        if (!navController.popBackStack()){
            backHome()
        }
    }

    private fun backHome(){
        val home = Intent(Intent.ACTION_MAIN)
        home.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        home.addCategory(Intent.CATEGORY_HOME)
        startActivity(home)
    }
}
