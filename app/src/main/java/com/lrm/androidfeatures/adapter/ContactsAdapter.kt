package com.lrm.androidfeatures.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.model.Contacts

class ContactsAdapter(
    context: Context,
    val contactList: List<Contacts>
) : RecyclerView.Adapter<ContactsAdapter.ContactsViewHolder>() {

    private lateinit var mListener: onItemClickListener

    interface onItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: onItemClickListener) {
        mListener = listener
    }

    class ContactsViewHolder(view: View, listener: onItemClickListener) : RecyclerView.ViewHolder(view) {
        val name = view.findViewById<TextView>(R.id.name)
        val number = view.findViewById<TextView>(R.id.number)

        init {
            itemView.setOnClickListener {
                listener.onItemClick(adapterPosition)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactsViewHolder {
        val layout =
            LayoutInflater.from(parent.context).inflate(R.layout.contacts_list_item, parent, false)

        return ContactsViewHolder(layout, mListener)
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    override fun onBindViewHolder(holder: ContactsViewHolder, position: Int) {
        val contact = contactList[position]
        holder.name.text = contact.name
        holder.number.text = contact.number
    }
}