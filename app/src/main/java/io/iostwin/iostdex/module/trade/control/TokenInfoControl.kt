package io.iostwin.iostdex.module.trade.control

import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.github.fujianlian.klinechart.DataHelper
import com.github.fujianlian.klinechart.KLineChartAdapter
import com.github.fujianlian.klinechart.formatter.DateFormatter
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.ActivityTokenInfoBinding
import io.iostwin.iostdex.domain.ChartHistoryResp
import io.iostwin.iostdex.domain.OnPopWindowMessage
import io.iostwin.iostdex.domain.PriceMessage
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.NetConfig
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class TokenInfoControl(
    private val binding: ActivityTokenInfoBinding,
    private val symbol: String,
    private val icon: String,
    private val name: String,
    private val decimal: String
) :
    OnTradeSelectMoreListener {
    val price = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val percent = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val max = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val min = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val volume = ObservableField<String>("0")
    private val moreStr = binding.root.context.getString(R.string.trade_more)
    val more = ObservableField<String>(moreStr)
    private var isToken = true
    private var volumeToken = BigDecimal.ZERO
    private var volumeMainToken = BigDecimal.ZERO
    private val adapter = KLineChartAdapter()
    private var currentResolution = "D"
    private val resolutions = arrayOf("1", "5", "15", "30", "60", "240", "D", "W", "M")
    private var fromTime = 1556640000
    private var call = NetConfig.getService(
        ApiService::class.java
    )
        .cartHistory(
            symbol,
            currentResolution,
            fromTime,
            (System.currentTimeMillis() / 1000).toInt()
        )

    init {
        binding.kLineChartView.adapter = adapter
        binding.kLineChartView.dateTimeFormatter = DateFormatter()
        binding.kLineChartView.setGridRows(4)
        binding.kLineChartView.setGridColumns(4)
        binding.chartKd.setOnCheckedChangeListener { _, i ->
            if (i != View.NO_ID) {
                binding.tradeMore.isSelected = false
                more.set(moreStr)
                val index = when (i) {
                    R.id.chart_1m -> 0
                    R.id.chart_15m -> 2
                    R.id.chart_4h -> 5
                    R.id.chart_1d -> 6
                    else -> 0
                }
                changeResolution(index)
            }
        }
        initData()
    }

    override fun onTradeSelectMore(text: String) {
        binding.chartKd.clearCheck()
        more.set(text)
        binding.tradeMore.isSelected = true
        val index = when (text) {
            "5m" -> 1
            "30m" -> 3
            "1h" -> 4
            "1W" -> 7
            "1M" -> 8
            else -> 0
        }
        changeResolution(index)
    }

    private fun changeResolution(index: Int) {
        if (currentResolution == resolutions[index])
            return
        adapter.clearData()
        adapter.notifyDataSetChanged()
        if (index < 2) {
            binding.kLineChartView.setMainDrawLine(true)
        } else {
            binding.kLineChartView.setMainDrawLine(false)
        }
        currentResolution = resolutions[index]
        initData()
    }

    private fun initData() {
        binding.kLineChartView.justShowLoading()
        if (call.isExecuted)
            call.cancel()
        call = NetConfig.getService(ApiService::class.java)
            .cartHistory(
                symbol,
                currentResolution,
                fromTime,
                (System.currentTimeMillis() / 1000).toInt()
            )
        call.enqueue(HttpCallBack(this::onSuccess))
    }

    private fun onSuccess(resp: ChartHistoryResp) {
        if (resp.s == "ok") {
            val data = resp.toKLineEntity()
            DataHelper.calculate(data)
            adapter.addFooterData(data)
            adapter.notifyDataSetChanged()
            binding.kLineChartView.startAnimation()
            binding.kLineChartView.refreshComplete()
        }
    }

    @Subscribe
    fun onPriceMessage(message: PriceMessage) {
        price.set(message.price)
        percent.set(message.percent24H)
        max.set(message.maxPrice24H)
        min.set(message.minPrice24H)
        volumeToken = message.volume24H
        volumeMainToken = message.amount24H
        if (isToken) {
            volume.set(volumeToken.toPlainString())
        } else {
            volume.set(volumeMainToken.toPlainString() + "IOST")
        }
    }

    fun swapTotal() {
        isToken = !isToken
        if (isToken) {
            volume.set(volumeToken.toPlainString())
        } else {
            volume.set(volumeMainToken.toPlainString() + "IOST")
        }
    }

    fun menu(type: Int) {
        EventBus.getDefault().post(OnPopWindowMessage(type))
    }

    fun onClick(view: View, isBuy: Int) {
        Router.startUri(
            UriRequest(
                view.context,
                Uri.Builder().path("/tradeToken").appendQueryParameter(
                    "icon",
                    icon
                ).appendQueryParameter("symbol", symbol).appendQueryParameter(
                    "name",
                    name
                ).appendQueryParameter("decimal", decimal).appendQueryParameter(
                    "isBuy",
                    isBuy.toString()
                ).build()
            )
        )
    }
}