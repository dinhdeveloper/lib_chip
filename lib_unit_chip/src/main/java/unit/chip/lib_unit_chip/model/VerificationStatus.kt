package unit.chip.lib_unit_chip.model

import android.os.Parcel
import android.os.Parcelable
import net.sf.scuba.util.Hex
import org.jmrtd.BACKey
import org.jmrtd.protocol.EACCAResult
import org.jmrtd.protocol.EACTAResult
import java.io.IOException
import java.io.ObjectInputStream
import java.io.ObjectOutputStream
import java.io.Serializable
import java.security.cert.Certificate
import java.util.ArrayList
import java.util.Arrays
import java.util.TreeMap


/**
 * Created by DinhTC on 10/31/2023.
 * Skype: 0975469232
 */


class VerificationStatus : Parcelable {

    /* Verdict for this verification feature. */
    /**
     * Gets the AA verdict.
     *
     * @return the AA status
     */
    var activeAuth: Verdict? = null
        private set

    /**
     * Gets the BAC verdict.
     *
     * @return the BAC status
     */
    var bac: Verdict? = null
        private set

    /**
     * Gets the SAC verdict.
     *
     * @return the SAC verdict
     */
    var secureAccessControl: Verdict? = null
        private set

    /**
     * Gets the CS verdict.
     *
     * @return the CS status
     */
    var countrysign: Verdict? = null
        private set

    /**
     * Gets the hash table verdict.
     *
     * @return a verdict
     */
    var passiveAuthentication: Verdict? = null
        private set

    /**
     * Gets the DS verdict.
     *
     * @return the DS status
     */
    var documentSigning: Verdict? = null
        private set

    /**
     * Gets the EAC verdict.
     *
     * @return the EAC status
     */
    var eac: Verdict? = null
        private set

    /**
     * Gets the CA verdict.
     *
     * @return the CA status
     */
    var chipAuth: Verdict? = null
        private set


    /* By products of the verification process that may be useful for relying parties to display. */
    private var triedBACEntries: List<BACKey>? =
        null /* As a result of BAC testing, this contains all tried BAC entries. */
    var hashResults: MutableMap<Int, HashMatchResult>? =
        null /* As a result of HT testing, this contains stored and computed hashes. */
    private var certificateChain: List<Certificate>? =
        null /* As a result of CS testing, this contains certificate chain from DSC to CSCA. */

    /**
     * Gets the EAC result.
     *
     * @return the EAC result
     */
    var eacResult: EACTAResult? = null
        private set

    /**
     * Gets the CA result.
     *
     * @return the CA result
     */
    var caResult: EACCAResult? = null
        private set

    /**
     * Outcome of a verification process.
     *
     * @author The JMRTD team (info@jmrtd.org)
     *
     * @version $Revision: 1559 $
     */
    enum class Verdict {
        UNKNOWN, /* Unknown */
        NOT_PRESENT, /* Not present */
        NOT_CHECKED, /* Present, not checked */
        FAILED, /* Present, checked, and not ok */
        SUCCEEDED
        /* Present, checked, and ok */
    }

    /**
     * Constructs a new status with all verdicts
     * set to UNKNOWN.
     */
    constructor() {
        setAll(Verdict.UNKNOWN)
    }
    fun setActiveAuth(v: Verdict) {
        this.activeAuth = v
    }
    fun setBAC(v: Verdict,triedBACEntries: List<BACKey>?) {
        this.bac = v
        this.triedBACEntries = triedBACEntries
    }
    fun setSAC(v: Verdict) {
        this.secureAccessControl = v
    }
    fun setCountrySigning(v: Verdict, certificateChain: List<Certificate>?) {
        this.countrysign = v
        this.certificateChain = certificateChain
    }
    fun setDocumentSigning(v: Verdict) {
        this.documentSigning = v
    }
    fun setPassiveAuthentication(v: Verdict, hashResults: MutableMap<Int, HashMatchResult>?) {
        this.passiveAuthentication = v
        this.hashResults = hashResults
    }
    fun setEAC(v: Verdict, eacResult: EACTAResult?) {
        this.eac = v
        this.eacResult = eacResult
    }
    fun setChipAuth(v: Verdict, eaccaResult: EACCAResult?) {
        this.chipAuth = v
        this.caResult = eaccaResult
    }
    fun setAll(verdict: Verdict) {
        setActiveAuth(verdict)
        setBAC(verdict, null)
        setCountrySigning(verdict, null)
        setDocumentSigning(verdict)
        setPassiveAuthentication(verdict, null)
        setEAC(verdict, null)
    }

    fun getTriedBACEntries(): List<*>? {
        return triedBACEntries
    }
    fun getCertificateChain(): List<*>? {
        return certificateChain
    }
//    fun getHashResults(): MutableMap<Int, HashMatchResult>? {
//        return hashResults
//    }

