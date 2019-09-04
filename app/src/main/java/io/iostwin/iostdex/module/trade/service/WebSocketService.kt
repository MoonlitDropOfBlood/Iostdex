package io.iostwin.iostdex.module.trade.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.Gson
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.domain.*
import io.socket.client.IO
import io.socket.client.Socket
import org.greenrobot.eventbus.EventBus


class WebSocketService : Service() {

    private var socket: Socket? = null
    private val gson = Gson()

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent, flags: Int, startId: Int): Int {
        val opts = IO.Options()
        opts.forceNew = true
        opts.reconnection = true
        socket?.close()

        socket = IO.socket("${BuildConfig.WS_URL}trade_${intent.getStringExtra("symbol")}", opts)
        socket!!.on("TradeMessage") {
            val json = it[0].toString()
            val message = gson.fromJson(json, WebSocketTradeMessage::class.java)
            message.his?.run {
                EventBus.getDefault().post(HistoryMessage(arrayListOf(*this.toTypedArray())))
            }
            message.buy?.run {
                EventBus.getDefault()
                    .post(OrderMessage(true, arrayListOf(*this.toTypedArray())))
            }
            message.sell?.run {
                EventBus.getDefault()
                    .post(OrderMessage(false, arrayListOf(*this.toTypedArray())))
            }
            message.apply {
                if (amount_24h != null) {
                    EventBus.getDefault()
                        .post(
                            PriceMessage(
                                amount_24h,
                                maxprice_24h!!,
                                minprice_24h!!,
                                percent_24h!!,
                                volumn_24h!!,
                                price!!,
                                supply!!
                            )
                        )
                }
//                if (price_lasthour != null) {
//                    EventBus.getDefault()
//                        .post(
//                            LastPriceMessage(
//                                price_lasthour,
//                                volumn_lasthour!!
//                            )
//                        )
//                }
                if (maxsupply != null) {
                    EventBus.getDefault()
                        .post(
                            TokenInfoMessage(
                                name,
                                website,
                                maxsupply,
                                symbol,
                                desc,
                                icon!!
                            )
                        )
                }
            }
        }
        socket!!.open()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.close()
    }

}
