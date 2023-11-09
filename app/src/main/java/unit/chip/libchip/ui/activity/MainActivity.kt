package unit.chip.libchip.ui.activity

import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.nfc.NfcAdapter
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import unit.chip.lib_unit_chip.public_release.NfcTagTool
import unit.chip.libchip.R
import unit.chip.libchip.base.BaseActivity
import unit.chip.libchip.databinding.ActivityMainBinding

class MainActivity : BaseActivity<ActivityMainBinding>() {

    override val layoutResourceId: Int
        get() = R.layout.activity_main

    private var nfcAdapter: NfcAdapter? = null
    private var pendingIntent: PendingIntent? = null
    var nfcTool: NfcTagTool? = null

    override fun onCreateActivity() {
        nfcTool = NfcTagTool(this)
        startNFC()
        findNavController(R.id.navhost)
    }

    private fun startNFC() {
        nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        pendingIntent = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            PendingIntent.getActivity(
                this, 0, Intent(this, javaClass).addFlags(
                    Intent.FLAG_ACTIVITY_SINGLE_TOP
                ), PendingIntent.FLAG_MUTABLE
            )
        } else {
            PendingIntent.getActivity(
                this,
                0,
                Intent(this, javaClass).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP),
                PendingIntent.FLAG_IMMUTABLE
            )
        }

        if (isNfcSupported() == true) {
            if (nfcAdapter?.isEnabled == false) {
                showWirelessSettings()
            }
        } else {
            Toast.makeText(this, getString(R.string.warning_no_nfc), Toast.LENGTH_SHORT).show()
        }
    }

    private fun isNfcSupported(): Boolean? {
        val packageManager = packageManager
        return packageManager?.hasSystemFeature(PackageManager.FEATURE_NFC)
    }


    override fun onBackPressed() {
        onSupportNavigateUp()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.navhost)
        return navController.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onResume() {
        super.onResume()
        if (nfcAdapter != null && nfcAdapter?.isEnabled == true) {
            nfcAdapter!!.enableForegroundDispatch(this, pendingIntent, null, null)
        } else {
            nfcAdapter = NfcAdapter.getDefaultAdapter(this)
        }
    }

    override fun onPause() {
        super.onPause()
        if (nfcAdapter != null) nfcAdapter?.disableForegroundDispatch(this)
    }

    public override fun onNewIntent(intent: Intent) {
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.navhost) as NavHostFragment
        val currentFragment = navHostFragment.childFragmentManager.fragments[0]
        if (NfcAdapter.ACTION_TAG_DISCOVERED == intent.action || NfcAdapter.ACTION_TECH_DISCOVERED == intent.action) {
            nfcTool?.handleNfcEvent(intent)
        } else {
            super.onNewIntent(intent)
        }
    }

    private fun showWirelessSettings() {
        val mDialog = MaterialAlertDialogBuilder(this)
        mDialog.setPositiveButton("Đồng ý") { dialogInterface, _ ->
            val intent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
            startActivity(intent)
            dialogInterface.dismiss()
        }.setNegativeButton("Hủy") { dialogInterface, _ ->
            dialogInterface.dismiss()
            finishAffinity()
        }.setMessage(getString(R.string.enable_nfc))
            .setTitle("Thông báo").create()
        mDialog.show()
    }
}