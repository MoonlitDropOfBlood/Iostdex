package io.iostwin.iostdex.module.main.ui.activites

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseActivity
import io.iostwin.iostdex.databinding.LayoutLoginBinding
import io.iostwin.iostdex.domain.LoginMessage
import io.iostwin.iostdex.domain.User
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@RouterUri(path = ["/login"])
class LoginActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding =
            DataBindingUtil.setContentView<LayoutLoginBinding>(this, R.layout.layout_login)
        binding.user = User
        EventBus.getDefault().register(this)
    }

    @Subscribe
    fun onLogout(@Suppress("UNUSED_PARAMETER") message: LoginMessage) {
        finish()
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
