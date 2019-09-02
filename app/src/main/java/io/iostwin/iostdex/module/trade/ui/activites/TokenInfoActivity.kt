@file:Suppress("DEPRECATION")

package io.iostwin.iostdex.module.trade.ui.activites

import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.ActivityTokenInfoBinding
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.view.ViewGroup
import android.widget.PopupWindow
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.fujianlian.klinechart.draw.Status
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.databinding.PopwindowTradeIndexBinding
import io.iostwin.iostdex.databinding.PopwindowTradeMoreBinding
import io.iostwin.iostdex.domain.OnPopWindowMessage
import io.iostwin.iostdex.domain.TradeIndexMessage
import io.iostwin.iostdex.module.trade.control.TokenInfoControl
import io.iostwin.iostdex.module.trade.control.TradeIndexControl
import io.iostwin.iostdex.module.trade.control.TradeMoreControl
import io.iostwin.iostdex.module.trade.service.WebSocketService
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@RouterUri(path = ["/tokenInfo"])
class TokenInfoActivity : AppCompatActivity() {

    private lateinit var control: TokenInfoControl
    private lateinit var resolution: PopupWindow
    private lateinit var index: PopupWindow
    private lateinit var binding: ActivityTokenInfoBinding
    private var dp8: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_token_info
        )
        val uri = intent.data!!
        val symbol = uri.getQueryParameter("symbol")!!
        val icon = uri.getQueryParameter("icon")!!
        val name = uri.getQueryParameter("name")!!
        initToolBar(name, icon)
        control = TokenInfoControl(binding, symbol)
        binding.control = control
        EventBus.getDefault().register(this)
        EventBus.getDefault().register(control)
        val intent = Intent(this, WebSocketService::class.java)
        intent.putExtra("symbol", symbol)
        startService(intent)
        initResolution()
        initIndex()
    }

    private fun initToolBar(name: String, icon: String) {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            setHomeButtonEnabled(true)
            title = "$name/IOST"
        }
        dp8 = resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin) / 2
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

    private fun initResolution() {
        val tradeBinding = DataBindingUtil.inflate<PopwindowTradeMoreBinding>(
            layoutInflater,
            R.layout.popwindow_trade_more,
            null,
            false
        )
        tradeBinding.control = TradeMoreControl(control)
        resolution = PopupWindow(
            tradeBinding.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        initPopWindow(resolution)
    }

    private fun initIndex() {
        val tradeIndex = DataBindingUtil.inflate<PopwindowTradeIndexBinding>(
            layoutInflater,
            R.layout.popwindow_trade_index,
            null,
            false
        )
        tradeIndex.tradeMainHide.isSelected = true
        tradeIndex.tradeAuxiliaryHide.isSelected = true
        tradeIndex.control = TradeIndexControl()
        index = PopupWindow(
            tradeIndex.root,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            true
        )
        initPopWindow(index)
    }

    private fun initPopWindow(popupWindow: PopupWindow) {
        // 设置PopupWindow的背景
        popupWindow.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        // 设置PopupWindow是否能响应外部点击事件
        popupWindow.isOutsideTouchable = true
        // 设置PopupWindow是否能响应点击事件
        popupWindow.isTouchable = true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    @Subscribe
    fun onPopWindowMessage(message: OnPopWindowMessage) {
        if (resolution.isShowing)
            resolution.dismiss()
        if (index.isShowing)
            index.dismiss()
        if (message.type == 1)
            resolution.showAsDropDown(binding.tradeMore, 0, dp8 / 2)
        if (message.type == 2)
            index.showAsDropDown(binding.tradeIndex, 0, dp8 / 2)
    }

    @Subscribe
    fun onTradeIndexMessage(message: TradeIndexMessage) {
        binding.kLineChartView.hideSelectData()
        if (message.isMain) {
            when (message.index) {
                1 -> binding.kLineChartView.changeMainDrawType(Status.MA)
                2 -> binding.kLineChartView.changeMainDrawType(Status.BOLL)
                else -> binding.kLineChartView.changeMainDrawType(Status.NONE)
            }
        } else {
            if (message.index != 0)
                binding.kLineChartView.setChildDraw(message.index - 1)
            else
                binding.kLineChartView.hideChildDraw()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
        EventBus.getDefault().unregister(control)
        stopService(Intent(this, WebSocketService::class.java))
    }
}
