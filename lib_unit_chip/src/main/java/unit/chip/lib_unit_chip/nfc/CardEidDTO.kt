package unit.chip.lib_unit_chip.nfc

import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.public_release.ChipResult
import unit.chip.lib_unit_chip.public_release.NfcCallback


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class CardEidDTO(val chipResult: ChipResult? = null, val cardServiceException: NfcCallback? = null, val ex: Unit?)