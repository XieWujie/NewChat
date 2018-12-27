package com.example.administrator.newchat.data.contacts


import com.example.administrator.newchat.utilities.runOnNewThread

class ContactRespository private constructor(private val contactDao: ContactDao){

    fun addContact(contact: Contact){
        runOnNewThread {
            contactDao.addContact(contact)
        }
    }

    fun getAllContacts(ownerId:String) = contactDao.getAllContacts(ownerId)


    fun removeContact(contact: Contact){
        runOnNewThread {
            contactDao.remove(contact)
        }
    }

    fun addContacts(list: List<Contact>){
        runOnNewThread {
            contactDao.addContacts(list)
        }
    }

    fun deleteAllContact(){
        runOnNewThread {
            contactDao.deleteAllContact()
        }
    }

    companion object {

        @Volatile var instance:ContactRespository? = null

        fun getInstance(contactDao: ContactDao):ContactRespository{
            return instance?: synchronized(this){
                instance?: ContactRespository(contactDao).also { instance = it }
            }
        }
    }
}