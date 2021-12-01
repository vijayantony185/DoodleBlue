package com.example.doodleblue.Model

import android.content.Context
import androidx.annotation.Nullable
import com.example.doodleblue.Utils.AppComUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SaveContact {
    companion object {
        fun save(@Nullable list: ArrayList<ContactsModel>?, context: Context?) {
            if (list != null) {
                val gson = Gson()
                val loginJson = gson.toJson(list)
                AppComUtils.setPreferenceString(context, "service_time", loginJson)
            } else {
                AppComUtils.setPreferenceString(context, "service_time", "")
            }
        }

        @Nullable
        fun getCurrentContact(context: Context?): ArrayList<ContactsModel>? {
            val gson = Gson()
            val json = AppComUtils.getPreferenceString(context, "service_time")
            val type = object : TypeToken<ArrayList<ContactsModel>?>() {}.type
            return gson.fromJson(json,type)
        }
    }
}