package com.example.administrator.newchat.view

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.avos.sns.SNS
import com.avos.sns.SNSType
import com.example.administrator.newchat.R
import com.example.administrator.newchat.databinding.LogInLayoutBinding
import com.example.administrator.newchat.utilities.LogInHelper
import com.example.administrator.newchat.utilities.PASSWORD
import com.example.administrator.newchat.utilities.USER_NAME
import java.lang.ref.WeakReference

class LoginFragment:Fragment(){
    lateinit var binding: LogInLayoutBinding
    val thirdType = SNSType.AVOSCloudSNSQQ
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate<LogInLayoutBinding>(inflater, R.layout.log_in_layout,container,false)
        init()
        return binding.root
    }

    fun init(){
        var name = activity?.intent?.getStringExtra(USER_NAME)
        var password = activity?.intent?.getStringExtra(PASSWORD)
        val logInHelper =  LogInHelper(name?:"",password?:"")
        logInHelper.setWeakActivity(WeakReference(activity!!))
        binding.loginhelper = logInHelper
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        SNS.onActivityResult(requestCode,resultCode,data,thirdType)
    }
}

