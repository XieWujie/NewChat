package com.example.administrator.newchat.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.avos.sns.SNS
import com.avos.sns.SNSType
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.databinding.LogInLayoutBinding
import com.example.administrator.newchat.utilities.LogInHelper
import com.example.administrator.newchat.utilities.PASSWORD
import com.example.administrator.newchat.utilities.USER_NAME
import com.example.administrator.newchat.utilities.ViewModelFactoryUtil
import com.example.administrator.newchat.viewmodel.MainModel
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference

class LoginFragment:Fragment(){

    lateinit var model: MainModel
    lateinit var binding: LogInLayoutBinding
    val thirdType = SNSType.AVOSCloudSNSQQ

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<LogInLayoutBinding>(inflater, R.layout.log_in_layout,container,false)
        val factory = ViewModelFactoryUtil.getMainModelFactory(requireContext())
        model = ViewModelProviders.of(this,factory).get(MainModel::class.java)
        binding.setLifecycleOwner(this)
        init()
        return binding.root
    }

    fun init(){
        model.lastUser?.observe(this, Observer {
            if (it==null){
                val logInHelper =  LogInHelper("","")
                logInHelper.setWeakActivity(WeakReference(activity!!))
                binding.loginhelper = logInHelper
            }else{
                CoreChat.loginByPassword(it.name,it.password){ any->
                    if (any is User){
                        val intent = Intent(requireContext(),MainActivity::class.java)
                        startActivity(intent)
                    }else if (any is Exception){
                        Snackbar.make(binding.root,any.message as CharSequence, Snackbar.LENGTH_LONG)
                    }
                }
            }
        })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SNS.onActivityResult(requestCode,resultCode,data,thirdType)
    }
}

