package io.iostwin.iostdex.module.trade.control

import androidx.databinding.library.baseAdapters.BR
import com.github.lilei.depthmapview.DepthBuySellData
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.FragmentDepthBinding
import io.iostwin.iostdex.domain.Order
import io.iostwin.iostdex.domain.OrderMessage
import io.iostwin.iostdex.module.trade.ui.adapters.OrderAdapter
import org.greenrobot.eventbus.Subscribe

class DepthControl(
    private val decimal: Int,
    val tradeSymbol: String,
    val binding: FragmentDepthBinding
) {
    val mainSymbol = "IOST"
    private val buyOrder = arrayListOf<Order>()
    private val sellOrder = arrayListOf<Order>()
    val buyAdapter = OrderAdapter(buyOrder, R.layout.item_depth_buy, BR.viewModel)
    val sellAdapter = OrderAdapter(sellOrder, R.layout.item_depth_sell, BR.viewModel)

    @Subscribe
    fun onOrderMessage(message: OrderMessage) {
        if (message.buy) {
            buyOrder.clear()
            buyOrder.addAll(message.data)
        } else {
            sellOrder.clear()
            sellOrder.addAll(message.data)
        }
        val buy = mutableListOf<DepthBuySellData>()
        val sell = mutableListOf<DepthBuySellData>()
        for (item in buyOrder) {
            buy.add(DepthBuySellData(item.price.toPlainString(), item.balance.toPlainString()))
            buyAdapter.max = maxOf(item.balance, buyAdapter.max)
            sellAdapter.max = maxOf(item.balance, sellAdapter.max)
        }
        for (item in sellOrder) {
            sell.add(DepthBuySellData(item.price.toPlainString(), item.balance.toPlainString()))
            buyAdapter.max = maxOf(item.balance, buyAdapter.max)
            sellAdapter.max = maxOf(item.balance, sellAdapter.max)
        }
        binding.depthMapView.setData(buy, sell, mainSymbol, tradeSymbol, 8, decimal)
        buyAdapter.notifyDataSetChanged()
        sellAdapter.notifyDataSetChanged()
    }
}