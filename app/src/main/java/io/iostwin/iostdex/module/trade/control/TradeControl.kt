package io.iostwin.iostdex.module.trade.control

import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField

class TradeControl(
    isBuy: Int,
    val tradeSymbolName: String,
    private val tradeSymbol: String,
    val decimal: Int
) {
    val isBuy = ObservableBoolean(isBuy == 1)
    val price = ObservableField<String>()
    val num = ObservableField<String>()
    val isLimit = ObservableBoolean(true)

}