package io.iostwin.iostdex.module.trade.control

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Toast
import androidx.databinding.*
import androidx.databinding.library.baseAdapters.BR
import com.google.gson.GsonBuilder
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import com.tokenpocket.opensdk.base.TPListener
import com.tokenpocket.opensdk.base.TPManager
import com.tokenpocket.opensdk.simple.model.Transaction
import es.dmoral.toasty.Toasty
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseViewAdapter
import io.iostwin.iostdex.domain.*
import io.iostwin.iostdex.module.trade.ui.adapters.OrderAdapter
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.IOSTService
import io.iostwin.iostdex.netwrok.NetConfig
import io.iostwin.iostdex.utils.md5
import io.iostwin.iostdex.utils.oneHundred
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import org.json.JSONObject
import java.math.BigDecimal

class TradeControl(
    context: Context,
    buy: Int,
    val tradeSymbolName: String,
    private val tradeSymbol: String,
    val decimal: Int
) {
    val mainSymbol = "IOST"
    val isBuy = ObservableBoolean(buy == 1)
    val price = ObservableField<String>()
    private var priceStr = ""
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

    private val data = arrayListOf<RecordOrderResp>()
    val adapter = BaseViewAdapter(
        data,
        R.layout.item_commission,
        BR.viewModel,
        0,
        this::onItemClick,
        this::onLongItemClick,
        this::onViewHolder
    )

    private var call = NetConfig.getService(
        ApiService::class.java
    )
        .orders(
            1,
            tradeSymbol,
            User.account ?: ""
        )

    private var actionId = ""
    private val contract = "ContractBqYBBN1JuvvcmbaWkbSv6Pa334UJinM9vTPWPC2hvUDL"
    private val gson = GsonBuilder().serializeNulls().create()

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
                val price = price.get()
                if (!TextUtils.isEmpty(price)) {
                    if (sellOrder.isNotEmpty() && buyOrder.isNotEmpty()) {
                        if (BigDecimal(price) != sellOrder[sellOrder.size - 1].price && BigDecimal(
                                price
                            ) != buyOrder[0].price
                        )
                            priceStr = price!!
                    }
                }
                if (isBuy.get()) {
                    if (TextUtils.isEmpty(price) || TextUtils.isEmpty(num))
                        return
                    val total = BigDecimal(price).multiply(BigDecimal(num))
                        .setScale(8, BigDecimal.ROUND_HALF_DOWN)
                    if (total > mainBalance.get()) {
                        Toasty.warning(context, R.string.trade_text2, Toast.LENGTH_SHORT, true)
                            .show()
                    }
                } else if (!TextUtils.isEmpty(num)) {
                    if (BigDecimal(num) > tradeBalance.get()) {
                        Toasty.warning(context, R.string.trade_text2, Toast.LENGTH_SHORT, true)
                            .show()
                    }
                }
            }

        }
        price.addOnPropertyChangedCallback(cal)
        num.addOnPropertyChangedCallback(cal)
        isBuy.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (!isLimit.get())
                    changePrice()
            }
        })
        isLimit.addOnPropertyChangedCallback(object : Observable.OnPropertyChangedCallback() {
            override fun onPropertyChanged(sender: Observable?, propertyId: Int) {
                if (!isLimit.get())
                    changePrice()
                else
                    price.set(priceStr)
            }

        })
    }

    fun changePrice() {
        if (isBuy.get()) {
            if (sellOrder.isNotEmpty()) {
                price.set(sellOrder[sellOrder.size - 1].price.toPlainString())
            }
        } else {
            if (buyOrder.isNotEmpty()) {
                price.set(buyOrder[0].price.toPlainString())
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
        changePrice()
        priceStr = price.get() ?: ""
    }

    @Subscribe
    fun onLogint(@Suppress("UNUSED_PARAMETER") message: LoginMessage) {
        isLogin.set(User.isLogin())
        sendHttp()
        updateBalance()
    }

    @Subscribe
    fun onPriceMessage(message: PriceMessage) {
        tradePrice.set(message.price)
        percent.set(message.percent24H)
        sendHttp()
        updateBalance()
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
        sendHttp()
        updateBalance()
    }

    fun itemSelect(index: Int) {
        isLimit.set(index == 0)
    }

    fun itemClick(parent: AdapterView<*>, position: Int) {
        val order = parent.getItemAtPosition(position) as Order
        price.set(order.price.toPlainString())
    }

    fun trade(view: View) {
        if (!isLogin.get()) {
            Router.startUri(
                UriRequest(
                    view.context,
                    Uri.Builder().path("/login").build()
                )
            )
            return
        }
        if (TextUtils.isEmpty(price.get()) || TextUtils.isEmpty(num.get())) {
            Toasty.warning(view.context, R.string.trade_error_1, Toast.LENGTH_SHORT, true).show()
            return
        }
        val priceStr = price.get()!!
        val numStr = num.get()!!
        val total = BigDecimal(priceStr).multiply(BigDecimal(numStr))
            .setScale(8, BigDecimal.ROUND_HALF_UP)
        if (total < BigDecimal.ONE) {
            Toasty.warning(view.context, R.string.trade_error_2, Toast.LENGTH_SHORT, true).show()
            return
        }
        NetConfig.getService(IOSTService::class.java).getChainInfo().enqueue(HttpCallBack({
            val `data` =
                arrayListOf(tradeSymbol, priceStr, numStr, if (isBuy.get()) "1" else "0")
            val amountLimit = AmountLimit(
                if (isBuy.get()) "iost" else tradeSymbol,
                if (isBuy.get()) total.toPlainString() else num.get()!!
            )
            val payload =
                createPayLoad("entrust", data, it.head_block_time, amountLimit)
            tp(view.context, payload)
        }))
    }

    private fun updateBalance() {
        if (!User.isLogin()) {
            return
        }
        val key = "TB${User.account!!}"
        val body = BatchContractStorageReq(
            true,
            "token.iost",
            mutableListOf(KeyField("iost", key), KeyField(tradeSymbol, key))
        )
        NetConfig.getService(IOSTService::class.java).getBatchContractStorage(body)
            .enqueue(HttpCallBack({
                val main = it.datas[0]
                val trade = it.datas[1]
                if (main != "null") {
                    mainBalance.set(
                        BigDecimal(main).divide(
                            BigDecimal(10).pow(8),
                            8,
                            BigDecimal.ROUND_HALF_DOWN
                        )
                    )
                }
                if (trade != "null") {
                    tradeBalance.set(
                        BigDecimal(trade).divide(
                            BigDecimal(10).pow(decimal),
                            decimal,
                            BigDecimal.ROUND_HALF_DOWN
                        )
                    )
                }
            }))
    }

    private fun createPayLoad(
        action: String,
        data: ArrayList<String>,
        time: Long,
        amountLimit: AmountLimit? = null
    ): String {
        val txABI = arrayListOf<Any>(contract, action, data)
        val tx = Tx(
            listOf(Action(action, contract, gson.toJson(data))),
            if (amountLimit == null) listOf() else listOf(amountLimit),
            time,
            time + (300L * 1000000000L)
        )
        val payload = TPPaylod(User.account!!, tx, txABI)
        return gson.toJson(payload)
    }

    private fun tp(context: Context, payload: String) {
        actionId = md5(System.nanoTime().toString())
        val transaction = Transaction()
        transaction.blockchain = "IOST"
        transaction.dappName = "IOSTDEX"
        transaction.dappIcon =
            "https://dapp-assets.dappradar.com/32bae78a7f9d20914c93fa6385a2bd8d.png"
        transaction.actionId = actionId
        transaction.action = "pushTransaction"
        transaction.payload = payload
        TPManager.getInstance().pushTransaction(context, transaction, object : TPListener {
            override fun onSuccess(data: String) {
                val jsonObject = JSONObject(data)
                if (jsonObject.optString("actionId") == actionId) {
                    Toasty.success(context, R.string.trade_success, Toast.LENGTH_SHORT, true).show()
                }
            }

            override fun onError(data: String) {
                Toasty.error(context, R.string.trade_error_3, Toast.LENGTH_SHORT, true).show()
            }

            override fun onCancel(data: String) {

            }
        })
    }

    fun operation(isAdd: Boolean) {
        val temp = price.get()
        val price = if (TextUtils.isEmpty(temp)) BigDecimal.ZERO.setScale(
            8,
            BigDecimal.ROUND_HALF_DOWN
        ) else BigDecimal(temp).setScale(8, BigDecimal.ROUND_HALF_DOWN)
        val min = BigDecimal("0.00000001")
        if (isAdd)
            this.price.set(min.add(price).toPlainString())
        else
            this.price.set(price.minus(min).toPlainString())
    }

    private fun onItemClick(context: Context, item: RecordOrderResp) {
        Router.startUri(
            UriRequest(
                context,
                Uri.Builder().path("/web").appendQueryParameter(
                    "url",
                    "https://www.iostabc.com/tx/${item.tradeHx}"
                ).build()
            )
        )
    }

    private fun onLongItemClick(context: Context, item: RecordOrderResp) {
        if (item.status >= 2) {
            Toasty.info(context, R.string.trade_text3, Toast.LENGTH_SHORT, true).show()
            return
        }
        AlertDialog.Builder(context).setMessage(R.string.trade_text4)
            .setNegativeButton(android.R.string.cancel) { dialogInterface, _ ->
                dialogInterface.dismiss()
            }.setPositiveButton(android.R.string.ok) { dialogInterface, _ ->
                dialogInterface.dismiss()
                NetConfig.getService(IOSTService::class.java).getChainInfo().enqueue(HttpCallBack({
                    val `data` =
                        arrayListOf(tradeSymbol, item.id.toString())
                    val payload =
                        createPayLoad("cancellations", data, it.head_block_time)
                    tp(context, payload)
                }))
            }.show()
    }

    private fun onViewHolder(viewHolder: BaseViewAdapter.ViewHolder) {
        val viewBinding = DataBindingUtil.findBinding<ViewDataBinding>(viewHolder.itemView)
        viewBinding!!.setVariable(BR.symbol, ObservableField<String>(tradeSymbolName))
    }

    private fun sendHttp() {
        if (!User.isLogin())
            return
        if (call.isExecuted)
            call.cancel()
        call = NetConfig.getService(
            ApiService::class.java
        )
            .orders(
                1,
                tradeSymbol,
                User.account!!
            )
        call.enqueue(HttpCallBack(this::onSuccess))
    }

    private fun onSuccess(resp: PageResp<RecordOrderResp>) {
        if (resp.totalRecord != 0) {
            data.clear()
            data.addAll(resp.list)
            adapter.notifyDataSetChanged()
        }
    }
}