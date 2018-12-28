package com.example.administrator.newchat.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.cache.LocalCacheUtil
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.RightVoiceLayoutBinding
import com.example.administrator.newchat.utilities.*
import com.google.android.material.snackbar.Snackbar
import java.io.File
import java.lang.Exception

class RightVoiceHolder(val bind:RightVoiceLayoutBinding):BaseHolder(bind.root){

    private val mediaPlayer = MediaPlayer()

    override fun bind(any: Any) {
        if (any is Message){
            when(any.sendState){
                SENDING->{
                    bind.chatRightProgressbar.visibility = View.VISIBLE
                    bind.chatRightTvError.visibility = View.GONE
                }
                SEND_FAIL->{
                    bind.chatRightProgressbar.visibility = View.GONE
                    bind.chatRightTvError.visibility = View.VISIBLE
                    bind.chatRightTvError.setOnClickListener {
                        resendEvent(any)
                        it.visibility = View.GONE
                        bind.chatRightProgressbar.visibility = View.VISIBLE
                    }
                }
                SEND_SUCCEED->{
                    bind.chatRightProgressbar.visibility = View.GONE
                    bind.chatRightTvError.visibility = View.GONE
                }
            }
            bind.message = any
            bind.chatRightLayoutContent.setOnClickListener {
                playVoice(any)
            }
        }
    }

    private fun playVoice(message: Message){
        if (message.message.contains("http")){
           val  path = PathUtil.getAudioCachePath(bind.root.context,message.id)
            if (path == null){
                begin(message.message)
                return
            }
            LocalCacheUtil.downloadFile(message.message!!,path!!,false,object : LocalCacheUtil.DownLoadCallback(){
                override fun done(e: Exception?) {
                    if (e == null){
                        begin(path!!)
                    }else{
                        e.printStackTrace()
                        Snackbar.make(bind.root,"语音加载失败",Snackbar.LENGTH_LONG).show()
                    }
                }
            })
        }else{
            begin(message.message)
        }
    }

    private fun resendEvent(message: Message){
        CoreChat.sendMessage(message)
    }

    private fun begin(path:String){
            if (mediaPlayer.isPlaying){
                mediaPlayer.stop()
            }else{
                mediaPlayer.reset()
                mediaPlayer.setDataSource(bind.root.context, Uri.parse(path))
                mediaPlayer.prepare()
                mediaPlayer.start()
            }
    }
}

