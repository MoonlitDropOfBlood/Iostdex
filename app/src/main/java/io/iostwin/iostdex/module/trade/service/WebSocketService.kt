package io.iostwin.iostdex.module.trade.service

import android.app.Service
import android.content.Intent
import android.os.IBinder
import android.util.Log
import io.iostwin.iostdex.BuildConfig
import io.socket.client.IO
import io.socket.client.Socket


class WebSocketService : Service() {

    private var socket: Socket? = null

    override fun onBind(intent: Intent): IBinder? = null

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val opts = IO.Options()
        opts.forceNew = true
        opts.reconnection = true
        socket?.close()

        socket = IO.socket(BuildConfig.WS_URL, opts)
        socket!!.on("TradeMessage") {
            Log.i("Socket", it.toString())
        }
        socket!!.open()
        return START_REDELIVER_INTENT
    }

    override fun onDestroy() {
        super.onDestroy()
        socket?.close()
    }

}
