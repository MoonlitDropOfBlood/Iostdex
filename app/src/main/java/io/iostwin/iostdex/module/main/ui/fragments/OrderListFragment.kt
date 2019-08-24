package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentOrderListBinding
import io.iostwin.iostdex.module.main.control.CommissionControl
import io.iostwin.iostdex.module.main.control.HistoryControl
import io.iostwin.iostdex.module.main.control.OrderListControl
import org.greenrobot.eventbus.EventBus

class OrderListFragment : BaseFragment() {
    private var control: OrderListControl<*>? = null
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<FragmentOrderListBinding>(
            inflater,
            R.layout.fragment_order_list,
            container,
            false
        )
        control = if (arguments!!.getBoolean("fist")) {
            CommissionControl()
        } else {
            HistoryControl()
        }
        if (control is CommissionControl) {
            EventBus.getDefault().register(control)
        }
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
}