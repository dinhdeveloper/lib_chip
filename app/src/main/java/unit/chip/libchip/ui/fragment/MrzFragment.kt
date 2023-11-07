package unit.chip.libchip.ui.fragment

import android.Manifest
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.google.gson.Gson
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import io.fotoapparat.Fotoapparat
import io.fotoapparat.parameter.ScaleType
import io.fotoapparat.result.BitmapPhoto
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import net.sf.scuba.data.Gender
import org.jmrtd.lds.icao.MRZInfo
import unit.chip.libchip.R
import unit.chip.libchip.base.BaseFragment
import unit.chip.libchip.common.BottomSheetScanNFC
import unit.chip.libchip.common.LoadingScreen
import unit.chip.libchip.common.Utils
import unit.chip.libchip.databinding.FragmentMrzBinding
import unit.chip.libchip.ui.activity.MainActivity


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


class MrzFragment : BaseFragment<FragmentMrzBinding>() {

    private var fotoapparat: Fotoapparat? = null
    private val PERMISSION_CAMERA = 2002
    override val layoutResourceId: Int
        get() = R.layout.fragment_mrz

    override fun onViewCreated() {
        openCamera()
    }

    private fun openCamera() {
        if (Utils.isCameraGranted(context)) {
            startCamera()
        } else {
            Utils.checkPermission(this@MrzFragment, Manifest.permission.CAMERA, PERMISSION_CAMERA)
        }
    }

    private fun startCamera() {
        viewBinding.tvErr.visibility = View.GONE
        fotoapparat = Fotoapparat(
            context = requireContext(),
            view = viewBinding.cameraPreview,
            scaleType = ScaleType.CenterCrop
        )
        viewBinding.tvErr.visibility = View.GONE
        fotoapparat?.start()

        viewBinding.btnClick.setOnClickListener {
            val photoResult = fotoapparat?.takePicture()
            photoResult
                ?.toBitmap()
                ?.whenAvailable { bitmapPhoto ->
                    fotoapparat?.stop()
                    showProgressBar()
                    readMrz(bitmapPhoto)
                }
        }
    }

    private fun readMrz(bitmapPhoto: BitmapPhoto?) {
        bitmapPhoto?.let { photo ->
            val bitmap = photo.bitmap
            val options =
                TextRecognizerOptions.DEFAULT_OPTIONS // Hoặc tùy chỉnh các tùy chọn theo nhu cầu của bạn
            val recognizer = TextRecognition.getClient(options)

            val image = InputImage.fromBitmap(bitmap, 0)
            recognizer.process(image)
                .addOnSuccessListener { text ->

                    val mrzString = extractMRZData(text.text)
                    val info = extractMRZInfo(mrzString)
                    if (info != null) {
                        val cccdID = info.first
                        val dateOfBirth = info.second
                        val dateOfExpiry = info.third
                        var mrzInfo = createDummyMrz(cccdID, dateOfBirth, dateOfExpiry)
                        Log.e("AAAAAAAAAAAAA", Gson().toJson(mrzInfo))
                        findNavController().navigate(
                            R.id.action_mrzFragment_to_nfcFragment,
                            bundleOf("KEY_MRZ_INFO" to mrzInfo)
                        )
                        hideProgressBar()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("AAAAAAAAAAAAAA", "${e.printStackTrace()}")
                    e.printStackTrace()
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

    private fun extractMRZData(ocrResult: String): String = runBlocking {
        val result = async {
            val cleanedOcrResult = ocrResult.replace(Regex("\\s+"), "")
            val startIndex = cleanedOcrResult.indexOf("IDVNM")
            if (startIndex != -1) {
                val mrzData = cleanedOcrResult.substring(startIndex)
                mrzData
            } else {
                hideProgressBar()
                startCamera()
                viewBinding.tvErr.visibility = View.VISIBLE
                viewBinding.tvErr.text = "Không tìm thấy MRZ, vui lòng chụp lại"
                "Không tìm thấy MRZ"
            }
        }
        result.await()
    }

    private fun extractMRZInfo(mrzData: String): Triple<String, String, String>? = runBlocking {
        if (mrzData.length < 46) {
            return@runBlocking null
        }

        val result = async {
            // Lấy thông tin từ chuỗi MRZ
            val cccdID = mrzData.substring(18, 27)
            val dateOfBirth = mrzData.substring(30, 36)
            val dateOfExpiry = mrzData.substring(38, 44)

            Triple(cccdID, dateOfBirth, dateOfExpiry)
        }

        result.await()
    }


    override fun onResume() {
        super.onResume()
        startCamera()
    }

    override fun onPause() {
        super.onPause()
        fotoapparat?.stop()
    }

    private fun showProgressBar() {
        LoadingScreen.displayLoadingWithText(
            requireContext(),
            "Vui lòng chờ...",
            false
        )
    }

    private fun hideProgressBar() {
        LoadingScreen.hideLoading()
    }
}