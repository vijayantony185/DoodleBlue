package com.example.doodleblue.Adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.doodleblue.Model.ContactsModel
import com.example.doodleblue.Model.SaveContact
import com.example.doodleblue.R
import com.example.doodleblue.databinding.AdapterContactsBinding

class ContactsAdapter(
    var context: Context,
    var model: ArrayList<ContactsModel>,
    var click: ContactClick
) : RecyclerView.Adapter<ContactsAdapter.ContactHolder>() {

    inner class ContactHolder(adapterContactsBinding: AdapterContactsBinding) : RecyclerView.ViewHolder(adapterContactsBinding.root) {

        var adapterContactsBind : AdapterContactsBinding = adapterContactsBinding

        fun bindview(contactsModel: ContactsModel) {
            adapterContactsBind.tvContactName.text=contactsModel.name
            adapterContactsBind.tvContactNumber.text=contactsModel.number


            var model = SaveContact.getCurrentContact(context)
            if (model != null){
                model.forEach {
                    if (it.number.equals(contactsModel.number)){
                        adapterContactsBind.checkbox.isChecked = true
                    }
                }
            }

            adapterContactsBind.checkbox.setOnClickListener {

                var  b : Boolean = adapterContactsBind.checkbox.isChecked

                if (b){
                    click.checkmodel(contactsModel)
                }else {
                    click.uncheckmodel(contactsModel)
                }
            }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactHolder {
        var layoutinflater = LayoutInflater.from(parent.context)
        var adapterContactsBinding = AdapterContactsBinding.inflate(layoutinflater,parent,false)
        return ContactHolder(adapterContactsBinding)
    }

    override fun onBindViewHolder(holder: ContactHolder, position: Int) {
        holder.setIsRecyclable(false)
        holder.bindview(model[position])

    }

    override fun getItemCount(): Int {
        return model.size
    }

    interface ContactClick {
        fun checkmodel(contactsModel: ContactsModel)
        fun uncheckmodel(contactsModel: ContactsModel)
    }
}