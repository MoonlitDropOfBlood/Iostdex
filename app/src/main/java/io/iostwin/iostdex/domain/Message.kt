package io.iostwin.iostdex.domain

import java.math.BigDecimal
import java.util.Date
import kotlin.collections.ArrayList

data class LoginMessage(val account: String)

data class LogoutMessage(val date: Date)

data class OrderHttpMessage(val isTop: Boolean)

data class OrderFinishMessage(val isTop: Boolean, val success: Boolean, val isEnd: Boolean)

data class OrderFiltrateMessage(
    val symbol: String,
    val direction: Int? = null,
    val status: Int? = null,
    val startTime: Int? = null,
    val endTime: Int? = null
)

data class OrderMessage(val buy: Boolean, val data: ArrayList<Order>)

data class HistoryMessage(val data: ArrayList<History>)

data class TradeMessage(val buy: OrderMessage, val sell: OrderMessage, val price: BigDecimal,val percent:BigDecimal)

data class PriceMessage(
    val amount24H: BigDecimal,
    val maxPrice24H: BigDecimal,
    val minPrice24H: BigDecimal,
    val percent24H: BigDecimal,
    val volume24H: BigDecimal,
    val price: BigDecimal,
    val supply: BigDecimal
)

data class OnPopWindowMessage(
    val type: Int
)

data class TokenInfoMessage(
    val name: String?,
    val website: String?,
    val maxSupply: BigDecimal,
    val symbol: String,
    val desc: String?,
    val icon: String
)

data class TradeIndexMessage(val isMain: Boolean, val index: Int)

