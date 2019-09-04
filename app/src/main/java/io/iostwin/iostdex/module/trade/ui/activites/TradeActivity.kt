package io.iostwin.iostdex.module.trade.ui.activites

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.R

@RouterUri(path = ["/tradeToken"])
class TradeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_trade)
    }
}
