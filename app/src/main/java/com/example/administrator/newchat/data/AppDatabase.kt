package com.example.administrator.newchat.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.administrator.newchat.data.contacts.Contact
import com.example.administrator.newchat.data.contacts.ContactDao
import com.example.administrator.newchat.data.message.Message
import com.example.administrator.newchat.data.message.MessageDao
import com.example.administrator.newchat.data.user.User
import com.example.administrator.newchat.data.user.UserDao
import com.example.administrator.newchat.utilities.DATABASE_NAME

@Database(entities = [User::class,Contact::class,Message::class],version = 2,exportSchema = false)
abstract class AppDatabase:RoomDatabase(){

    abstract fun getUserDao(): UserDao
    abstract fun getContactsDao():ContactDao
    abstract fun getMessageDao():MessageDao

    companion object {

        private @Volatile var instance:AppDatabase? = null

        fun getInstance(context: Context):AppDatabase{
            return instance?: synchronized(this){
                instance?: buildInstance(context).also { instance = it }
            }
        }
        private fun buildInstance(context: Context):AppDatabase{
            return Room.databaseBuilder(context,AppDatabase::class.java, DATABASE_NAME)
                .addCallback(object :RoomDatabase.Callback(){
                    override fun onCreate(db: SupportSQLiteDatabase) {
                        super.onCreate(db)
                    }
                }).build()
        }
    }
}