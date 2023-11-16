package com.example.resview

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ContactAdapter(private val onContactClickListener: (Contact) -> Unit) :
    ListAdapter<Contact, RecyclerView.ViewHolder>(ContactDiffCallback()),ItemTouchHelperAdapter  {

    private val TEXT_VIEW_TYPE = 1
    private val CHECKBOX_VIEW_TYPE = 2

    private var isCheckboxMode = false

    private var mutableList: MutableList<Contact> = mutableListOf()

    init {
        mutableList = currentList.toMutableList()
    }

    fun setCheckboxMode(isCheckboxMode: Boolean) {
        this.isCheckboxMode = isCheckboxMode
        notifyItemRangeChanged(0,currentList.size)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        return when (viewType) {
            TEXT_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_contact, parent, false)
                ContactTextViewHolder(view)
            }
            CHECKBOX_VIEW_TYPE -> {
                val view = inflater.inflate(R.layout.item_contact_checkbox, parent, false)
                ContactCheckboxViewHolder(view)
            }
            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contact = getItem(position)

        when (holder.itemViewType) {
            TEXT_VIEW_TYPE -> {
                val textViewHolder = holder as ContactTextViewHolder
                textViewHolder.bind(contact)
                textViewHolder.itemView.setOnClickListener { onContactClickListener(contact) }
            }
            CHECKBOX_VIEW_TYPE -> {
                val checkboxViewHolder = holder as ContactCheckboxViewHolder
                checkboxViewHolder.bind(contact)
                checkboxViewHolder.itemView.setOnClickListener {
                    checkboxViewHolder.itemView.findViewById<CheckBox>(R.id.checkBox).apply{ isChecked=!isChecked}
                }
            }
        }

    }

    override fun getItemViewType(position: Int): Int {

        return if (isCheckboxMode) CHECKBOX_VIEW_TYPE else TEXT_VIEW_TYPE
    }

    class ContactTextViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.findViewById<TextView>(R.id.textId).text = contact.id.toString()
            itemView.findViewById<TextView>(R.id.textFirstName).text = contact.firstName
            itemView.findViewById<TextView>(R.id.textLastName).text = contact.lastName
            itemView.findViewById<TextView>(R.id.textPhoneNumber).text = contact.phoneNumber
        }
    }

    class ContactCheckboxViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(contact: Contact) {
            itemView.findViewById<TextView>(R.id.textId).text = contact.id.toString()
            itemView.findViewById<TextView>(R.id.textFirstName).text = contact.firstName
            itemView.findViewById<TextView>(R.id.textLastName).text = contact.lastName
            itemView.findViewById<TextView>(R.id.textPhoneNumber).text = contact.phoneNumber
            itemView.findViewById<CheckBox>(R.id.checkBox).setOnCheckedChangeListener { _, isChecked -> contact.isSelected = isChecked }
        }
    }

    class ContactDiffCallback : DiffUtil.ItemCallback<Contact>() {
        override fun areItemsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Contact, newItem: Contact): Boolean {
            return oldItem == newItem
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {

        val contacts = currentList.toMutableList()
        Collections.swap(contacts, fromPosition, toPosition)
        submitList(contacts)

    }

    override fun onItemDismiss(position: Int) {
        TODO("Not yet implemented")
    }

    override fun submitList(list: List<Contact>?) {
        list?.let {
            mutableList = it.toMutableList()
        }
        super.submitList(mutableList)
    }
}