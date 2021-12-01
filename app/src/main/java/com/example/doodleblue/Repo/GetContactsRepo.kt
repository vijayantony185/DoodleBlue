package com.example.doodleblue.Repo

import android.content.ContentValues
import android.content.Context
import android.provider.ContactsContract
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.doodleblue.Model.ContactsModel

class GetContactsRepo {

    var arrayList = ArrayList<ContactsModel>()

    fun getcontacts(context: Context) : LiveData<ArrayList<ContactsModel>>{
        val data: MutableLiveData<ArrayList<ContactsModel>> = MutableLiveData()

        val cr = context.contentResolver
        val cur = cr.query(
            ContactsContract.Contacts.CONTENT_URI,
            null, null, null, null
        )
        if (cur?.count ?: 0 > 0) {
            while (cur != null && cur.moveToNext()) {
                val id = cur.getString(
                    cur.getColumnIndexOrThrow(ContactsContract.Contacts._ID)
                )
                val name = cur.getString(
                    cur.getColumnIndexOrThrow(
                        ContactsContract.Contacts.DISPLAY_NAME
                    )
                )
                if (cur.getInt(
                        cur.getColumnIndexOrThrow(
                            ContactsContract.Contacts.HAS_PHONE_NUMBER
                        )
                    ) > 0
                ) {
                    val pCur = cr.query(
                        ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                        null,
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                        arrayOf(id),
                        null
                    )
                    val model = ContactsModel()
                    while (pCur!!.moveToNext()) {
                        val phoneNo = pCur.getString(
                            pCur.getColumnIndexOrThrow(
                                ContactsContract.CommonDataKinds.Phone.NUMBER
                            )
                        )
                        model.number = phoneNo
                        model.name = name
                        Log.i(ContentValues.TAG, "Name: $name")
                        Log.i(ContentValues.TAG, "Phone Number: $phoneNo")
                    }
                    arrayList.add(model)
                    data.value = arrayList
                    pCur.close()
                }
            }
        }
        cur?.close()

        return data
    }
}