package unit.chip.libchip.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import unit.chip.lib_unit_chip.common.StringUtils
import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.FeatureStatus
import unit.chip.lib_unit_chip.model.VerificationStatus
import unit.chip.libchip.R
import unit.chip.libchip.base.BaseFragment
import unit.chip.libchip.databinding.FragmentDetailBinding
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Locale
import javax.security.auth.x500.X500Principal


/**
 * Created by DinhTC on 10/31/2023.
 * Skype: 0975469232
 */


class DetailFragment : BaseFragment<FragmentDetailBinding>() {

    private var receivedPassport: CardEiD? = null
    private var simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override val layoutResourceId: Int
        get() = R.layout.fragment_detail

    override fun onViewCreated() {
        receivedPassport = arguments?.getParcelable("KEY_PASSPORT")
        if (receivedPassport != null) {
            renderUI(receivedPassport!!)
        }
    }

    private fun renderUI(cardEiD: CardEiD) {
        displayAuthenticationStatus(cardEiD.verificationStatus, cardEiD.featureStatus!!)

        val personDetails = cardEiD.personDetails
        if (personDetails != null) {
            val name = personDetails.primaryIdentifier!!.replace("<", "")
            val surname = personDetails.secondaryIdentifier!!.replace("<", "")
            viewBinding.valueName.text = getString(R.string.name, name, surname)
            viewBinding.valueDOB.text = personDetails.dateOfBirth
            viewBinding.valueGender.text = personDetails.gender?.name
            viewBinding.valuePassportNumber.text = personDetails.documentNumber
            viewBinding.valueExpirationDate.text = personDetails.dateOfExpiry
            viewBinding.valueIssuingState.text = personDetails.issuingState
            viewBinding.valueNationality.text = personDetails.nationality
        }

        if (cardEiD.face != null) {
            viewBinding.iconPhoto.setImageBitmap(cardEiD.face)
        } else if (cardEiD.portrait != null) {
            viewBinding.iconPhoto.setImageBitmap(cardEiD.portrait)
        }

        cardEiD.additionalPersonDetails?.apply {
            viewBinding.id.text = id
            viewBinding.birthDay.text = birthDay
            viewBinding.description.text = description
            viewBinding.expiredDate.text = expiredDate
            viewBinding.fatherName.text = fatherName
            viewBinding.gender.text = gender
            viewBinding.homeTown.text = homeTown
            viewBinding.issueDate.text = issueDate
            viewBinding.motherName.text = motherName
            viewBinding.name.text = name
            viewBinding.nation.text = nation
            viewBinding.nationality.text = nationality
            viewBinding.oldNumber.text = oldNumber
            viewBinding.recentLocation.text = recentLocation
            viewBinding.religion.text = religion
            viewBinding.valueUnk.text = unkIdNumber
        }

        val sodFile = cardEiD.sodFile
        if (sodFile != null) {
            val countrySigningCertificate = sodFile.issuerX500Principal
            val dnRFC2253 = countrySigningCertificate.getName(X500Principal.RFC2253)
            val dnCANONICAL = countrySigningCertificate.getName(X500Principal.CANONICAL)
            val dnRFC1779 = countrySigningCertificate.getName(X500Principal.RFC1779)

            val name = countrySigningCertificate.name
            //new X509Certificate(countrySigningCertificate);

            val docSigningCertificate = sodFile.docSigningCertificate

            if (docSigningCertificate != null) {
                viewBinding.serialNumber.text = docSigningCertificate.serialNumber.toString()
                viewBinding.publicKey.text = docSigningCertificate.publicKey.algorithm
                viewBinding.sigAlgName.text = docSigningCertificate.sigAlgName

                try {
                    viewBinding.thumbprint.text = StringUtils.bytesToHex(
                        MessageDigest.getInstance("SHA-1").digest(
                        docSigningCertificate.encoded)).toUpperCase()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
                viewBinding.issuerDN.text = docSigningCertificate.issuerDN.name
                viewBinding.subjectDN.text = docSigningCertificate.subjectDN.name
                viewBinding.notBefore.text = simpleDateFormat.format(docSigningCertificate.notBefore)
                viewBinding.notAfter.text = simpleDateFormat.format(docSigningCertificate.notAfter)

            } else {
                viewBinding.cardViewDocumentSigningCertificate.visibility = View.GONE
            }

        } else {
            viewBinding.cardViewDocumentSigningCertificate.visibility = View.GONE
        }
    }

    private fun displayAuthenticationStatus(verificationStatus: VerificationStatus?, featureStatus: FeatureStatus) {
        displayVerificationStatusIcon(viewBinding.valueChipAuth, verificationStatus?.chipAuth)
        displayVerificationStatusIcon(viewBinding.valuePassive, verificationStatus?.passiveAuthentication)
        displayVerificationStatusIcon(viewBinding.valueActive, verificationStatus?.activeAuth)
        displayVerificationStatusIcon(viewBinding.valueCountrySigning, verificationStatus?.secureAccessControl)
        displayVerificationStatusIcon(viewBinding.valueDocumentSigning, verificationStatus?.documentSigning)
    }

    private fun displayVerificationStatusIcon(imageView: ImageView?, verdict: VerificationStatus.Verdict?) {
        var verdict = verdict
        if (verdict == null) {
            verdict = VerificationStatus.Verdict.UNKNOWN
        }
        val resourceIconId: Int
        val resourceColorId: Int
        when (verdict) {
            VerificationStatus.Verdict.SUCCEEDED -> {
                resourceIconId = R.drawable.ic_check_circle_outline
                resourceColorId = R.color.colorGreen
            }
            VerificationStatus.Verdict.FAILED -> {
                resourceIconId = R.drawable.ic_close_circle_outline
                resourceColorId = R.color.red
            }
            VerificationStatus.Verdict.NOT_PRESENT -> {
                resourceIconId = R.drawable.ic_close_circle_outline
                resourceColorId = android.R.color.darker_gray
            }
            VerificationStatus.Verdict.NOT_CHECKED -> {
                resourceIconId = R.drawable.ic_help_circle_outline
                resourceColorId = R.color.colorYellow
            }
            VerificationStatus.Verdict.UNKNOWN -> {
                resourceIconId = R.drawable.ic_close_circle_outline
                resourceColorId = android.R.color.darker_gray
            }
        }
        imageView!!.setImageResource(resourceIconId)
        imageView.setColorFilter(ContextCompat.getColor(requireActivity(), resourceColorId), android.graphics.PorterDuff.Mode.SRC_IN)
    }
}