package io.iostwin.iostdex.module.main.control

import android.content.Context
import android.net.Uri
import android.os.AsyncTask
import androidx.databinding.ObservableField
import androidx.databinding.library.baseAdapters.BR
import com.sankuai.waimai.router.Router
import com.sankuai.waimai.router.core.UriRequest
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseViewAdapter
import io.iostwin.iostdex.databinding.FragmentAssetsBinding
import io.iostwin.iostdex.domain.Assets
import io.iostwin.iostdex.domain.TokenAssets
import io.iostwin.iostdex.domain.TokenSymbolResp
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.IOSTService
import io.iostwin.iostdex.netwrok.NetConfig
import java.io.IOException
import java.math.BigDecimal

class AssetsControl(private val binding: FragmentAssetsBinding) {
    private val tokens = arrayListOf<TokenSymbolResp>()
    private val assets = arrayListOf<TokenAssets>()
    var assetsAll = true
    private val fail = fun(_: Throwable) {
        binding.refreshLayout.finishRefresh(false)//传入false表示刷新失败
    }
    private val data = arrayListOf<TokenAssets>()

    val adapter = BaseViewAdapter(
        data,
        R.layout.item_assets,
        BR.viewModel,
        mItemClickListener = this::onItemClick
    )

    val totalAmount = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val gas = ObservableField<BigDecimal>(BigDecimal.ZERO)
    val ram = ObservableField<BigDecimal>(BigDecimal.ZERO)

    init {
        binding.refreshLayout.setOnRefreshListener { sendHttp() }
    }

    fun autoRefresh() {
        binding.refreshLayout.autoRefresh()
    }

    private fun onItemClick(context: Context, tokenAssets: TokenAssets) {
        Router.startUri(
            UriRequest(
                context,
                Uri.Builder().path("/tokenInfo").appendQueryParameter(
                    "icon", tokenAssets.icon
                ).appendQueryParameter("symbol", tokenAssets.symbol).appendQueryParameter(
                    "name",
                    tokenAssets.name
                ).appendQueryParameter("decimal", tokenAssets.digit.toString()).build()
            )
        )
    }

    private fun sendHttp() {
        if (User.isLogin()) {
            NetConfig.getService(ApiService::class.java).chartAll()
                .enqueue(HttpCallBack(this::success, fail))
            NetConfig.getService(IOSTService::class.java).getAccount(User.account!!)
                .enqueue(HttpCallBack({
                    var total = it.balance
                    for (frozen in it.frozen_balances) {
                        total = total.add(frozen.amount)
                    }
                    totalAmount.set(total)
                    gas.set(
                        it.gas_info.current_total.minus(it.gas_info.limit).setScale(
                            2,
                            BigDecimal.ROUND_HALF_UP
                        ).div(it.gas_info.limit).multiply(
                            BigDecimal(100)
                        )
                    )
                    ram.set(
                        it.ram_info.used.setScale(
                            2,
                            BigDecimal.ROUND_HALF_UP
                        ).div(it.ram_info.total).multiply(BigDecimal(100))
                    )
                }))
        } else {
            binding.refreshLayout.finishRefresh()
        }
    }

    private fun success(response: ArrayList<TokenSymbolResp>) {
        tokens.clear()
        tokens.addAll(response)
        binding.refreshLayout.finishRefresh()
        val task = BalanceTask(this::taskResult, tokens)
        task.execute(*tokens.toTypedArray())
    }

    private fun taskResult(result: Assets) {
        totalAmount.set(result.total)
        assets.clear()
        assets.addAll(result.tokenAssets)
        data.clear()
        if (assetsAll) {
            data.addAll(assets)
        } else {
            assets.forEach {
                if (it.assess > BigDecimal.ZERO)
                    data.add(it)
            }
        }
        adapter.notifyDataSetChanged()
    }

    class BalanceTask(
        private val finish: (result: Assets) -> Unit, private val tokens: ArrayList<TokenSymbolResp>
    ) : AsyncTask<TokenSymbolResp, Unit, Assets>() {
        override fun doInBackground(vararg p0: TokenSymbolResp): Assets {
            val assets = Assets(BigDecimal.ZERO, arrayListOf())
            val service = NetConfig.getService(IOSTService::class.java)
            try {
                val resp = service.getTokenBalance(User.account!!, "iost").execute().body()!!
                val frozenBalanceIOST = BigDecimal.ZERO
                for (item in resp.frozen_balances) {
                    frozenBalanceIOST.add(item.amount)
                }
                val assetsValueIOST = resp.balance.add(frozenBalanceIOST)
                assets.total = assetsValueIOST
                assets.tokenAssets.add(
                    TokenAssets(
                        "/static/img/logo/IOST.png",
                        "IOST",
                        "iost",
                        resp.balance,
                        frozenBalanceIOST,
                        BigDecimal.ONE,
                        resp.balance.add(frozenBalanceIOST),
                        8
                    )
                )
                for (i in p0.indices) {
                    val it = p0[i]
                    val result =
                        service.getTokenBalance(User.account!!, it.symbol).execute().body()!!
                    val frozenBalance = BigDecimal.ZERO
                    for (item in result.frozen_balances) {
                        frozenBalance.add(item.amount)
                    }
                    val price = it.price ?: BigDecimal.ZERO
                    val assetsValue =
                        result.balance.add(frozenBalanceIOST).multiply(price).setScale(
                            8,
                            BigDecimal.ROUND_HALF_UP
                        )
                    assets.total = assets.total.add(assetsValue)
                    assets.tokenAssets.add(
                        TokenAssets(
                            it.icon, it.name, it.symbol, result.balance, frozenBalance,
                            price,
                            assetsValue,
                            tokens[i].digit
                        )
                    )
                }
            } catch (e: IOException) {

            }
            return assets
        }

        override fun onPostExecute(result: Assets) {
            super.onPostExecute(result)
            finish(result)
        }
    }
}