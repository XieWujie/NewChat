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
import com.bumptech.glide.Glide
import com.example.administrator.newchat.CoreChat
import com.example.administrator.newchat.custom.BottomInput


import com.example.administrator.newchat.databinding.FragmentMainDrawLayoutBinding
import com.example.administrator.newchat.utilities.ChatUtil
import com.example.administrator.newchat.presenter.MainDrawPresenter
import com.google.android.material.snackbar.Snackbar

class MainDrawFragment : Fragment() {
    private lateinit var bind:FragmentMainDrawLayoutBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        bind = FragmentMainDrawLayoutBinding.inflate(inflater,container,false)
        bind.presenter = MainDrawPresenter(CoreChat.owner!!.avatar)
        bind.avatar.setOnClickListener {
            requestPermission()
        }
        return bind.root
    }


    private fun requestPermission() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(activity as Activity,
                arrayOf(Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        } else {
            dispatchPictureIntent()
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, @NonNull permissions: Array<String>, @NonNull grantResults: IntArray) {
        if (grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            when (requestCode) {
                BottomInput.TYPE_IMAGE ->dispatchPictureIntent()
            }
        }else {
            Snackbar.make(bind.root, "拒绝权限将不能正常使用该功能", Snackbar.LENGTH_LONG).show()
        }
    }



    private fun dispatchPictureIntent() {
        val photoPickerIntent = Intent(Intent.ACTION_PICK, null)
        photoPickerIntent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
        startActivityForResult(photoPickerIntent, BottomInput.TYPE_IMAGE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        if (Activity.RESULT_OK == resultCode) {
            if (data.data!=null){
                initAvatar(data.data)
            }
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun initAvatar(uri: Uri){
        val realPath = ChatUtil.getRealPathFromURI(requireContext(),uri)
        Glide.with(this).load(realPath).into(bind.avatar)
        CoreChat.setAvatar(realPath!!)
    }
}
