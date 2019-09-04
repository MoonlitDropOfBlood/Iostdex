package io.iostwin.iostdex.domain

import android.content.Context
import com.tokenpocket.opensdk.base.TPListener
import com.tokenpocket.opensdk.base.TPManager
import com.tokenpocket.opensdk.simple.model.Authorize
import io.iostwin.iostdex.R
import io.iostwin.iostdex.utils.md5
import org.greenrobot.eventbus.EventBus
import org.json.JSONObject
import java.math.BigDecimal


object User {
    var account: String? = null
    private var actionId: String = ""
    fun isLogin(): Boolean = account != null

    fun login(context: Context) {
        actionId = md5(System.nanoTime().toString())
        val authorize = Authorize()
        authorize.blockchain = "IOST"
        authorize.dappName = "IOSTDEX"
        authorize.dappIcon =
            "https://dapp-assets.dappradar.com/32bae78a7f9d20914c93fa6385a2bd8d.png"
        authorize.actionId = actionId
        authorize.expired = System.currentTimeMillis() / 1000 + 300
        authorize.memo = context.getString(R.string.login_slogan)
        TPManager.getInstance().authorize(context, authorize, object : TPListener {
            override fun onSuccess(data: String) {
                val jsonObject = JSONObject(data)
                if (jsonObject.optString("actionId") == actionId)
                    EventBus.getDefault().post(LoginMessage(jsonObject.optString("wallet")))
            }

            override fun onError(data: String) {

            }

            override fun onCancel(data: String) {

            }
        })
    }
}

data class Assets(
    var total: BigDecimal,
    val tokenAssets: ArrayList<TokenAssets>
)

data class TokenAssets(
    val icon: String,
    val name: String,
    val symbol: String,
    val balance: BigDecimal,
    val frozenBalance: BigDecimal,
    val price: BigDecimal,
    val assess: BigDecimal,
    val digit: Int
)