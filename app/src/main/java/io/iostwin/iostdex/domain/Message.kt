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

data class OrderMessage(val buy: Int, val data: ArrayList<Order>)

data class HistoryMessage(val data: ArrayList<History>)

data class PriceMessage(
    val amount24H: BigDecimal,
    val maxPrice24H: BigDecimal,
    val minPrice24H: BigDecimal,
    val percent24H: BigDecimal,
    val volume24H: BigDecimal,
    val price: BigDecimal,
    val supply: BigDecimal
)

//data class LastPriceMessage(
//    val priceLastHour: BigDecimal,
//    val volumeLastHour: BigDecimal
//)

data class TokenInfoMessage(
    val website: String?,
    val maxSupply: BigDecimal,
    val symbol: String,
    val desc: String?,
    val icon: String
)

