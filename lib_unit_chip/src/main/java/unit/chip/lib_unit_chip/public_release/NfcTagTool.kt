package unit.chip.lib_unit_chip.public_release

import android.app.Activity
import android.content.Intent
import android.nfc.NfcAdapter
import android.nfc.Tag
import android.nfc.tech.IsoDep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import net.sf.scuba.data.Gender
import net.sf.scuba.smartcards.CardService
import net.sf.scuba.smartcards.CardServiceException
import org.jmrtd.BACDeniedException
import org.jmrtd.PACEException
import org.jmrtd.PassportService
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.MRZInfo
import unit.chip.lib_unit_chip.common.StringUtils
import unit.chip.lib_unit_chip.model.AdditionalPersonDetails
import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.PersonDetails
import unit.chip.lib_unit_chip.nfc.CardChipNFC
import unit.chip.lib_unit_chip.nfc.CardEidDTO
import unit.chip.lib_unit_chip.nfc.CardNfcUtils
import java.security.Security
import java.util.Arrays


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class NfcTagTool private constructor() {

    private var mActivity: Activity? = null
    private var nfcAdapter: NfcAdapter? = null
    private var nfcOption: NfcOption? = null
    private var nfcCallback: NfcCallback? = null
    private var documentNumber = ""
    private var dateOfBirth = ""
    private var dateOfExpiry = ""

    init {
        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
    }

    constructor(activity: Activity) : this() {
        this.mActivity = activity
        nfcAdapter = NfcAdapter.getDefaultAdapter(activity)
    }

    fun handleNfcEvent(intent: Intent?) {
        if ("android.nfc.action.TECH_DISCOVERED" == intent?.action || "android.nfc.action.TAG_DISCOVERED" == intent?.action) {
            val tag = intent.getParcelableExtra<Tag>("android.nfc.extra.TAG")
            if (Arrays.asList(*tag?.techList).contains("android.nfc.tech.IsoDep")) {
                val nfc = IsoDep.get(tag)
                if (checkValidate()){
                    if (documentNumber.length == 12) {
                        val last9Digits = documentNumber.takeLast(9)
                        documentNumber = last9Digits
                    } else {
                        nfcCallback?.onError(NfcError.DOCUMENT_NUMBER_INVALID)
                    }
                    val mrzInfo = createDummyMrz(documentNumber, dateOfBirth, dateOfExpiry)
                    startReadChip(mrzInfo, nfc)
                }

            } else {
                nfcCallback?.onError(NfcError.TAG_INVALID)
            }
        }
    }

    private fun checkValidate(): Boolean {
        return if (dateOfBirth.length < 6 || dateOfBirth.length > 6){
            nfcCallback?.onError(NfcError.DATE_OF_BIRTH_INVALID)
            false
        }else {
            return if (dateOfExpiry.length < 6 || dateOfExpiry.length > 6){
                nfcCallback?.onError(NfcError.DATE_OF_EXPIRY_INVALID)
                false
            }else {
                true
            }
        }
    }

    private fun createDummyMrz(
        documentNumber: String,
        dateOfBirthDay: String,
        expirationDate: String
    ): MRZInfo {
        return MRZInfo(
            "ID",
            "VNM",
            "DUMMY",
            "DUMMY",
            documentNumber,
            "VNM",
            dateOfBirthDay,
            Gender.MALE,
            expirationDate,
            ""
        )
    }

    fun handleNFC(nfcOption: NfcOption, nfcCallback: NfcCallback) {
        this.nfcOption = nfcOption
        this.nfcCallback = nfcCallback
        if (nfcOption != null) {
            documentNumber = nfcOption.documentNumber
            dateOfBirth = nfcOption.dateOfBirth
            dateOfExpiry = nfcOption.dateOfExpiry
        }
    }

    private fun startReadChip(mrzInfo: MRZInfo, isoDep: IsoDep): CardEidDTO {
        return runBlocking {
            try {
                val cardEidDTO = withContext(Dispatchers.IO) {
                    var chipResult: ChipResult? = null
                    var passportService: PassportService? = null

                    try {
                        isoDep.timeout = isoDep.timeout.coerceAtLeast(2000)
                        val cardService = CardService.getInstance(isoDep)
                        passportService = PassportService(
                            cardService,
                            PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                            PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                            PassportService.DEFAULT_MAX_BLOCKSIZE,
                            false,
                            false
                        )
                        passportService.open()

                        val cardEidNFC = CardChipNFC(
                            passportService,
                            mrzInfo,
                            PassportService.DEFAULT_MAX_BLOCKSIZE
                        )
                        chipResult = ChipResult()
                        val cardEid = CardEiD()
                        cardEid.featureStatus = cardEidNFC.features
                        cardEid.verificationStatus = cardEidNFC.verificationStatus
                        cardEid.sodFile = cardEidNFC.sodFile
                        cardEidNFC.verifySecurity()

                        // Basic Information
                        if (cardEidNFC.dg1File != null) {
                            val mrzInfo = (cardEidNFC.dg1File as DG1File).mrzInfo
                            val personDetails = PersonDetails()
                            personDetails.dateOfBirth = mrzInfo.dateOfBirth
                            personDetails.dateOfExpiry = mrzInfo.dateOfExpiry
                            personDetails.documentCode = mrzInfo.documentCode
                            personDetails.documentNumber = mrzInfo.documentNumber
                            personDetails.optionalData1 = mrzInfo.optionalData1
                            personDetails.optionalData2 = mrzInfo.optionalData2
                            personDetails.issuingState = mrzInfo.issuingState
                            personDetails.primaryIdentifier = mrzInfo.primaryIdentifier
                            personDetails.secondaryIdentifier = mrzInfo.secondaryIdentifier
                            personDetails.nationality = mrzInfo.nationality
                            personDetails.gender = mrzInfo.gender
                            cardEid.personDetails = personDetails
                        }

                        // Picture
                        if (cardEidNFC.dg2File != null) {
                            try {
                                val faceImage =
                                    CardNfcUtils.retrieveFaceImage(cardEidNFC.dg2File!!)
                                cardEid.face = faceImage
                                chipResult.face = faceImage
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        // Portrait Get the picture
                        if (cardEidNFC.dg5File != null) {
                            try {
                                val faceImage =
                                    CardNfcUtils.retrievePortraitImage(cardEidNFC.dg5File!!)
                                cardEid.portrait = faceImage
                                chipResult.portrait = faceImage
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        // Fingerprints Get the pictures
                        if (cardEidNFC.dg3File != null) {
                            try {
                                val bitmaps =
                                    CardNfcUtils.retrieveFingerPrintImage(cardEidNFC.dg3File!!)
                                cardEid.fingerprints = bitmaps
                                chipResult.fingerprints = bitmaps
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        // Signature Get the pictures
                        if (cardEidNFC.dg7File != null) {
                            try {
                                val bitmap =
                                    CardNfcUtils.retrieveSignatureImage(cardEidNFC.dg7File!!)
                                cardEid.signature = bitmap
                                chipResult.signature = bitmap
                            } catch (e: Exception) {
                                e.printStackTrace()
                            }
                        }
                        val dataGR13 = cardEidNFC.dGr13File
                        if (dataGR13 != null) {
                            val personDetails = AdditionalPersonDetails()
                            personDetails.id = dataGR13.id!!
                            personDetails.name = dataGR13.name!!
                            personDetails.birthDay = dataGR13.birthDay!!
                            personDetails.gender = dataGR13.gender!!
                            personDetails.nationality = dataGR13.nationality!!
                            personDetails.nation = dataGR13.nation!!
                            personDetails.religion = dataGR13.religion!!
                            personDetails.homeTown = dataGR13.homeTown!!
                            personDetails.recentLocation = dataGR13.recentLocation!!
                            personDetails.description = dataGR13.description!!
                            personDetails.issueDate = dataGR13.issueDate!!
                            personDetails.expiredDate = dataGR13.expiredDate!!
                            personDetails.fatherName = dataGR13.fatherName!!
                            personDetails.motherName = dataGR13.motherName!!
                            // personDetails.partnerName = dataGR13.partnerName!!
                            personDetails.oldNumber = dataGR13.oldNumber!!
                            personDetails.unkIdNumber = dataGR13.unkIdNumber!!
                            cardEid.additionalPersonDetails = personDetails


                            chipResult.id = dataGR13.id!!
                            chipResult.name = dataGR13.name!!
                            chipResult.birthDay = dataGR13.birthDay!!
                            chipResult.gender = dataGR13.gender!!
                            chipResult.nationality = dataGR13.nationality!!
                            chipResult.nation = dataGR13.nation!!
                            chipResult.religion = dataGR13.religion!!
                            chipResult.homeTown = dataGR13.homeTown!!
                            chipResult.recentLocation = dataGR13.recentLocation!!
                            chipResult.description = dataGR13.description!!
                            chipResult.issueDate = dataGR13.issueDate!!
                            chipResult.expiredDate = dataGR13.expiredDate!!
                            chipResult.fatherName = dataGR13.fatherName!!
                            chipResult.motherName = dataGR13.motherName!!
                            chipResult.oldNumber = dataGR13.oldNumber!!
                            chipResult.unkIdNumber = dataGR13.unkIdNumber!!
                        }

                        val dsCert = cardEid.sodFile?.docSigningCertificate?.let { docSigning ->
                            StringUtils.encodeToBase64String(docSigning)
                        }

                        if (dsCert != null) {
                            chipResult.dsCert = dsCert
                        }
                        val certificateString = cardEid.sodFile?.docSigningCertificate?.let {
                            StringUtils.convertToBase64(
                                it
                            )
                        }
                        if (certificateString != null) {
                            chipResult.certificate = certificateString
                        }

                    } catch (e: Exception) {
                        nfcCallback?.onError(NfcError.READ_DATA_FAILURE)
                    } finally {
                        try {
                            passportService?.close()
                        } catch (ex: Exception) {
                            ex.printStackTrace()
                        }
                    }
                    CardEidDTO(chipResult, nfcCallback,null)
                }
                nfcCallback?.onSuccess(cardEidDTO.chipResult)
                cardEidDTO
            } catch (e: Exception) {
                when (e) {
                    is AccessDeniedException -> {
                        nfcCallback?.onError(NfcError.AUTHENTICATE_FAILURE)
                    }

                    is BACDeniedException -> {
                        nfcCallback?.onError(NfcError.AUTHENTICATE_FAILURE)
                    }

                    is PACEException -> {
                        nfcCallback?.onError(NfcError.AUTHENTICATE_FAILURE)
                    }

                    is CardServiceException -> {
                        nfcCallback?.onError(NfcError.OPEN_FAILURE)
                    }
                    else -> {
                        nfcCallback?.onError(NfcError.NFC_OPTION_NULL)
                    }
                }
                CardEidDTO(null, nfcCallback, e.printStackTrace())
            }
        }
    }
}