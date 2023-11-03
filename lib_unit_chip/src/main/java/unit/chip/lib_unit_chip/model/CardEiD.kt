package unit.chip.lib_unit_chip.model

import android.graphics.Bitmap
import org.jmrtd.lds.SODFile


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class CardEiD(
    var sodFile: SODFile? = null,
    var face: Bitmap? = null,
    var portrait: Bitmap? = null,
    var signature: Bitmap? = null,
    var fingerprints: List<Bitmap>? = null,
    var personDetails: PersonEiDDetails? = null,
    var additionalPersonDetails: AdditionalPersonEiDDetails? = null,
    var personOptionalDetails: PersonOptionalEiDDetails? = null,
    var featureStatus: FeatureEiDStatus? = null,
    var verificationStatus: VerificationEiDStatus? = null
)