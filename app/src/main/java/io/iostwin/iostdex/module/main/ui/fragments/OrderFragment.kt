package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentOrderBinding
import io.iostwin.iostdex.domain.TokenSymbolResp
import io.iostwin.iostdex.module.main.control.OrderControl
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.NetConfig

class OrderFragment : BaseFragment() {
    private val tokenSymbols = arrayListOf<TokenSymbolResp>()
    private lateinit var binding: FragmentOrderBinding
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_order,
            container,
            false
        )
        binding.control = OrderControl()
        NetConfig.getService(ApiService::class.java).chartAll().enqueue(HttpCallBack(this::success))
        return binding.root
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        tokenSymbols.addAll(response)
        for (item in tokenSymbols) {
//            binding.tokenSymbol.addTab(binding.tokenSymbol.newTab().setText(item.name))
        }
        initData()
    }

    override fun initData() {
//        tokenSymbols[binding.tokenSymbol.selectedTabPosition]
    }
}