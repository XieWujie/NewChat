package com.example.administrator.newchat.adapter

import android.content.Context
import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.data.cache.DownloadUtil
import com.example.administrator.newchat.data.cache.LocalCacheUtil
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.LeftLayoutVoiceBinding
import com.example.administrator.newchat.databinding.RightVoiceLayoutBinding
import com.example.administrator.newchat.utilities.*
import java.io.File
import java.io.IOException
import java.lang.Exception

class LeftVoiceHolder(val bind:LeftLayoutVoiceBinding):BaseHolder(bind.root){

    private val mediaPlayer = MediaPlayer()

    override fun bind(any: Any) {
        if (any is Message){
            bind.message = any
            bind.voiceText.setOnClickListener {
                playVoice(any)
            }
        }
    }

    private fun playVoice(message: Message){
        var path :String?
        if (message.message.contains("http")){
            path = PathUtil.getAudioCachePath(bind.root.context,message.id)
            if (path == null)return
            LocalCacheUtil.downloadFile(message.message!!,path!!,false,object : LocalCacheUtil.DownLoadCallback(){
                override fun done(e: Exception?) {
                    if (e == null){
                        begin(path!!)
                    }
                }
            })
        }else{
            begin(message.message)
        }
    }

    private fun begin(path:String){
        runOnNewThread {
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
}

