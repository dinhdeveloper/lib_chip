package unit.chip.lib_unit_chip.common

import android.os.Build
import androidx.annotation.RequiresApi
import org.bouncycastle.openssl.jcajce.JcaPEMWriter
import org.bouncycastle.util.io.pem.PemObject
import org.bouncycastle.util.io.pem.PemWriter
import java.io.StringWriter
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

    fun encodeToBase64String(certificate: Certificate): String? {
        val convertToBase64PEMString = certificateToBase64PEM(certificate)
        return run {
            val encoder = Base64.getEncoder()
            val bytes = convertToBase64PEMString.toByteArray(Charsets.UTF_8)
            encoder.encodeToString(bytes)
        }
    }
    private fun certificateToBase64PEM(certificate: Certificate): String {
        val stringWriter = StringWriter()
        val pemWriter = JcaPEMWriter(stringWriter)
        pemWriter.writeObject(certificate)
        pemWriter.close()

        return stringWriter.toString()
    }

}