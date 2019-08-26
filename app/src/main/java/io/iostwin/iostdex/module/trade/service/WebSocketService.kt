package io.iostwin.iostdex.module.trade.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import com.google.gson.Gson
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.domain.WebSocketTradeMessage
import io.socket.client.IO
import io.socket.client.Socket


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
            if (message.his != null) {

            }
            if (message.buy != null) {

            }
            if (message.sell != null) {

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
