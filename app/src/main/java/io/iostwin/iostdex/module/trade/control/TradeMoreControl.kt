package io.iostwin.iostdex.module.trade.control

import android.view.View
import android.widget.TextView
import io.iostwin.iostdex.domain.OnPopWindowMessage
import org.greenrobot.eventbus.EventBus

class TradeMoreControl(private val listener: OnTradeSelectMoreListener) {
    fun onSelect(view: View) {
        listener.onTradeSelectMore((view as TextView).text.toString())
        EventBus.getDefault().post(OnPopWindowMessage(0))
    }
}