package unit.chip.lib_unit_chip.model

import android.os.Parcelable
import android.os.Parcel


/**
 * Created by DinhTC on 11/7/2023.
 * Skype: 0975469232
 */



data class NfcOption(
    val accessToken: String = "",
    val tokenId: String = "",
    val tokenKey: String = "",
    val documentNumber: String = "",
    val dateOfBirth: String = "",
    val dateOfExpiry: String = ""
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(accessToken)
        parcel.writeString(tokenId)
        parcel.writeString(tokenKey)
        parcel.writeString(documentNumber)
        parcel.writeString(dateOfBirth)
        parcel.writeString(dateOfExpiry)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<NfcOption> {
        override fun createFromParcel(parcel: Parcel): NfcOption {
            return NfcOption(parcel)
        }

        override fun newArray(size: Int): Array<NfcOption?> {
            return arrayOfNulls(size)
        }
    }
}
