package io.iostwin.iostdex.module.trade.control

import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import androidx.databinding.Observable
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import androidx.databinding.ObservableInt
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.R
import io.iostwin.iostdex.domain.*
import io.iostwin.iostdex.module.trade.ui.adapters.OrderAdapter
import io.iostwin.iostdex.utils.Utils
import io.iostwin.iostdex.utils.oneHundred
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.math.BigDecimal

class TradeControl(
    buy: Int,
    val tradeSymbolName: String,
    private val tradeSymbol: String,
    val decimal: Int
) {
    val mainSymbol = "IOST"
    val isBuy = ObservableBoolean(buy == 1)
    val price = ObservableField<String>()
    val num = ObservableField<String>()
    val isLimit = ObservableBoolean(true)
    val tradePrice = ObservableField<BigDecimal>()
    val percent = ObservableField<BigDecimal>()
    val seekBar = ObservableInt()
    val isLogin = ObservableBoolean(User.isLogin())

    private val buyOrder = arrayListOf<Order>()
    private val sellOrder = arrayListOf<Order>()
    val buyAdapter = OrderAdapter(buyOrder, R.layout.item_order_buy, BR.viewModel)
    val sellAdapter = OrderAdapter(sellOrder, R.layout.item_order_sell, BR.viewModel)

    val mainBalance = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val tradeBalance = ObservableField<BigDecimal>(BigDecimal.ZERO)

    init {
        seekBar.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (isBuy.get()) {
                    val temp = price.get()
                    val price = if (TextUtils.isEmpty(temp)) BigDecimal.ZERO else BigDecimal(temp)
                    if (price == BigDecimal.ZERO) {
                        return
                    }
                    val balance = mainBalance.get()!!
                    num.set(
                        balance.multiply(BigDecimal(seekBar.get())).divide(
                            oneHundred,
                            8,
                            BigDecimal.ROUND_HALF_DOWN
                        ).divide(price, decimal, BigDecimal.ROUND_HALF_DOWN).toPlainString()
                    )
                } else {
                    val temp = tradeBalance.get()!!
                    num.set(
                        temp.multiply(BigDecimal(seekBar.get())).divide(
                            oneHundred,
                            decimal,
                            BigDecimal.ROUND_HALF_DOWN
                        ).toPlainString()
                    )
                }
            }
        })
        val cal = object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                val num = num.get()
                if (isBuy.get()) {
                    val price = price.get()
                    if (TextUtils.isEmpty(price) || TextUtils.isEmpty(num))
                        return
                    val total = BigDecimal(price).multiply(BigDecimal(num))
                        .setScale(8, BigDecimal.ROUND_HALF_DOWN)
                    if (total > mainBalance.get()) {
                        Utils.toast(R.string.trade_text2)
                    }
                } else if (!TextUtils.isEmpty(num)) {
                    if (BigDecimal(num) > tradeBalance.get()) {
                        Utils.toast(R.string.trade_text2)
                    }
                }
            }

        }
    }

    @Subscribe(sticky = true)
    fun onOrderInitMessage(message: TradeMessage) {
        EventBus.getDefault().removeStickyEvent(message)
        tradePrice.set(message.price)
        percent.set(message.percent)
        onOrderMessage(message.buy)
        onOrderMessage(message.sell)
        if (isBuy.get()) {
            if (buyOrder.isNotEmpty()) {
                price.set(buyOrder[0].price.toPlainString())
            }
        } else {
            if (sellOrder.isNotEmpty()) {
                price.set(sellOrder[0].price.toPlainString())
            }
        }
    }

    @Subscribe
    fun onPriceMessage(message: PriceMessage) {
        tradePrice.set(message.price)
        percent.set(message.percent24H)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onOrderMessage(message: OrderMessage) {
        if (message.buy) {
            buyOrder.clear()
            buyOrder.addAll(message.data)
        } else {
            sellOrder.clear()
            sellOrder.addAll(message.data)
            sellOrder.reverse()
        }
        for (item in buyOrder) {
            buyAdapter.max = maxOf(item.balance, buyAdapter.max)
            sellAdapter.max = maxOf(item.balance, sellAdapter.max)
        }
        for (item in sellOrder) {
            buyAdapter.max = maxOf(item.balance, buyAdapter.max)
            sellAdapter.max = maxOf(item.balance, sellAdapter.max)
        }
        buyAdapter.notifyDataSetChanged()
        sellAdapter.notifyDataSetChanged()
    }

    fun itemSelect(index: Int) {
        isLimit.set(index == 0)
    }

    fun itemClick(parent: AdapterView<*>, position: Int) {
        val order = parent.getItemAtPosition(position) as Order
        price.set(order.price.toPlainString())
    }

    fun trade(view: View) {

    }

}