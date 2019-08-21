package io.iostwin.iostdex.module.main.control

import android.os.Handler
import android.widget.AdapterView
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.FragmentHomeBinding
import io.iostwin.iostdex.common.ListAdapter
import io.iostwin.iostdex.domain.TokenSymbolResp
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.NetConfig

class HomeControl(private val binding: FragmentHomeBinding) {
    private val data = arrayListOf<TokenSymbolResp>()
    val adapter = ListAdapter(data, R.layout.item_home, BR.viewModel)
    private val fail = fun(_: Throwable) {
        binding.refreshLayout.finishRefresh(false)//传入false表示刷新失败
    }
    private val handler = Handler()

    init {
        binding.refreshLayout.setOnRefreshListener { sendHttp() }
        binding.refreshLayout.autoRefresh()
        handler.postDelayed({ autoRefData() }, 5000)
    }

    private fun autoRefData() {
        sendHttp()
        handler.postDelayed({ autoRefData() }, 5000)
    }

    private fun sendHttp() {
        NetConfig.getService(ApiService::class.java).chartAll()
            .enqueue(HttpCallBack(this::success, fail))
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        data.clear()
        data.addAll(response)
        adapter.notifyDataSetChanged()
        binding.refreshLayout.finishRefresh()
    }

    fun itemClick(parent: AdapterView<*>, position: Int) {
    }
}