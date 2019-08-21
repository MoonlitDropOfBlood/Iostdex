package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

class IOSTInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val builder = request.newBuilder()
        val header: String? = request.header("header_extend")
        header?.let {
            if (it == "iost") {
                builder.removeHeader("header_extend")
                val httpUrl = request.url.newBuilder().host(BuildConfig.IOST_NET).build()
                builder.url(httpUrl)
            }
        }
        return chain.proceed(builder.build())
    }
}