package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentOrderBinding
import io.iostwin.iostdex.domain.LoginMessage
import io.iostwin.iostdex.domain.TokenSymbolResp
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.module.main.control.OrderControl
import io.iostwin.iostdex.module.main.ui.adapters.OrderPagerAdapter
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.NetConfig
import org.greenrobot.eventbus.EventBus

class OrderFragment : BaseFragment() {
    private lateinit var control: OrderControl
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<FragmentOrderBinding>(
            inflater,
            R.layout.fragment_order,
            container,
            false
        )
        binding.viewPager.adapter = OrderPagerAdapter(childFragmentManager)
        control = OrderControl(binding)
        binding.control = control
        NetConfig.getService(ApiService::class.java).chartAll().enqueue(HttpCallBack(this::success))
        EventBus.getDefault().register(control)
        return binding.root
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        control.addSymbol(response)
    }

    override fun initData() {
    }



    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(control)
    }
}