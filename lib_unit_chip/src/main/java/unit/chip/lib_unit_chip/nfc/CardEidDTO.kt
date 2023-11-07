package unit.chip.lib_unit_chip.nfc

import unit.chip.lib_unit_chip.model.CardEiD


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class CardEidDTO(val cardEiD: CardEiD? = null, val cardServiceException: NfcCallback? = null)