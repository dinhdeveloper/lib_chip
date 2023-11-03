package unit.chip.lib_unit_chip.common

import android.nfc.Tag
import android.nfc.tech.IsoDep
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import net.sf.scuba.smartcards.CardService
import net.sf.scuba.smartcards.CardServiceException
import org.jmrtd.BACDeniedException
import org.jmrtd.PACEException
import org.jmrtd.PassportService
import org.jmrtd.lds.icao.DG1File
import org.jmrtd.lds.icao.MRZInfo
import unit.chip.lib_unit_chip.model.AdditionalPersonEiDDetails
import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.PersonEiDDetails
import unit.chip.lib_unit_chip.nfc.CardEidCallback
import unit.chip.lib_unit_chip.nfc.CardEidDTO
import unit.chip.lib_unit_chip.nfc.CardEidNFC
import unit.chip.lib_unit_chip.nfc.CardNfcUtils
import java.security.Security


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class NfcTagTool {

    init {
        Security.insertProviderAt(org.spongycastle.jce.provider.BouncyCastleProvider(), 1)
    }

    suspend fun handleNFC(tag: Tag, mrzInfo: MRZInfo, cardCallback: CardEidCallback): CardEidDTO = coroutineScope {
        return@coroutineScope try {
            val cardEidDTO = withContext(Dispatchers.IO) {
                var cardEid : CardEiD? = null
                var cardServiceException: Exception? = null
                var passportService: PassportService? = null
                try {
                    val nfc = IsoDep.get(tag)
                    nfc.timeout = nfc.timeout.coerceAtLeast(2000)
                    val cardService = CardService.getInstance(nfc)
                    passportService = PassportService(
                        cardService,
                        PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                        PassportService.NORMAL_MAX_TRANCEIVE_LENGTH,
                        PassportService.DEFAULT_MAX_BLOCKSIZE,
                        false,
                        false
                    )
                    passportService.open()

                    val cardEidNFC = CardEidNFC(passportService, mrzInfo, PassportService.DEFAULT_MAX_BLOCKSIZE)
                    cardEid = CardEiD()
                    cardEid.featureStatus = cardEidNFC.features
                    cardEid.verificationStatus = cardEidNFC.verificationStatus
                    cardEid.sodFile = cardEidNFC.sodFile

                    //Basic Information
                    if (cardEidNFC.dg1File != null) {
                        val mrzInfo = (cardEidNFC.dg1File as DG1File).mrzInfo
                        val personDetails = PersonEiDDetails()
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

                    //Picture
                    if (cardEidNFC.dg2File != null) {
                        try {
                            val faceImage =  CardNfcUtils.retrieveFaceImage(cardEidNFC.dg2File!!)
                            cardEid.face = faceImage
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }
                    }
                    //Portrait Get the picture
                    if (cardEidNFC.dg5File != null) {
                        try {
                            val faceImage = CardNfcUtils.retrievePortraitImage(cardEidNFC.dg5File!!)
                            cardEid.portrait = faceImage
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    //Finger prints Get the pictures
                    if (cardEidNFC.dg3File != null) {
                        try {
                            val bitmaps = CardNfcUtils.retrieveFingerPrintImage(
                                cardEidNFC.dg3File!!
                            )
                            cardEid.fingerprints = bitmaps
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    //Signature Get the pictures
                    if (cardEidNFC.dg7File != null) {
                        //Get the picture
                        try {
                            val bitmap = CardNfcUtils.retrieveSignatureImage(cardEidNFC.dg7File!!)
                            cardEid.signature = bitmap
                        } catch (e: Exception) {
                            e.printStackTrace()
                        }

                    }
                    val dataGR13 = cardEidNFC.cardFileInputStr
                    if (dataGR13 != null){
                        val personDetails = AdditionalPersonEiDDetails()
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
                        //personDetails.partnerName = dataGR13.partnerName!!
                        personDetails.oldNumber = dataGR13.oldNumber!!
                        personDetails.unkIdNumber = dataGR13.unkIdNumber!!

                        cardEid.additionalPersonDetails = personDetails
                    }
                } catch (e: Exception) {
                    cardServiceException = e
                } finally {
                    try {
                        passportService?.close()
                    } catch (ex: Exception) {
                        ex.printStackTrace()
                    }
                }
                CardEidDTO(cardEid, cardServiceException)
            }
            cardCallback.onCardEidRead(cardEidDTO.cardEiD)
            cardEidDTO
        } catch (e: Exception) {
            when (e) {
                is AccessDeniedException -> cardCallback.onAccessDeniedException(
                    e as org.jmrtd.AccessDeniedException
                )
                is BACDeniedException -> cardCallback.onBACDeniedException(e)
                is PACEException -> cardCallback.onPACEException(e)
                is CardServiceException -> cardCallback.onCardException(e)
                else -> cardCallback.onGeneralException(e)
            }
            CardEidDTO(null, e)
        }
    }
}