package unit.chip.lib_unit_chip.nfc

import net.sf.scuba.smartcards.CardServiceException
import org.jmrtd.AccessDeniedException
import org.jmrtd.BACDeniedException
import org.jmrtd.PACEException
import unit.chip.lib_unit_chip.model.CardEiD


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


interface CardEidCallback {
    fun onCardEidRead(passport: CardEiD?)
    fun onAccessDeniedException(exception: AccessDeniedException)
    fun onBACDeniedException(exception: BACDeniedException)
    fun onPACEException(exception: PACEException)
    fun onCardException(exception: CardServiceException)
    fun onGeneralException(exception: Exception?)
}