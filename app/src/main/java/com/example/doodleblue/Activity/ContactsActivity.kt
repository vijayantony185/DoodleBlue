package com.example.doodleblue.Activity

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.provider.Settings
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.doodleblue.Adapter.ContactsAdapter
import com.example.doodleblue.Constants.Progress
import com.example.doodleblue.Model.ContactsModel
import com.example.doodleblue.Model.SaveContact
import com.example.doodleblue.R
import com.example.doodleblue.ViewModel.GetContactsViewModel
import com.example.doodleblue.databinding.ActivityContactsBinding

class ContactsActivity : AppCompatActivity() {

    var progress: Progress? = null
    var contactsAdapter : ContactsAdapter? = null
    lateinit var getContactsViewModel: GetContactsViewModel
    lateinit var activityContactsBinding : ActivityContactsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

       activityContactsBinding = ActivityContactsBinding.inflate(layoutInflater)
        setContentView(activityContactsBinding.root)


        activityContactsBinding.toolbar!!.setOnClickListener {
            var intent = Intent(applicationContext, SavedContactsActivity::class.java)
            startActivity(intent)
        }


        checkpermission()

    }

    fun checkpermission() {
        //check condition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)){
            val intent = Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:$packageName"))
            startActivityForResult(intent, 105)
        }else {
            check()
        }
    }

    private fun check() {
        if ((ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) && (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.READ_PHONE_STATE
            ) != PackageManager.PERMISSION_GRANTED) &&(ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED) ) {
            //when permission is not granted request permission
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.READ_CONTACTS, Manifest.permission.READ_PHONE_STATE,Manifest.permission.READ_CALL_LOG),
                100
            )
        } else {
            getContactList()
        }
    }

    private fun getContactList() {
        getContactsViewModel = ViewModelProviders.of(this).get(GetContactsViewModel::class.java)
        getContactsViewModel.getcontacts(applicationContext)
        getContactsViewModel.getLiveData().observe(this,{
            if (it != null){
                contactsAdapter = ContactsAdapter(
                    applicationContext,
                    it,
                    object : ContactsAdapter.ContactClick {
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
                    });

                activityContactsBinding.recyclerview!!.adapter = contactsAdapter
                activityContactsBinding.recyclerview!!.layoutManager = LinearLayoutManager(applicationContext)
            }
        })
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String?>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 100 && grantResults.size > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getContactList()
        } else {
            Toast.makeText(applicationContext, "permission Denied", Toast.LENGTH_SHORT).show()
            checkpermission()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        if (requestCode == 105) {
            check()
        } else {
            Toast.makeText(this, "Draw over other app permission not available. Closing the application", Toast.LENGTH_SHORT).show()
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun showProgress() {
        progress = Progress(this)
        progress!!.show()
    }

    private fun cancelProgress() {
        if (progress != null) {
            progress!!.dismiss()
        }
    }
}