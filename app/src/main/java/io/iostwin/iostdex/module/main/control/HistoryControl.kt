package io.iostwin.iostdex.module.main.control

import android.net.Uri
import android.view.View
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.R
import io.iostwin.iostdex.domain.HistoryOrderResp
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.NetConfig

class HistoryControl : OrderListControl<HistoryOrderResp>() {
    override fun getLayoutRes(): Int = R.layout.item_history

    override fun itemClick(view: View, position: Int) {
        Router.startUri(
            UriRequest(
                view.context,
                Uri.Builder().path("/web").appendQueryParameter(
                    "url",
                    "https://www.iostabc.com/tx/${data[position].tradeHx}"
                ).build()
            )
        )
    }

    override fun sendHttp() =
        NetConfig.getService(ApiService::class.java).his(page, symbol, User.account!!)
}