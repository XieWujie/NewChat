package com.example.administrator.newchat.view


import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.administrator.newchat.CoreChat

import com.example.administrator.newchat.adapter.ChatAdapter
import com.example.administrator.newchat.custom.BottomInput
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.databinding.FragmentChatBinding
import com.example.administrator.newchat.utilities.*
import com.example.administrator.newchat.viewmodel.MessageModel
import com.google.android.material.snackbar.Snackbar
import java.lang.RuntimeException
import java.util.*


class ChatFragment : Fragment() {

    lateinit var binding:FragmentChatBinding
    lateinit var model:MessageModel
    private val adapter = ChatAdapter()
    private var conversationName:String? = null
    private var conversationId:String? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = FragmentChatBinding.inflate(inflater,container,false)
        val factory = ViewModelFactoryUtil.getChatModelFactory(requireContext())
        model = ViewModelProviders.of(this,factory).get(MessageModel::class.java)
        binding.setLifecycleOwner(this)
        initView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val conversationId = activity?.intent?.getStringExtra(CONVERSATION_ID)
        val conversationName = activity?.intent?.getStringExtra(CONVERSATION__NAME)
        if (conversationId==null || conversationName==null){
            throw RuntimeException("have not find this id")
        }else{
            binding.freshLayout.setOnRefreshListener{
                val message = if (adapter.currentList?.size?:0>0){
                    adapter?.currentList?.get(0)
                }else{
                    null
                }
                if (message!=null){
                    CoreChat.queryMessageByTime(message.id,message.createAt)
                }else{
                    CoreChat.queryMessageByConversationId(conversationId)
                }
                binding.freshLayout.isRefreshing = false
            }
        }
    }

    fun initView(){
        binding.chatRcView.layoutManager = LinearLayoutManager(requireContext())
        binding.chatRcView.adapter = adapter
    }

   fun begin(id:String,conversationName:String){
       this.conversationId = id
       this.conversationName = conversationName
       binding.chatBottom.init(id,conversationName)
       binding.chatBottom.setBottomInputListener(object :BottomInput.BottomInputListener{
           override fun onClick(type: Int) {
               dispatchEvent(type)
           }
       })
       //CoreChat.queryMessageByConversationId(id)
       model.getMessage(id).observe(this, Observer {
           adapter.submitList(it)
       })
       adapter.registerAdapterDataObserver(object :RecyclerView.AdapterDataObserver(){

           override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
               super.onItemRangeInserted(positionStart, itemCount)
               binding.chatRcView.scrollToPosition(adapter.itemCount-1)
           }
       })
   }

    fun dispatchEvent(type: Int){
        when(type){
            BottomInput.TYPE_IMAGE->requestPermission(type)
        }
    }

    private fun requestPermission(type:Int) {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), type)
        } else {
            when(type){
                BottomInput.TYPE_IMAGE->dispatchPictureIntent()
            }
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                BottomInput.TYPE_IMAGE ->dispatchPictureIntent()
            }
        } else {
            Snackbar.make(binding.root, "拒绝权限将不能正常使用该功能", Snackbar.LENGTH_LONG).show()
        }
    }

    fun sendImageMessage(uri: Uri?) {
        if (uri!=null) {
            val realPath = ChatUtil.getRealPathFromURI(requireContext(), uri)
            val message = Message("",conversationId!!,realPath!!,conversationName!!,
                IMAGE_MESSAGE,CoreChat.userId!!,1,Date().time,CoreChat.userId!!,CoreChat.owner?.avatar)
            CoreChat.sendMessage(message)
        }
    }

    private fun dispatchPictureIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK, null)
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(photoPickerIntent, BottomInput.TYPE_IMAGE)
    }

   override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {

        if (Activity.RESULT_OK == resultCode) {
            when (requestCode) {
                BottomInput.TYPE_IMAGE -> sendImageMessage(data.data)
                else -> {

                }
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}
