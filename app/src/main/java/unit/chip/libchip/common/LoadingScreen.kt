package unit.chip.libchip.common

import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.Window
import android.widget.TextView
import unit.chip.libchip.R


/**
 * Created by DinhTC on 11/3/2023.
 * Skype: 0975469232
 */


object LoadingScreen {
    private val activeDialogs = mutableListOf<Dialog>()

    fun displayLoadingWithText(context: Context?, text: String? = null, cancelable: Boolean) {
        try {
            if (context == null) return

            val dialog = Dialog(context)
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
            dialog.setContentView(R.layout.loading_progress)
            dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
            dialog.setCancelable(cancelable)
            val textView = dialog.findViewById<TextView>(R.id.text)
            textView.text = text
            dialog.show()

            // Thêm dialog mới vào danh sách
            activeDialogs.add(dialog)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun hideLoading() {
        for (dialog in activeDialogs) {
            try {
                dialog.dismiss()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        // Xóa tất cả dialog khỏi danh sách
        activeDialogs.clear()
    }
}