package io.iostwin.iostdex.module.main.control

import android.net.Uri
import android.os.Handler
import android.widget.AdapterView
import androidx.databinding.library.baseAdapters.BR
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
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
    private var autoStatus = 0

    init {
        binding.refreshLayout.setOnRefreshListener { sendHttp() }
        binding.refreshLayout.autoRefresh()
        handler.postDelayed({ autoRefData() }, 5000)
    }

    private fun autoRefData() {
        sendHttp()
        if (autoStatus == 0)
            handler.postDelayed({ autoRefData() }, 5000)
        else if (autoStatus == 1) {
            autoStatus = 2
        }
    }

    private fun sendHttp() {
        NetConfig.getService(ApiService::class.java).chartAll()
            .enqueue(HttpCallBack(this::success, fail))
    }

    fun start() {
        if (autoStatus != 0) {
            if (autoStatus == 2) {
                autoStatus = 0
                autoRefData()
            }
        }
    }

    fun stop() {
        autoStatus = 1
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        data.clear()
        data.addAll(response)
        adapter.notifyDataSetChanged()
        binding.refreshLayout.finishRefresh()
    }

    fun itemClick(parent: AdapterView<*>, position: Int) {
        val tokenSymbol = parent.getItemAtPosition(position) as TokenSymbolResp
        Router.startUri(
            UriRequest(
                binding.root.context,
                Uri.Builder().path("/tokenInfo").appendQueryParameter(
                    "icon", tokenSymbol.icon
                ).appendQueryParameter("symbol", tokenSymbol.symbol).appendQueryParameter(
                    "name",
                    tokenSymbol.name
                ).build()
            )
        )
    }
}