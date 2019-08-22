package io.iostwin.iostdex.domain

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class TokenSymbolResp(
    val digit: Int,
    val name: String,
    val icon: String,
    val percent_24h: BigDecimal?,
    val price: BigDecimal?,
    val symbol: String
) {
    fun compareTo(): Int {
        return percent_24h?.run {
            this.compareTo(BigDecimal.ZERO)
        } ?: 0
    }
}

data class PageResp<T>(val list: ArrayList<T>, val maxPage: Int, val totalRecord: Int)

data class RecordOrderResp(
    val buy: Int,
    val fee: BigDecimal,
    @SerializedName("feesymbol") val feeSymbol: String,
    val finishAmount: BigDecimal,
    val finishNum: BigDecimal,
    val id: Long,
    @SerializedName("lasttime") val lastTime: Long,
    val price: BigDecimal,
    val status: Int,
    val time: Long,
    val totalNum: BigDecimal,
    val tradeHash: String,
    val user: String
)

data class TokenBalanceResp(val balance: BigDecimal, val frozen_balances: List<FrozenBalance>)

data class AccountResp(
    val balance: BigDecimal,
    val frozen_balances: List<FrozenBalance>,
    val gas_info: GasInfo,
    val name: String,
    val ram_info: RamInfo
)

data class FrozenBalance(
    val amount: BigDecimal,
    val time: Long
)

data class GasInfo(
    val current_total: BigDecimal,
    val limit: BigDecimal
)

data class RamInfo(
    val available: BigDecimal,
    val total: BigDecimal,
    val used: BigDecimal
)