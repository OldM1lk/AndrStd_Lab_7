package com.example.lab_7

import androidx.recyclerview.widget.DiffUtil

class ContactItemDiffCallback : DiffUtil.ItemCallback<Contact>() {

    override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem.phone == newItem.phone
    }

    override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
        return oldItem == newItem
    }
}