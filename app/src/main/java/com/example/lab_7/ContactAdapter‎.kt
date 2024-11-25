package com.example.lab_7

import android.content.Intent
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView

class ContactAdapter : ListAdapter<Contact, ContactAdapter.ViewHolder>(ContactItemDiffCallback()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val name: TextView = view.findViewById(R.id.textName)
        private val phone: TextView = view.findViewById(R.id.textPhone)
        private val type: TextView = view.findViewById(R.id.textType)

        fun bind(contact: Contact) {
            name.text = contact.name
            phone.text = contact.phone
            type.text = contact.type

            itemView.setOnClickListener {
                val dial = Intent(Intent.ACTION_DIAL)
                dial.data = Uri.parse(("tel:${contact.phone}"))
                itemView.context.startActivity(dial)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.rview_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}