    constructor(parcel: Parcel) {
        this.activeAuth = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.bac = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.secureAccessControl = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.countrysign = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.passiveAuthentication = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.documentSigning = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.eac = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null
        this.chipAuth = if (parcel.readInt() == 1) Verdict.valueOf(parcel.readString()!!) else null

        if (parcel.readInt() == 1) {
            triedBACEntries = ArrayList()
            parcel.readList(triedBACEntries!!, BACKey::class.java.classLoader)
        }

        if (parcel.readInt() == 1) {
            hashResults = TreeMap()
            val size = parcel.readInt()
            for (i in 0 until size) {
                val key = parcel.readInt()
                val value = parcel.readSerializable() as HashMatchResult
                hashResults!![key] = value
            }
        }

        if (parcel.readInt() == 1) {
            certificateChain = ArrayList()
            parcel.readList(certificateChain!!, Certificate::class.java.classLoader)
        }

        if (parcel.readInt() == 1) {
            eacResult = parcel.readSerializable() as EACTAResult
        }

        if (parcel.readInt() == 1) {
            caResult = parcel.readSerializable() as EACCAResult
        }
    }

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(if (this.activeAuth != null) 1 else 0)
        if (activeAuth != null) {
            dest.writeString(activeAuth?.name)
        }
        dest.writeInt(if (this.bac != null) 1 else 0)
        if (bac != null) {
            dest.writeString(bac?.name)
        }
        dest.writeInt(if (this.secureAccessControl != null) 1 else 0)
        if (secureAccessControl != null) {
            dest.writeString(secureAccessControl?.name)
        }
        dest.writeInt(if (this.countrysign != null) 1 else 0)
        if (countrysign != null) {
            dest.writeString(countrysign?.name)
        }
        dest.writeInt(if (this.passiveAuthentication != null) 1 else 0)
        if (passiveAuthentication != null) {
            dest.writeString(passiveAuthentication?.name)
        }
        dest.writeInt(if (this.documentSigning != null) 1 else 0)
        if (documentSigning != null) {
            dest.writeString(documentSigning?.name)
        }
        dest.writeInt(if (this.eac != null) 1 else 0)
        if (eac != null) {
            dest.writeString(eac?.name)
        }
        dest.writeInt(if (this.chipAuth != null) 1 else 0)
        if (chipAuth != null) {
            dest.writeString(chipAuth?.name)
        }
        dest.writeInt(if (triedBACEntries != null) 1 else 0)
        if (triedBACEntries != null) {
            dest.writeList(triedBACEntries)
        }

        dest.writeInt(if (hashResults != null) 1 else 0)
        if (hashResults != null) {
            dest.writeInt(hashResults!!.size)
            for ((key, value) in hashResults!!) {
                dest.writeInt(key)
                dest.writeSerializable(value)
            }
        }
        dest.writeInt(if (certificateChain != null) 1 else 0)
        if (certificateChain != null) {
            dest.writeList(certificateChain)
        }

        dest.writeInt(if (eacResult != null) 1 else 0)
        if (eacResult != null) {
            dest.writeSerializable(eacResult)
        }

        dest.writeInt(if (caResult != null) 1 else 0)
        if (caResult != null) {
            dest.writeSerializable(caResult)
        }
    }


    /**
     * The result of matching the stored and computed hashes of a single datagroup.
     */
    class HashMatchResult(storedHash: ByteArray, computedHash: ByteArray?) : Serializable {
        /**
         * Gets the stored hash.
         *
         * @return a hash
         */
        var storedHash: ByteArray? = null
            private set

        /**
         * Gets the computed hash.
         *
         * @return a hash
         */
        var computedHash: ByteArray? = null
            private set

        /**
         * Whether the hashes match.
         *
         * @return a boolean
         */
        val isMatch: Boolean
            get() = Arrays.equals(storedHash, computedHash)

        init {
            this.storedHash = storedHash
            this.computedHash = computedHash
        }

        override fun toString(): String {
            return "AAAAAAA_HashResult [" + isMatch + ", stored: " + Hex.bytesToHexString(storedHash) + ", computed: " + Hex.bytesToHexString(
                computedHash
            )
        }

        override fun hashCode(): Int {
            return 11 + 3 * Arrays.hashCode(storedHash) + 5 * Arrays.hashCode(computedHash)
        }

        override fun equals(other: Any?): Boolean {
            if (other == null) {
                return false
            }
            if (other === this) {
                return true
            }
            if (other.javaClass != this.javaClass) {
                return false
            }
            val otherHashResult = other as HashMatchResult?
            return Arrays.equals(otherHashResult!!.computedHash, computedHash) && Arrays.equals(
                otherHashResult.storedHash,
                storedHash
            )
        }

        /* NOTE: Part of our serializable implementation. */
        @Throws(IOException::class, ClassNotFoundException::class)
        private fun readObject(inputStream: ObjectInputStream) {
            //			inputStream.defaultReadObject();
            storedHash = readBytes(inputStream)
            computedHash = readBytes(inputStream)
        }

        /* NOTE: Part of our serializable implementation. */
        @Throws(IOException::class)
        private fun writeObject(outputStream: ObjectOutputStream) {
            //			outputStream.defaultWriteObject();
            writeByteArray(storedHash, outputStream)
            writeByteArray(computedHash, outputStream)
        }

        @Throws(IOException::class)
        private fun readBytes(inputStream: ObjectInputStream): ByteArray? {
            val length = inputStream.readInt()
            if (length < 0) {
                return null
            }
            val bytes = ByteArray(length)
            for (i in 0 until length) {
                val b = inputStream.readInt()
                bytes[i] = b.toByte()
            }
            return bytes
        }

        @Throws(IOException::class)
        private fun writeByteArray(bytes: ByteArray?, outputStream: ObjectOutputStream) {
            if (bytes == null) {
                outputStream.writeInt(-1)
            } else {
                outputStream.writeInt(bytes.size)
                for (b in bytes) {
                    outputStream.writeInt(b.toInt())
                }
            }
        }

        companion object {
            private const val serialVersionUID = 263961258911936111L
        }
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<*> = object : Parcelable.Creator<VerificationStatus> {
            override fun createFromParcel(pc: Parcel): VerificationStatus {
                return VerificationStatus(pc)
            }

            override fun newArray(size: Int): Array<VerificationStatus?> {
                return arrayOfNulls(size)
            }
        }
    }
}