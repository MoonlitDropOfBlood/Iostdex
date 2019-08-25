package io.iostwin.iostdex

import android.app.Application
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler
import io.iostwin.iostdex.netwrok.NetConfig
import io.iostwin.iostdex.utils.ContextHolder
import io.socket.client.IO


class IOSTDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        ContextHolder.init(this)
        val rootHandler = DefaultRootUriHandler(this)
        Router.init(rootHandler)
        IO.setDefaultOkHttpWebSocketFactory(NetConfig.webSocketClient)
        IO.setDefaultOkHttpCallFactory(NetConfig.webSocketClient)
    }
}