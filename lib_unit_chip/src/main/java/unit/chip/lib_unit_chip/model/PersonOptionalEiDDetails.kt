package unit.chip.lib_unit_chip.model

import android.graphics.Bitmap
import java.util.ArrayList


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class PersonOptionalEiDDetails(
    var endorsementsAndObservations: String? = null,
    var dateAndTimeOfPersonalization: String? = null,
    var dateOfIssue: String? = null,
    var imageOfFront: Bitmap? = null,
    var imageOfRear: Bitmap? = null,
    var issuingAuthority: String? = null,
    var namesOfOtherPersons: List<String>? = ArrayList(),
    var personalizationSystemSerialNumber: String? = null,
    var taxOrExitRequirements: String? = null,
    var tag: Int = 0,
    var tagPresenceList: List<Int>? = ArrayList()
)