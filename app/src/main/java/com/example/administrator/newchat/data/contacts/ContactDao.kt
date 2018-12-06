package com.example.administrator.newchat.data.contacts

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ContactDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContact(contact: Contact)

    @Query("SELECT * FROM contact WHERE ownerId = :ownerId")
    fun getAllContacts(ownerId:String):DataSource.Factory<Int,Contact>

    @Query("SELECT * FROM contact")
    fun getContacts():List<Contact>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addContacts(list:List<Contact>)

    @Query("SELECT id FROM contact")
    fun getContactsId():List<String>

    @Query("DELETE FROM contact")
    fun deleteAllContact()
}