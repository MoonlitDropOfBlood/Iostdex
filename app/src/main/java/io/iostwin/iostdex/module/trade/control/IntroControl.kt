package io.iostwin.iostdex.module.trade.control

import android.net.Uri
import android.view.View
import androidx.databinding.ObservableField
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.domain.PriceMessage
import io.iostwin.iostdex.domain.TokenInfoMessage
import org.greenrobot.eventbus.Subscribe
import java.math.BigDecimal

class IntroControl {
    val icon = ObservableField<String>()
    val name = ObservableField<String>()
    val symbol = ObservableField<String>()
    val maxSupply = ObservableField<BigDecimal>()
    val availableSupply = ObservableField<BigDecimal>()
    val website = ObservableField<String>()
    val desc = ObservableField<String>()

    @Subscribe
    fun onPriceMessage(message: PriceMessage) {
        availableSupply.set(message.supply)
    }

    @Subscribe
    fun onTokenInfoMessage(message: TokenInfoMessage) {
        if (icon.get() == null)
            icon.set(message.icon)
        message.name?.run {
            if (name.get() == null)
                name.set(this)
        }
        if (symbol.get() == null)
            symbol.set(message.symbol)
        if (maxSupply.get() == null)
            maxSupply.set(message.maxSupply)
        if (website.get() == null)
            website.set(message.website)
        if (desc.get() == null)
            desc.set(message.desc)
    }

    fun onClick(v: View) {
        if (website.get() != null)
            Router.startUri(
                UriRequest(
                    v.context,
                    Uri.Builder().path("/web").appendQueryParameter(
                        "url",
                        website.get()
                    ).build()
                )
            )
    }
}