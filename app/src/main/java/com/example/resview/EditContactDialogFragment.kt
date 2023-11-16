package com.example.resview

import android.content.Context
import android.os.Bundle
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment

class EditContactDialogFragment : DialogFragment() {

    interface EditContactDialogListener {
        fun onContactEdited(contact: Contact)
    }

    private lateinit var listener: EditContactDialogListener

    private lateinit var contact: Contact

    override fun onAttach(context: Context) {
        super.onAttach(context)
        try {
            listener = context as EditContactDialogListener
        } catch (e: ClassCastException) {
            throw ClassCastException("$context must implement EditContactDialogListener")
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            contact = it.getParcelable(ARG_CONTACT) ?: throw IllegalArgumentException("Contact cannot be null")
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): AlertDialog {
        val inflater = requireActivity().layoutInflater
        val view = inflater.inflate(R.layout.dialog_add_contact, null)

        val firstNameEditText = view.findViewById<EditText>(R.id.editTextFirstName)
        val lastNameEditText = view.findViewById<EditText>(R.id.editTextLastName)
        val phoneNumberEditText = view.findViewById<EditText>(R.id.editTextPhoneNumber)

        firstNameEditText.setText(contact.firstName)
        lastNameEditText.setText(contact.lastName)
        phoneNumberEditText.setText(contact.phoneNumber)

        return AlertDialog.Builder(requireContext())
            .setTitle("Редактирование")
            .setView(view)
            .setPositiveButton("Сохранить") { _, _ ->
                val editedFirstName = firstNameEditText.text.toString()
                val editedLastName = lastNameEditText.text.toString()
                val editedPhoneNumber = phoneNumberEditText.text.toString()

                val editedContact = contact.copy(
                    firstName = editedFirstName,
                    lastName = editedLastName,
                    phoneNumber = editedPhoneNumber
                )

                listener.onContactEdited(editedContact)
            }
            .setNegativeButton("Отмена") { _, _ -> }
            .create()
    }

    companion object {
        private const val ARG_CONTACT = "contact"

        fun newInstance(contact: Contact): EditContactDialogFragment {
            val fragment = EditContactDialogFragment()
            val args = Bundle()
            args.putParcelable(ARG_CONTACT, contact)
            fragment.arguments = args
            return fragment
        }
    }
}