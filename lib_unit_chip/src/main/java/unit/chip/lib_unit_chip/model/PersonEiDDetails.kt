package unit.chip.lib_unit_chip.model

import net.sf.scuba.data.Gender


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class PersonEiDDetails(
    var documentCode: String? = null,
    var issuingState: String? = null,
    var primaryIdentifier: String? = null,
    var secondaryIdentifier: String? = null,
    var nationality: String? = null,
    var documentNumber: String? = null,
    var dateOfBirth: String? = null,
    var dateOfExpiry: String? = null,
    var optionalData1: String? = null,
    var optionalData2: String? = null,
    var gender: Gender? = Gender.UNKNOWN
)