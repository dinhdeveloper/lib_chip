package unit.chip.libchip.common

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.gson.Gson
import org.jmrtd.lds.icao.MRZInfo
import unit.chip.lib_unit_chip.common.NfcTagTool
import unit.chip.lib_unit_chip.model.CardEiD
import unit.chip.lib_unit_chip.model.NfcError
import unit.chip.lib_unit_chip.model.NfcOption
import unit.chip.lib_unit_chip.nfc.NfcCallback
import unit.chip.libchip.R


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class BottomSheetScanNFC(
    private var nfcOption: NfcOption,
    private var nfcTool: NfcTagTool,
    private val listenerSuccessOutput: (CardEiD?) -> Unit,
    private val listenerCloseOutput: (() -> Unit)
) : BottomSheetDialogFragment() {

    private lateinit var txtLabel: TextView
    private lateinit var txtNote: TextView
    private lateinit var btnCancel: TextView
    private lateinit var imgScan: ImageView
    private lateinit var imgCheck: ImageView
    private lateinit var imgCheck1: ImageView
    private lateinit var progress: DotProgressBar

    var bottomSheetBehavior: BottomSheetBehavior<*>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.bottom_sheet_scan_nfc, container, false)
    }

    override fun getTheme(): Int {
        return R.style.AppBottomSheetDialogTheme
    }

    override fun onViewCreated(
        modalSheetView: View,
        savedInstanceState: Bundle?
    ) {
        super.onViewCreated(modalSheetView, savedInstanceState)
        /*Show full dialog*/
        bottomSheetBehavior = BottomSheetBehavior.from(view?.parent as View)
        bottomSheetBehavior?.state = BottomSheetBehavior.STATE_EXPANDED
        bottomSheetBehavior?.isDraggable = false

        txtLabel = modalSheetView.findViewById(R.id.txtLabel)
        txtNote = modalSheetView.findViewById(R.id.txtNote)
        btnCancel = modalSheetView.findViewById(R.id.btnCancel)
        imgScan = modalSheetView.findViewById(R.id.imgScan)
        imgCheck = modalSheetView.findViewById(R.id.imgCheck)
        imgCheck1 = modalSheetView.findViewById(R.id.imgCheck1)
        progress = modalSheetView.findViewById(R.id.progress)

        btnCancel.setOnClickListener {
            onDestroy()
            listenerCloseOutput.invoke()
            dismiss()
        }
        startReadChip()
    }

    private fun startReadChip() {
        progress.visibility = View.VISIBLE
        progress.startProgress()

        nfcTool.handleNFC(nfcOption, object : NfcCallback() {
            override fun onSuccess(nfcResult: CardEiD?) {
                progress.stopProgress()
                progress.visibility = View.GONE
                listenerSuccessOutput(nfcResult)
                Log.d("AAAAAAAAAAAA", Gson().toJson(nfcResult))
                dismiss()
            }

            override fun onError(nfcError: NfcError?) {
                when (nfcError) {
                    NfcError.OPEN_FAILURE -> {}
                    NfcError.AUTHENTICATE_FAILURE -> {}
                    else -> {}
                }
            }
        })
    }
}