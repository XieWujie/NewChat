package com.example.administrator.newchat.data.message

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.administrator.newchat.utilities.VERIFY_MESSAGE


@Dao
interface MessageDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(message: Message)

    @Query("SELECT * FROM message WHERE conversationId = :id AND type!=$VERIFY_MESSAGE ORDER BY createAt")
    fun queryById(id:String):DataSource.Factory<Int,Message>

    @Query("SELECT * FROM message m WHERE owner = :owner GROUP BY conversationId ORDER BY createAt DESC")
    fun getNewMessage(owner:String):DataSource.Factory<Int,Message>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(messages:List<Message>)
}