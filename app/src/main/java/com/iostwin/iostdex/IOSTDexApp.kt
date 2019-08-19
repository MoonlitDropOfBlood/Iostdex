package com.iostwin.iostdex

import android.app.Application
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.common.DefaultRootUriHandler


class IOSTDexApp : Application() {
    override fun onCreate() {
        super.onCreate()
        val rootHandler = DefaultRootUriHandler(this)
        Router.init(rootHandler)
    }
}