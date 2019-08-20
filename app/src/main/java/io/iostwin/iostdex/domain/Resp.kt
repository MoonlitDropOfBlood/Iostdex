package io.iostwin.iostdex.domain

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