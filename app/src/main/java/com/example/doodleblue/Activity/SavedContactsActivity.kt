package com.example.doodleblue.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.doodleblue.Adapter.ContactsAdapter
import com.example.doodleblue.Model.ContactsModel
import com.example.doodleblue.Model.SaveContact
import com.example.doodleblue.R
import com.example.doodleblue.databinding.ActivitySavedContactsBinding

class SavedContactsActivity : AppCompatActivity() {
    lateinit var activitySavedContactsBinding: ActivitySavedContactsBinding
    var contactsAdapter : ContactsAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        activitySavedContactsBinding = ActivitySavedContactsBinding.inflate(layoutInflater)
        setContentView(activitySavedContactsBinding.root)

        var model = SaveContact.getCurrentContact(applicationContext)
        if (model != null){
            contactsAdapter = ContactsAdapter(applicationContext,model!!,object : ContactsAdapter.ContactClick{
                override fun checkmodel(contactsModel: ContactsModel) {
                    var model = SaveContact.getCurrentContact(applicationContext)
                    if (model == null) {
                        var contactmodel = ArrayList<ContactsModel>()
                        contactmodel.add(contactsModel)
                        SaveContact.save(contactmodel, applicationContext)
                    } else {
                        model.add(contactsModel)
                        SaveContact.save(model, applicationContext)
                    }
                }

                override fun uncheckmodel(contactsModel: ContactsModel) {
                    var model = SaveContact.getCurrentContact(applicationContext)

                    var templist = ArrayList<ContactsModel>()
                    templist.addAll(model!!)

                    if (model != null) {
                        model.forEach {
                            if (it.name.equals(contactsModel.name)) {
                                templist.remove(it)
                            }
                        }

                        model.clear()
                        model.addAll(templist)
                        SaveContact.save(model, applicationContext)
                    }
                }

            })

            activitySavedContactsBinding.recyclerviewSavedContacts!!.adapter = contactsAdapter
            activitySavedContactsBinding.recyclerviewSavedContacts!!.layoutManager = LinearLayoutManager(applicationContext)
        }

    }
}