package io.iostwin.iostdex.utils

import java.lang.StringBuilder
import java.math.BigDecimal
import java.security.MessageDigest

val tenThousand = BigDecimal("10000")
val thousand = BigDecimal("1000")
val oneHundred = BigDecimal("100")

fun md5(text: String): String {
    //获取md5加密对象
    val instance = MessageDigest.getInstance("MD5")
    //对字符串加密，返回字节数组
    val digest: ByteArray = instance.digest(text.toByteArray())
    val sb = StringBuilder()
    for (b in digest) {
        //获取低八位有效值
        val i: Int = b.toInt() and 0xff
        //将整数转化为16进制
        var hexString = Integer.toHexString(i)
        if (hexString.length < 2) {
            //如果是一位的话，补0
            hexString = "0$hexString"
        }
        sb.append(hexString)
    }
    return sb.toString()
}