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
import android.os.Build
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import io.iostwin.iostdex.BuildConfig
import io.iostwin.iostdex.module.trade.service.WebSocketService

@RouterUri(path = ["/tokenInfo"])
class TokenInfoActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityTokenInfoBinding>(
            this,
            R.layout.activity_token_info
        )
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setHomeButtonEnabled(true)
        val uri = intent.data!!
        val symbol = uri.getQueryParameter("symbol")!!
        val icon = uri.getQueryParameter("icon")!!
        val name = uri.getQueryParameter("name")!!
        supportActionBar!!.title = "$name/IOST"
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
        stopService(Intent(this, WebSocketService::class.java))
    }
}
