package com.example.doodleblue.ViewModel

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import com.example.doodleblue.Model.ContactsModel
import com.example.doodleblue.Repo.GetContactsRepo

class GetContactsViewModel(application: Application) : AndroidViewModel(application) {

    lateinit var getContactsRepo: GetContactsRepo
    var contactslivedata : LiveData<ArrayList<ContactsModel>>? = null
    var initialize = false

    fun getcontacts(context: Context){
        if (!initialize){
            getContactsRepo = GetContactsRepo()
            this.contactslivedata =getContactsRepo.getcontacts(context)
        }
    }

    fun getLiveData() : LiveData<ArrayList<ContactsModel>>{
        return contactslivedata!!
    }
}