package unit.chip.lib_unit_chip.public_release


/**
 * Created by DinhTC on 11/7/2023.
 * Skype: 0975469232
 */


abstract class NfcCallback {
    abstract fun onSuccess(nfcResult: ChipResult?)
    abstract fun onError(nfcError: NfcError?)
}