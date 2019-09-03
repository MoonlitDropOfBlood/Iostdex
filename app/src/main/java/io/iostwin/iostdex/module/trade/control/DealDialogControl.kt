package io.iostwin.iostdex.module.trade.control

import android.app.Dialog
import android.net.Uri
import android.view.View
import android.widget.TextView
import androidx.databinding.ObservableField
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.domain.TradeDetailResp

class DealDialogControl(val tradeSymbol: String, private val dialog: Dialog) {
    val mainSymbol = "IOST"
    val detail = ObservableField<TradeDetailResp>()

    fun close() {
        dialog.dismiss()
    }

    fun startDetail(view: View) {
        val text = (view as TextView).text
        val path = if (text.length < 13) "account" else "tx"
        Router.startUri(
            UriRequest(
                view.context,
                Uri.Builder().path("/web").appendQueryParameter(
                    "url",
                    "https://www.iostabc.com/$path/$text"
                ).build()
            )
        )
    }
}