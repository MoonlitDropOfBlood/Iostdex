package io.iostwin.iostdex.module.main.control

import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.ListAdapter
import io.iostwin.iostdex.databinding.FragmentAssetsBinding
import io.iostwin.iostdex.domain.TokenSymbolResp
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.IOSTService
import io.iostwin.iostdex.netwrok.NetConfig
import java.math.BigDecimal

class AssetsControl(private val binding: FragmentAssetsBinding) {
    private val data = arrayListOf<TokenSymbolResp>()
    val adapter = ListAdapter(data, R.layout.item_home, BR.viewModel)
    private val fail = fun(_: Throwable) {
        binding.refreshLayout.finishRefresh(false)//传入false表示刷新失败
    }

    val totalAmount = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val gas = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val ram = ObservableField<BigDecimal>(BigDecimal.ZERO)

    init {
        binding.refreshLayout.setOnRefreshListener { sendHttp() }
    }

    fun autoRefresh() {
        binding.refreshLayout.autoRefresh()
    }

    private fun sendHttp() {
        NetConfig.getService(ApiService::class.java).chartAll()
            .enqueue(HttpCallBack(this::success, fail))
        NetConfig.getService(IOSTService::class.java).getAccount(User.account!!)
            .enqueue(HttpCallBack({
                var total = it.balance
                for (frozen in it.frozen_balances) {
                    total = total.add(frozen.amount)
                }
                totalAmount.set(total)
                gas.set(it.gas_info.current_total.div(it.gas_info.limit))
            }))
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        data.clear()
        data.addAll(response)
        adapter.notifyDataSetChanged()
        binding.refreshLayout.finishRefresh()
    }
}