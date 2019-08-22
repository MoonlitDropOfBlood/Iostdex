package io.iostwin.iostdex.domain

import java.util.*

data class LoginMessage(val account: String)

data class LogoutMessage(val date: Date)

data class OrderHttpMessage(val isTop: Boolean)

data class OrderFinishMessage(val success: Boolean, val isEnd: Boolean)

data class OrderFiltrateMessage(
    val symbol: String,
    val direction: Int? = null,
    val status: Int = 0,
    val startTime: Int? = null,
    val endTime: Int? = null
)