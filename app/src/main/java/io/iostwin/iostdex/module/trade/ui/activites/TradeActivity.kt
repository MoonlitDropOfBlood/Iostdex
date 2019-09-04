@file:Suppress("DEPRECATION")

package io.iostwin.iostdex.module.trade.ui.activites

import android.annotation.SuppressLint
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.ActivityTradeBinding
import io.iostwin.iostdex.module.trade.control.TradeControl
import org.greenrobot.eventbus.EventBus

@RouterUri(path = ["/tradeToken"])
class TradeActivity : AppCompatActivity() {

    private lateinit var control: TradeControl

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<ActivityTradeBinding>(this, R.layout.activity_trade)
        val uri = intent.data!!
        val symbol = uri.getQueryParameter("symbol")!!
        val icon = uri.getQueryParameter("icon")!!
        val name = uri.getQueryParameter("name")!!
        val decimal = uri.getQueryParameter("decimal")!!.toInt()
        val isBuy = uri.getQueryParameter("isBuy")!!.toInt()
        initToolBar(name, icon, binding)
        control = TradeControl(isBuy, name, symbol, decimal)
        binding.control = control
        EventBus.getDefault().register(control)
    }

    private fun initToolBar(name: String, icon: String, binding: ActivityTradeBinding) {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            setHomeButtonEnabled(true)
            title = "$name/IOST"
        }
        val dp8 = resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin) / 2
        val px = dp8 * 3
        @Suppress("DEPRECATION") val target = object : SimpleTarget<Drawable>(px, px) {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                val src = LayerDrawable(arrayOf(getDrawable(R.drawable.bg_token_icon), resource))
                supportActionBar!!.setIcon(src)
            }
        }
        val mRequestOptions = RequestOptions.circleCropTransform()
        Glide.with(this) // could be an issue!
            .load(BuildConfig.RES_URL + icon)
            .apply(mRequestOptions)
            .into(target)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(control)
    }
}
