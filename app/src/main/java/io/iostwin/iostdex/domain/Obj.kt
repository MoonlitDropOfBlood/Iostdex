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

data class TPPaylod(
    val account: String,
    val tx: Tx,
    val txABI: List<Any>,
    val domain: String = "www.iostdex.io",
    val network: String = "MAINNET"
)

data class Tx(
    val actions: List<Action>,
    val amount_limit: List<AmountLimit>,
    val time: Long,
    val expiration: Long,
    val chain_id: Int = 1024,
    val delay: Int = 0,
    val gasLimit: Int = 100000,
    val gasRatio: Int = 1,
    val publisher: String = "",
    val publisher_sigs: List<Any> = listOf(),
    val reserved: Any? = null,
    val signatures: List<Any> = listOf(),
    val signers: List<Any> = listOf()
)

data class Action(
    val actionName: String,
    val contract: String,
    val `data`: String
)

data class AmountLimit(
    val token: String,
    val value: String
)