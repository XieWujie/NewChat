package com.example.administrator.newchat.utilities

import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.provider.MediaStore
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.avos.avoscloud.im.v2.AVIMConversation
import com.avos.avoscloud.im.v2.AVIMException
import com.avos.avoscloud.im.v2.callback.AVIMConversationMemberQueryCallback
import com.avos.avoscloud.im.v2.conversation.AVIMConversationMemberInfo
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.getKey
import com.example.administrator.newchat.data.user.User

object ChatUtil{
    fun getRealPathFromURI(context: Context, contentUri: Uri): String? {
        if (contentUri.scheme == "file") {
            return contentUri.encodedPath
        } else {
            var cursor: Cursor? = null
            try {
                val pro = arrayOf(MediaStore.Images.Media.DATA)
                cursor = context.contentResolver.query(contentUri, pro, null, null, null)
                if (null != cursor) {
                    val column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
                    cursor.moveToFirst()
                    return cursor.getString(column_index)
                } else {
                    return ""
                }
            } finally {
                cursor?.close()
            }
        }
    }

    /**
     * 弹出键盘
     *
     * @param context
     * @param view
     */
    fun showSoftInput(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(view, 0)
        }
    }

    /**
     * 隐藏键盘
     *
     * @param context
     * @param view
     */
    fun hideSoftInput(context: Context, view: View?) {
        if (view != null) {
            val imm = context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun findConversationTitle(c:AVIMConversation,fromName:String,findCallback:(title:String)->Unit){
        val ownerName = CoreChat.owner?.name
        if (fromName == ownerName){
            val m = c["Info"] as Map<String,String?>
            val name = m[getKey(CoreChat.userId!!, OTHER_NAME)]
            if (name !=null){
                findCallback(name)
            }else{
                findCallback(fromName)
            }
        }else{
            findCallback(fromName)
        }
    }

}