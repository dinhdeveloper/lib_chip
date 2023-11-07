package unit.chip.lib_unit_chip.nfc

import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.NfcError


/**
 * Created by DinhTC on 11/7/2023.
 * Skype: 0975469232
 */


abstract class NfcCallback {
    abstract fun onSuccess(nfcResult: CardEiD?)
    abstract fun onError(nfcError: NfcError?)
}