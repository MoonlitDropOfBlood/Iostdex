package com.iostwin.iostdex.netwrok

import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object NetConfig {

    val client = OkHttpClient.Builder()
        .retryOnConnectionFailure(true)//允许失败重试
        .readTimeout(60, TimeUnit.SECONDS)//设置读取超时时间
        .writeTimeout(60, TimeUnit.SECONDS)//设置写的超时时间
        .connectTimeout(60, TimeUnit.SECONDS)//设置连接超时时间
        .build()

    val retrofit = Retrofit.Builder()
        .baseUrl("https://api.iostdex.io")
        .client(client)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
}