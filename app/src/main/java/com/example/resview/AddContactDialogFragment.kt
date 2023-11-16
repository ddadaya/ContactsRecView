package com.example.resview

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class AddContactDialogFragment : DialogFragment() {

    interface AddContactDialogListener {
        fun onContactAdded(contact: Contact)
    }

    private lateinit var listener: AddContactDialogListener

    override fun onAttach(context: Context) {
        super.onAttach(context)
            listener = context as AddContactDialogListener
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_contact, null)

        val firstNameEditText = view.findViewById<EditText>(R.id.editTextFirstName)
        val lastNameEditText = view.findViewById<EditText>(R.id.editTextLastName)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.editTextPhoneNumber)

        return AlertDialog.Builder(requireContext())
            .setTitle("Добавление контакта")
            .setView(view)
            .setPositiveButton("Добавить") { _, _ ->
                val firstName = firstNameEditText.text.toString()
                val lastName = lastNameEditText.text.toString()
                val phoneNumber = phoneNumberEditText.text.toString()

                val newContact = Contact(id, firstName, lastName, phoneNumber)

                listener.onContactAdded(newContact)
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .create()
    }
}