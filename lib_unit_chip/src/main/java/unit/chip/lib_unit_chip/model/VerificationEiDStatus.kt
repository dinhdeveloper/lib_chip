package unit.chip.lib_unit_chip.model

import org.jmrtd.BACKey
import org.jmrtd.protocol.EACCAResult
import org.jmrtd.protocol.EACTAResult
import java.security.cert.Certificate


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


data class VerificationEiDStatus(
    var activeAuth: Verdict = Verdict.UNKNOWN,
    var bac: Verdict = Verdict.UNKNOWN,
    var secureAccessControl: Verdict = Verdict.UNKNOWN,
    var countrysign: Verdict = Verdict.UNKNOWN,
    var passiveAuthentication: Verdict = Verdict.UNKNOWN,
    var documentSigning: Verdict = Verdict.UNKNOWN,
    var eac: Verdict = Verdict.UNKNOWN,
    var chipAuth: Verdict = Verdict.UNKNOWN,
    var triedBACEntries: List<BACKey>? = null,
    var hashResults: Map<Int, HashMatchResult>? = null,
    var certificateChain: List<Certificate>? = null,
    var eacResult: EACTAResult? = null,
    var caResult: EACCAResult? = null
) {

    enum class Verdict {
        UNKNOWN,
        NOT_PRESENT,
        NOT_CHECKED,
        FAILED,
        SUCCEEDED
    }

    data class HashMatchResult(
        val storedHash: ByteArray,
        val computedHash: ByteArray?
    )
}
