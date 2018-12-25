package com.example.administrator.newchat.utilities

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import com.avos.avoscloud.AVPersistenceUtils.getCacheDir
import android.os.Environment.MEDIA_MOUNTED
import java.io.File


object PathUtil{
    private fun isExternalStorageWritable(): Boolean {
        val state = Environment.getExternalStorageState()
        return Environment.MEDIA_MOUNTED.equals(state)
    }


    private fun getAvailableCacheDir(context: Context): File {
        return if (isExternalStorageWritable()) {
            context.getExternalCacheDir()
        } else {
            context.getCacheDir()
        }
    }

    fun getAudioCachePath(context: Context, id: String): String? {
        return if (TextUtils.isEmpty(id)) null else File(getAvailableCacheDir(context), id).getAbsolutePath()
    }

    fun getRecordPathByCurrentTime(context: Context): String {
        return File(getAvailableCacheDir(context), "record_" + System.currentTimeMillis()).absolutePath
    }
}