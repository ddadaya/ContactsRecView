package com.example.resview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ItemTouchHelper.DOWN
import androidx.recyclerview.widget.ItemTouchHelper.UP
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import kotlin.random.Random

class MainActivity : AppCompatActivity(), AddContactDialogFragment.AddContactDialogListener,
    EditContactDialogFragment.EditContactDialogListener {

    private lateinit var addContactButton: ImageButton
    private lateinit var deleteButton: ImageButton

    private lateinit var cancelButton: Button
    private lateinit var agreeButton: Button

    private lateinit var recyclerView: RecyclerView

    private var isSelectionMode = false
    private var contacts = mutableListOf<Contact>()

    private val contactAdapter = ContactAdapter { contact -> showEditContactDialog(contact) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addContactButton = findViewById(R.id.addButton)
        deleteButton = findViewById(R.id.deleteButton)
        cancelButton = findViewById(R.id.cancelButton)
        agreeButton = findViewById(R.id.agreeButton)

        recyclerView = findViewById(R.id.recyclerView)

        contacts = generateContacts()
        contactAdapter.submitList(contacts)

        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = contactAdapter

        deleteButton.setOnClickListener { toggleSelectionMode() }
        cancelButton.setOnClickListener { exitSelectionMode() }
        agreeButton.setOnClickListener { deleteSelectedContacts() }
        addContactButton.setOnClickListener { showAddContactDialog() }

        val itemTouchHelper = ItemTouchHelper(SimpleItemTouchHelperCallback(contactAdapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)


    }

    inner class SimpleItemTouchHelperCallback(private val adapter: ItemTouchHelperAdapter) :
        ItemTouchHelper.SimpleCallback(UP+DOWN,0) {

        override fun isLongPressDragEnabled(): Boolean {
            return !isSelectionMode
        }

        override fun onMove(
            recyclerView: RecyclerView,
            viewHolder: RecyclerView.ViewHolder,
            target: RecyclerView.ViewHolder
        ): Boolean {
            adapter.onItemMove(viewHolder.absoluteAdapterPosition, target.absoluteAdapterPosition)


            return true
        }

        override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {}
    }

    private fun generateContacts(): MutableList<Contact> {
        for (i in 1..100) {
            contacts.add(Contact(i,
                "Имя$i",
                "Фамилия$i",
                "8-${Random.nextInt(900,999)}-${Random.nextInt(100,999)}-${Random.nextInt(10,99)}-${Random.nextInt(10,99)}"))
        }
        return contacts
    }

    private fun showAddContactDialog() {
        val dialog = AddContactDialogFragment()
        dialog.show(supportFragmentManager, "AddContactDialogFragment")
    }

    private fun showEditContactDialog(contact: Contact) {
        val dialog = EditContactDialogFragment.newInstance(contact)
        dialog.show(supportFragmentManager, "EditContactDialogFragment")
    }

    override fun onContactAdded(contact: Contact) {

        val deletedContacts = (1..contacts.size).filterNot { it in contacts.map { it.id } }

        val newContact = if (deletedContacts.isNotEmpty()) {
            contact.copy(id=deletedContacts.first())
        } else {
            contact.copy(id = contactAdapter.currentList.size + 1)
        }
        contacts.add(newContact)
        contactAdapter.submitList(contacts)
    }
    override fun onContactEdited(contact: Contact) {

        contacts = contactAdapter.currentList.toMutableList()
        val index = contacts.indexOfFirst { it.id == contact.id }
        if (index != -1) {
            contacts[index] = contact
            contactAdapter.submitList(contacts)
        }
    }

    private fun toggleSelectionMode() {
        isSelectionMode = !isSelectionMode
        if (isSelectionMode) {
            contactAdapter.setCheckboxMode(true)

            deleteButton.visibility = View.GONE
            cancelButton.visibility = View.VISIBLE
            agreeButton.visibility = View.VISIBLE
            addContactButton.visibility = View.GONE

        } else {
            contactAdapter.setCheckboxMode(false)

            deleteButton.visibility = View.VISIBLE
            cancelButton.visibility = View.GONE
            agreeButton.visibility = View.GONE
            addContactButton.visibility = View.VISIBLE
        }
    }

    private fun exitSelectionMode() {
        toggleSelectionMode()
    }

    private fun deleteSelectedContacts() {
        contacts=contactAdapter.currentList.filter { !it.isSelected }.toMutableList()
        contactAdapter.submitList(contacts)

        exitSelectionMode()
    }

    override fun onBackPressed() {
        if (isSelectionMode)
            toggleSelectionMode()
        else
            super.onBackPressed()
    }
}
