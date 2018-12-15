package com.example.administrator.newchat.data.user

import androidx.lifecycle.LiveData
import androidx.room.*
import com.example.administrator.newchat.data.user.User

@Dao
interface UserDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: User)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updata(user: User)

    @Query("SELECT * FROM user ORDER BY loginTime DESC LIMIT -1,1")
    fun getLastUser():LiveData<User>

    @Query("SELECT * FROM user WHERE id =:id")
    fun getUserById(id:String):LiveData<User>

    @Query("DELETE FROM user")
    fun deleteAllUser()

}