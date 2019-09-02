package io.iostwin.iostdex.module.trade.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentDepthBinding
import io.iostwin.iostdex.module.trade.control.DepthControl
import org.greenrobot.eventbus.EventBus

class DepthFragment : BaseFragment() {
    private var control: DepthControl? = null
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<FragmentDepthBinding>(
            inflater,
            R.layout.fragment_depth,
            container,
            false
        )
        val name = arguments!!.getString("name")
        val decimal = arguments!!.getInt("decimal")
        control = DepthControl(decimal,name!!,binding)
        binding.control = control
        return binding.root
    }

    override fun initData() {
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        if (isVisibleToUser) {
            if (control != null)
                EventBus.getDefault().register(control)
        } else {
            if (control != null)
                EventBus.getDefault().unregister(control)
        }
    }

    override fun onResume() {
        super.onResume()
        if (control != null)
            EventBus.getDefault().register(control)
    }

    override fun onPause() {
        super.onPause()
        if (control != null)
            EventBus.getDefault().unregister(control)
    }
}