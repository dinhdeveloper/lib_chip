package unit.chip.lib_unit_chip.model

import android.graphics.Bitmap
import android.os.Parcelable
import org.jmrtd.lds.SODFile
import android.os.Parcel
import java.util.ArrayList

/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */

class CardEiD : Parcelable {

    var sodFile: SODFile? = null
    var face: Bitmap? = null
    var portrait: Bitmap? = null
    var signature: Bitmap? = null
    var fingerprints: List<Bitmap>? = null
    var personDetails: PersonDetails? = null
    var additionalPersonDetails: AdditionalPersonDetails? = null
    var personOptionalDetails: PersonOptionalDetails? = null
    var featureStatus: FeatureStatus? = null
    var verificationStatus: VerificationStatus? = null

    constructor(parcel: Parcel) {


        fingerprints = ArrayList()
        this.face =
            if (parcel.readInt() == 1) parcel.readParcelable(Bitmap::class.java.classLoader) else null
        this.portrait =
            if (parcel.readInt() == 1) parcel.readParcelable(Bitmap::class.java.classLoader) else null
        this.personDetails =
            if (parcel.readInt() == 1) parcel.readParcelable(PersonDetails::class.java.classLoader) else null
        this.additionalPersonDetails =
            if (parcel.readInt() == 1) parcel.readParcelable(AdditionalPersonDetails::class.java.classLoader) else null

        if (parcel.readInt() == 1) {
            parcel.readList(fingerprints!!, Bitmap::class.java.classLoader)
        }

        this.signature =
            if (parcel.readInt() == 1) parcel.readParcelable(Bitmap::class.java.classLoader) else null
        this.personOptionalDetails =
            if (parcel.readInt() == 1) parcel.readParcelable(PersonOptionalDetails::class.java.classLoader) else null
        if (parcel.readInt() == 1) {
            sodFile = parcel.readSerializable() as SODFile
        }

        if (parcel.readInt() == 1) {
            featureStatus = parcel.readParcelable(FeatureStatus::class.java.classLoader)
        }

        if (parcel.readInt() == 1) {
            featureStatus = parcel.readParcelable(FeatureStatus::class.java.classLoader)
        }

        if (parcel.readInt() == 1) {
            verificationStatus = parcel.readParcelable(VerificationStatus::class.java.classLoader)
        }

    }

    constructor() {
        fingerprints = ArrayList()
        featureStatus = FeatureStatus()
        verificationStatus = VerificationStatus()
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(if (face != null) 1 else 0)
        if (face != null) {
            dest.writeParcelable(face, flags)
        }

        dest.writeInt(if (portrait != null) 1 else 0)
        if (portrait != null) {
            dest.writeParcelable(portrait, flags)
        }

        dest.writeInt(if (personDetails != null) 1 else 0)
        if (personDetails != null) {
            dest.writeParcelable(personDetails, flags)
        }

        dest.writeInt(if (additionalPersonDetails != null) 1 else 0)
        if (additionalPersonDetails != null) {
            dest.writeParcelable(additionalPersonDetails, flags)
        }

        dest.writeInt(if (fingerprints != null) 1 else 0)
        if (fingerprints != null) {
            dest.writeList(fingerprints)
        }

        dest.writeInt(if (signature != null) 1 else 0)
        if (signature != null) {
            dest.writeParcelable(signature, flags)
        }

        dest.writeInt(if (personOptionalDetails != null) 1 else 0)
        if (personOptionalDetails != null) {
            dest.writeParcelable(personOptionalDetails, flags)
        }

        dest.writeInt(if (sodFile != null) 1 else 0)
        if (sodFile != null) {
            dest.writeSerializable(sodFile)
        }

        dest.writeInt(if (featureStatus != null) 1 else 0)
        if (featureStatus != null) {
            dest.writeParcelable(featureStatus, flags)
        }

        dest.writeInt(if (verificationStatus != null) 1 else 0)
        if (verificationStatus != null) {
            dest.writeParcelable(verificationStatus, flags)
        }

    }

    companion object {

        @JvmField
        val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<CardEiD> {
            override fun createFromParcel(pc: Parcel): CardEiD {
                return CardEiD(pc)
            }

            override fun newArray(size: Int): Array<CardEiD?> {
                return arrayOfNulls(size)
            }
        }
    }
}
