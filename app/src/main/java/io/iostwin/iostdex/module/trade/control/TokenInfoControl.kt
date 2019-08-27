package io.iostwin.iostdex.module.trade.control

import androidx.databinding.ObservableField
import io.iostwin.iostdex.domain.PriceMessage
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class TokenInfoControl {
    val price = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val percent = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val max = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val min = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val volume = ObservableField<String>("0")
    var isToken = true
    var volumeToken = BigDecimal.ZERO
    var volumeMainToken = BigDecimal.ZERO


    @Subscribe
    fun onPriceMessage(message: PriceMessage) {
        price.set(message.price)
        percent.set(message.percent24H)
        max.set(message.maxPrice24H)
        min.set(message.minPrice24H)
        volumeToken = message.volume24H
        volumeMainToken = message.amount24H
        if (isToken) {
            volume.set(volumeToken.toPlainString())
        } else {
            volume.set(volumeMainToken.toPlainString() + "IOST")
        }
    }
}