@file:Suppress("DEPRECATION")

package io.iostwin.iostdex.module.trade.ui.activites

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.databinding.DataBindingUtil
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.ActivityTokenInfoBinding
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.module.trade.control.TokenInfoControl
import io.iostwin.iostdex.module.trade.service.WebSocketService
import org.greenrobot.eventbus.EventBus

@RouterUri(path = ["/tokenInfo"])
class TokenInfoActivity : AppCompatActivity() {

    private lateinit var control: TokenInfoControl

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityTokenInfoBinding>(
            this,
            R.layout.activity_token_info
        )
        val uri = intent.data!!
        val symbol = uri.getQueryParameter("symbol")!!
        val icon = uri.getQueryParameter("icon")!!
        val name = uri.getQueryParameter("name")!!
        control = TokenInfoControl(binding, symbol)
        binding.control = control
        EventBus.getDefault().register(control)

        setSupportActionBar(binding.toolbar)
        supportActionBar!!.apply {
            setHomeButtonEnabled(true)
            title = "$name/IOST"
        }
        val px = resources.getDimensionPixelOffset(R.dimen.activity_horizontal_margin) / 2 * 3
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
        val intent = Intent(this, WebSocketService::class.java)
        intent.putExtra("symbol", symbol)
        startService(intent)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            onBackPressed()
            return true
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(control)
        stopService(Intent(this, WebSocketService::class.java))
    }
}
