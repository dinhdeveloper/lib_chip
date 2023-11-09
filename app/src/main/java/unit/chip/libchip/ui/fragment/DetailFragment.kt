package unit.chip.libchip.ui.fragment

import android.view.View
import android.widget.ImageView
import androidx.core.content.ContextCompat
import unit.chip.lib_unit_chip.common.StringUtils
import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.FeatureStatus
import unit.chip.lib_unit_chip.model.VerificationStatus
import unit.chip.lib_unit_chip.public_release.ChipResult
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

    private var receivedPassport: ChipResult? = null
    private var simpleDateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

    override val layoutResourceId: Int
        get() = R.layout.fragment_detail

    override fun onViewCreated() {
        receivedPassport = arguments?.getParcelable("KEY_PASSPORT")
        if (receivedPassport != null) {
            renderUI(receivedPassport!!)
        }
    }

    private fun renderUI(cardEiD: ChipResult) {
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