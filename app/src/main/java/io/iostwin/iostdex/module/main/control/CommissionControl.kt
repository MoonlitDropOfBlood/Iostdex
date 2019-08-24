package io.iostwin.iostdex.module.main.control

import android.net.Uri
import android.view.View
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.R
import io.iostwin.iostdex.domain.OrderFiltrateMessage
import io.iostwin.iostdex.domain.RecordOrderResp
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.NetConfig

class CommissionControl : OrderListControl<RecordOrderResp>() {
    private var direction: Int? = null
    private var status: Int? = null
    private var startTime: Int? = null
    private var endTime: Int? = null
    override fun getLayoutRes(): Int = R.layout.item_commission

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

    override fun onOrderFiltrate(message: OrderFiltrateMessage) {
        super.onOrderFiltrate(message)
        direction = message.direction
        startTime = message.startTime
        status = message.status
        endTime = message.endTime
    }

    override fun sendHttp() = NetConfig.getService(ApiService::class.java)
        .orders(page, symbol, User.account!!, direction, status, startTime, endTime)
}