package com.example.administrator.newchat.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.avos.avoscloud.AVException
import com.avos.avoscloud.AVObject
import com.avos.avoscloud.FindCallback
import com.example.administrator.newchat.R
import com.example.administrator.newchat.adapter.QueryAdapter
import com.example.administrator.newchat.databinding.AddContactFragmentBinding
import com.example.administrator.newchat.presenter.AddContactPresenter


class AddContactFragment :BaseFragment(){

    private lateinit var binding:AddContactFragmentBinding
    private lateinit var presenter: AddContactPresenter
    private val adapter = QueryAdapter()
    private val callback = object :FindCallback<AVObject>(){

        override fun done(p0: MutableList<AVObject>?, p1: AVException?) {
            if (p1==null&&p0!=null){
                adapter.setList(p0)
                adapter.notifyDataSetChanged()
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = DataBindingUtil.inflate(inflater,R.layout.add_contact_fragment,container,false)
        init()
        return binding.root
    }

    private fun init(){
        binding.recyclerview.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerview.adapter = adapter
        presenter = AddContactPresenter()
        binding.searchView.setOnQueryTextListener(object :SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
               if (!query.isNullOrEmpty()){
                   presenter.getUserIdByNet(query,callback)
                   return true
               }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (!newText.isNullOrEmpty()){
                    presenter.getUserIdByNet(newText,callback)
                    return true
                }
                return false
            }

        })
    }

    override fun onBackPress(): Boolean {
        findNavController().navigate(R.id.messageFragment)
        return true
    }
}
