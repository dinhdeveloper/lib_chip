package unit.chip.libchip.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment


/**
 * Created by DinhTC on 10/31/2023.
 * Skype: 0975469232
 */


abstract class BaseFragment<_viewDataBinding : ViewDataBinding> : Fragment() {

    abstract val layoutResourceId: Int
    lateinit var viewBinding: _viewDataBinding
    private val binding get() = viewBinding
    abstract fun onViewCreated()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        viewBinding = DataBindingUtil.inflate(inflater, layoutResourceId, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onViewCreated()
    }
}