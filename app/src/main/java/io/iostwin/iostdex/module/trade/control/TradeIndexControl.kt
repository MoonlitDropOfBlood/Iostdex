package io.iostwin.iostdex.module.trade.control

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.get
import androidx.core.view.size
import io.iostwin.iostdex.domain.OnPopWindowMessage
import io.iostwin.iostdex.domain.TradeIndexMessage
import org.greenrobot.eventbus.EventBus

class TradeIndexControl {
    fun onClick(view: View, isMain: Boolean, index: Int) {
        val group = view.parent as ViewGroup
        for (i in 0 until group.size) {
            if (group[i] is TextView) {
                group[i].isSelected = false
            }
        }
        view.isSelected = true
        val event = EventBus.getDefault()
        event.post(OnPopWindowMessage(0))
        event.post(TradeIndexMessage(isMain, index))
    }
}