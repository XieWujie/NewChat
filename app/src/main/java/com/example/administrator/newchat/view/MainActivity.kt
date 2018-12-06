package com.example.administrator.newchat.view


import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.onNavDestinationSelected
import androidx.navigation.ui.setupWithNavController
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.databinding.ActivityMainBinding
import com.example.administrator.newchat.utilities.*
import com.example.administrator.newchat.viewmodel.MainModel


class MainActivity : AppCompatActivity() {

    lateinit var binding:ActivityMainBinding
    lateinit var model:MainModel
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)
        initUI()
        val factory = ViewModelFactoryUtil.getMainModelFactory(this)
        model = ViewModelProviders.of(this,factory).get(MainModel::class.java)
        if (CoreChat.userId==null) {
            model.lastUser?.observe(this, Observer {
                loginAction(it)
            })
        }else{
            model.getUserById(CoreChat.userId!!).observe(this, Observer {

            })
        }
    }

    fun loginAction(lastUser:User?){
        if (lastUser==null){
            toLoginActivity("","")
            return
        }
        if (lastUser.isLogout){
            toLoginActivity(lastUser.name,lastUser.password)
        }else{
            CoreChat.init(lastUser)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        CoreChat.logout()
    }

    private fun initUI(){
        binding.mainviewhelper = MainViewHelper()
        setSupportActionBar(binding.toolbar)
        navController = findNavController(R.id.main_nav_fragment)
        binding.bottomNav.setupWithNavController(navController)
        binding.toolbar.setupWithNavController(navController, AppBarConfiguration(navController.graph))
        navController.addOnNavigatedListener { controller, destination ->
            when(destination.id){
                R.id.addContactFragment,R.id.messageFragment->{
                 binding.bottomNav.visibility = View.GONE
                 binding.toolbar.visibility = View.GONE
             }
                R.id.messageFragment,R.id.contactFragment->{
                    binding.bottomNav.visibility = View.VISIBLE
                    binding.toolbar.visibility = View.VISIBLE
                }

            }
        }
    }

    fun toLoginActivity(userName:String,password:String){
        val intent = Intent(this,LogInActivity::class.java)
        intent.putExtra(USER_NAME,userName)
        intent.putExtra(PASSWORD,password)
        startActivity(intent)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_top_menu,menu)
        return true
    }
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        return item?.onNavDestinationSelected(navController)?:false||super.onOptionsItemSelected(item)
    }
}
