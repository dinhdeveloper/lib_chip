package unit.chip.lib_unit_chip.public_release

import android.graphics.Bitmap
import android.os.Parcel
import android.os.Parcelable
import org.jmrtd.lds.SODFile


/**
 * Created by DinhTC on 11/9/2023.
 * Skype: 0975469232
 */


data class ChipResult(
    var face: Bitmap? = null,
    var portrait: Bitmap? = null,
    var signature: Bitmap? = null,
    var fingerprints: List<Bitmap>? = null,
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
    var facePath: String = "",
    var partnerName: String = "",
    var unkIdNumber: String = "",
    var dsCert : String = "",
    var certificate : String = "",
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader),
        parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader),
        parcel.readParcelable<Bitmap>(Bitmap::class.java.classLoader),
        parcel.createTypedArrayList(Bitmap.CREATOR),
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
        parcel.readString() ?: "",
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(face, flags)
        parcel.writeParcelable(portrait, flags)
        parcel.writeParcelable(signature, flags)
        parcel.writeTypedList(fingerprints)
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
        parcel.writeString(facePath)
        parcel.writeString(partnerName)
        parcel.writeString(unkIdNumber)
        parcel.writeString(dsCert)
        parcel.writeString(certificate)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<ChipResult> {
        override fun createFromParcel(parcel: Parcel): ChipResult {
            return ChipResult(parcel)
        }

        override fun newArray(size: Int): Array<ChipResult?> {
            return arrayOfNulls(size)
        }
    }
}
