package com.example.resview

import android.os.Parcel
import android.os.Parcelable

data class Contact(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String,
    var isSelected: Boolean = false
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(firstName)
        parcel.writeString(lastName)
        parcel.writeString(phoneNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is Contact) return false

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (phoneNumber != other.phoneNumber) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + phoneNumber.hashCode()
        return result
    }

    companion object CREATOR : Parcelable.Creator<Contact> {
        override fun createFromParcel(parcel: Parcel): Contact {
            return Contact(parcel)
        }

        override fun newArray(size: Int): Array<Contact?> {
            return arrayOfNulls(size)
        }
    }
}