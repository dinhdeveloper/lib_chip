package unit.chip.lib_unit_chip.model


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


import android.os.Parcel
import android.os.Parcelable

data class FeatureEiDStatus(
    var hasSAC: Verdict = Verdict.UNKNOWN,
    var hasBAC: Verdict = Verdict.UNKNOWN,
    var hasAA: Verdict = Verdict.UNKNOWN,
    var hasEAC: Verdict = Verdict.UNKNOWN,
    var hasCA: Verdict = Verdict.UNKNOWN
) {

    enum class Verdict {
        UNKNOWN, /* Presence unknown */
        PRESENT, /* Present */
        NOT_PRESENT
        /* Not present */
    }

    constructor(parcel: Parcel) : this(
        Verdict.values()[parcel.readInt()],
        Verdict.values()[parcel.readInt()],
        Verdict.values()[parcel.readInt()],
        Verdict.values()[parcel.readInt()],
        Verdict.values()[parcel.readInt()]
    )
}
