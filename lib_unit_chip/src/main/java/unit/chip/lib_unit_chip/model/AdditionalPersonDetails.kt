package unit.chip.lib_unit_chip.model

import android.os.Parcel
import android.os.Parcelable


/**
 * Created by DinhTC on 10/31/2023.
 * Skype: 0975469232
 */


data class AdditionalPersonDetails(
    var birthDay: String = "",
    var id: String = "",
    var issueDate: String = "",
    var nation: String = "",
    var fatherName: String = "",
    var name: String = "",
    var gender: String = "",
    var homeTown: String = "",
    var description: String = "",
    var motherName: String = "",
    var nationality: String = "",
    var oldNumber: String = "",
    var expiredDate: String = "",
    var recentLocation: String = "",
    var religion: String = "",
    var sodBase64: String = "",
    var facePath: String = "",
    var partnerName: String = "",
    var unkIdNumber: String = ""
) : Parcelable {

    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(birthDay)
        parcel.writeString(id)
        parcel.writeString(issueDate)
        parcel.writeString(nation)
        parcel.writeString(fatherName)
        parcel.writeString(name)
        parcel.writeString(gender)
        parcel.writeString(homeTown)
        parcel.writeString(description)
        parcel.writeString(motherName)
        parcel.writeString(nationality)
        parcel.writeString(oldNumber)
        parcel.writeString(expiredDate)
        parcel.writeString(recentLocation)
        parcel.writeString(religion)
        parcel.writeString(sodBase64)
        parcel.writeString(facePath)
        parcel.writeString(partnerName)
        parcel.writeString(unkIdNumber)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<AdditionalPersonDetails> {
        override fun createFromParcel(parcel: Parcel): AdditionalPersonDetails {
            return AdditionalPersonDetails(parcel)
        }

        override fun newArray(size: Int): Array<AdditionalPersonDetails?> {
            return arrayOfNulls(size)
        }
    }
}