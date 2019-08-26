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

    override fun toString(): String {
        return name
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
    val tradeHx: String,
    val user: String
)

data class HistoryOrderResp(
    val buy: Int,
    val fee: BigDecimal,
    @SerializedName("feesymbol") val feeSymbol: String,
    val finishAmount: BigDecimal,
    val finishNum: BigDecimal,
    val finishTime: Long,
    val fromUser: String,
    val id: Int,
    val orderId: Int,
    val price: BigDecimal,
    val symbol: String,
    val toUser: String,
    val tradeHx: String
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

data class WebSocketTradeMessage(
    val amount_24h: BigDecimal?,
    val buy: List<Order>?,
    val desc: String?,
    val digit: Int?,
    val his: List<Hi>?,
    val icon: String?,
    val isenabled: Int?,
    val maxprice_24h: BigDecimal?,
    val maxsupply: BigDecimal?,
    val minprice_24h: BigDecimal?,
    val name: String?,
    val paused: Int?,
    val percent_24h: BigDecimal?,
    val price: BigDecimal?,
    val price_lasthour: BigDecimal?,
    val sell: List<Order>?,
    val sort: Int?,
    val supply: BigDecimal?,
    val symbol: String,
    val up: Int?,
    val volumn_24h: BigDecimal?,
    val volumn_lasthour: BigDecimal?,
    val website: String?
)

data class Hi(
    val buy: Int,
    val id: Int,
    val num: BigDecimal,
    val price: BigDecimal,
    val tradetime: Int
)

data class Order(
    val amount: BigDecimal,
    val balance: BigDecimal,
    val price: BigDecimal
)