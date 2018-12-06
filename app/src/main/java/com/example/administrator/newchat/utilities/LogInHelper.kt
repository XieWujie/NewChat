package com.example.administrator.newchat.utilities

import android.content.Context
import android.content.Intent
import android.view.View
import androidx.databinding.BaseObservable
import androidx.databinding.Bindable
import androidx.fragment.app.FragmentActivity
import androidx.navigation.findNavController
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVUser
import com.avos.avoscloud.LogInCallback
import com.avos.sns.*
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.R
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.view.MainActivity
import com.google.android.material.snackbar.Snackbar
import java.lang.ref.WeakReference


class LogInHelper(
     @Bindable var userName:String,
     @Bindable var password:String
 ):BaseObservable(){

    var activity:WeakReference<FragmentActivity> ? = null
    fun login(view:View){
        CoreChat.loginByPassword(userName,password){
            if (it is User){
                val intent = Intent(view.context,MainActivity::class.java)
                view.context.startActivity(intent)
            }else if (it is Exception){
                Snackbar.make(view,it.message as CharSequence,Snackbar.LENGTH_LONG)
            }
        }
    }

    fun register(view: View){
        view.findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
    }

    fun qqLogin(view: View){

    }

    fun setWeakActivity(activity: WeakReference<FragmentActivity>){
        this.activity = activity
    }

    fun loginByQQ(context: Context){
        if (activity==null||activity?.get()==null){
            throw Throwable("have not found activity")
        }
        SNS.setupPlatform(context, SNSType.AVOSCloudSNSQQ, "1107783980","hUr0zNOkrQTwzSQ8","https://leancloud.cn")
        SNS.loginWithCallback(activity!!.get(),SNSType.AVOSCloudSNSQQ,object :SNSCallback(){
            override fun done(snsbase: SNSBase?, p1: SNSException?) {
                if (p1 == null){
                    val userInfo = snsbase?.userInfo()
                    if (userInfo!=null){
                        SNS.loginWithAuthData(userInfo,object :LogInCallback<AVUser>(){
                            override fun done(p0: AVUser?, p1: AVException?) {
                                if (p1==null){

                                }
                            }
                        })
                    }
                }
            }

        })
    }
}