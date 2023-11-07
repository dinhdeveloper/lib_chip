package unit.chip.libchip.base

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.widget.FrameLayout
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import unit.chip.libchip.R


/**
 * Created by DinhTC on 10/31/2023.
 * Skype: 0975469232
 */


abstract class BaseActivity<_viewDataBinding : ViewDataBinding>() : AppCompatActivity() {

    abstract val layoutResourceId: Int
    protected var viewDataBinding: ViewDataBinding? = null
    abstract fun onCreateActivity()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(layoutResourceId)
        onCreateActivity()
    }

    override fun setContentView(@LayoutRes layoutResID: Int) {
        val baseLayout = layoutInflater.inflate(R.layout.activity_base, null) as FrameLayout
        val layoutMain: FrameLayout = baseLayout.findViewById(R.id.layout_main)
        viewDataBinding = DataBindingUtil.inflate(layoutInflater, layoutResID, layoutMain, true)
        super.setContentView(baseLayout)
    }
}