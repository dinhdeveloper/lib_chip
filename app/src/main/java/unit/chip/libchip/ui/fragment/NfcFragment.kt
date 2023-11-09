package unit.chip.libchip.ui.fragment

import android.os.Bundle
import android.view.WindowManager
import androidx.navigation.fragment.findNavController
import net.sf.scuba.data.Gender
import org.jmrtd.lds.icao.MRZInfo
import unit.chip.lib_unit_chip.public_release.NfcOption
import unit.chip.lib_unit_chip.public_release.ChipResult
import unit.chip.libchip.R
import unit.chip.libchip.base.BaseFragment
import unit.chip.libchip.common.BottomSheetScanNFC
import unit.chip.libchip.databinding.FragmentNfcBinding
import unit.chip.libchip.ui.activity.MainActivity


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class NfcFragment : BaseFragment<FragmentNfcBinding>() {

    private var mrzInfo: MRZInfo? = null

    override val layoutResourceId: Int
        get() = R.layout.fragment_nfc

    override fun onViewCreated() {
//        val arguments = arguments
//        if (arguments!!.containsKey("KEY_MRZ_INFO")) {
//            mrzInfo = arguments.getSerializable("KEY_MRZ_INFO") as MRZInfo
//            if (mrzInfo != null) {
//                showBottomSheet(mrzInfo!!)
//            }
//        } else {
//            Toast.makeText(context, "MRZ không có dữ liệu", Toast.LENGTH_SHORT).show()
//        }

        val nfcOption = NfcOption(
            accessToken = "",
            tokenId = "",
            tokenKey = "",
            documentNumber = "045095008604",
            dateOfBirth = "950320",
            dateOfExpiry = "350320"
        )
        showBottomSheet(nfcOption)
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

    private fun showBottomSheet(nfcOption: NfcOption) {
        if (activity is MainActivity) {
            if ((activity as MainActivity).nfcTool != null) {
                val bottomSheetScanNfc = context?.let { context ->
                    BottomSheetScanNFC(nfcOption, (activity as MainActivity).nfcTool!!, {
                        goToDetail(it)
                    },{

                    }, {
                        //findNavController().popBackStack()
                    })
                }
                bottomSheetScanNfc?.isCancelable = false
                activity?.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
                activity?.supportFragmentManager?.let {
                    bottomSheetScanNfc?.show(
                        it,
                        bottomSheetScanNfc?.tag
                    )
                }
            }
        }
    }

    private fun goToDetail(cardEiD: ChipResult?) {

        //val dsCert = cardEiD?.sodFile?.docSigningCertificate?.let { docSigning -> StringUtils.encodeToBase64String(docSigning) }

        val arguments = Bundle()
        arguments.putParcelable("KEY_PASSPORT", cardEiD)
        findNavController().navigate(
            R.id.action_nfcFragment_to_detailFragment,
            arguments
        )
    }
}