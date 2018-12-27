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
            var path :String?
            if (any.message.contains("http")){
                path = PathUtil.getAudioCachePath(bind.root.context,any.id)
                if (path == null)return
                LocalCacheUtil.downloadFile(any.message!!,path!!,false,object : LocalCacheUtil.DownLoadCallback(){
                    override fun done(e: Exception?) {
                        if (e == null){
                            begin(path!!)
                        }
                    }
                })
            }else{
                begin(any.message)
            }
        }
    }

    private fun resendEvent(message: Message){
        CoreChat.sendMessage(message)
    }

    private fun begin(path:String){
        runOnNewThread {
            mediaPlayer.reset()
            mediaPlayer.setDataSource(bind.root.context, Uri.parse(path))
            mediaPlayer.prepare()
            bind.chatRightLayoutContent.setOnClickListener {
                if (mediaPlayer.isPlaying){
                    mediaPlayer.stop()
                }else{
                    mediaPlayer.start()
                }
            }
        }
    }
}

