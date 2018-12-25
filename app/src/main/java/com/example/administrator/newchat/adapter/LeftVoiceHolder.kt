package com.example.administrator.newchat.adapter

import android.media.AudioManager
import android.media.MediaPlayer
import android.net.Uri
import android.util.Log
import android.view.View
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
            var path :String?
            if (any.message.contains("http")){
                path = PathUtil.getAudioCachePath(bind.root.context,any.id)
                if (path == null)return
                LocalCacheUtil.downloadFile(any.message!!,path!!,false,object :LocalCacheUtil.DownLoadCallback(){
                    override fun done(e: Exception?) {
                        if (e == null){
                            begin(path!!)
                            Log.d("localpath",path!!)
                        }
                    }
                })
            }else{
                begin(any.message)
            }
        }
    }

    private fun begin(path:String){
        runOnNewThread {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(bind.root.context, Uri.parse(path))
            mediaPlayer.prepare()
            bind.voiceText.setOnClickListener {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                }else{
                    mediaPlayer.start()
                }
            }
        }
    }
}

