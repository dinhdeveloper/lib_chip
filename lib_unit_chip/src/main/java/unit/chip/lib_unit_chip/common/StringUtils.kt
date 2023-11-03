package unit.chip.lib_unit_chip.common

import android.os.Build
import androidx.annotation.RequiresApi
import java.security.cert.Certificate
import java.security.cert.CertificateEncodingException
import java.util.Base64


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


object StringUtils {

    private val hexArray = "0123456789ABCDEF".toCharArray()
    fun bytesToHex(bytes: ByteArray): String {
        val hexChars = CharArray(bytes.size * 2)
        for (j in bytes.indices) {
            val v = bytes[j].toInt() and 0xFF
            hexChars[j * 2] = hexArray[v.ushr(4)]
            hexChars[j * 2 + 1] = hexArray[v and 0x0F]
        }
        return String(hexChars)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun convertToBase64(certificate: Certificate): String? {
        try {
            val certificateBytes = certificate.encoded
            return Base64.getEncoder().encodeToString(certificateBytes)
        } catch (e: CertificateEncodingException) {
            e.printStackTrace()
        }
        return null
    }
}