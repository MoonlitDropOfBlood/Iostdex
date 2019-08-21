package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.PageResp
import io.iostwin.iostdex.domain.RecordOrderResp
import io.iostwin.iostdex.domain.TokenSymbolResp
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET("/api/chart/all/")
    fun chartAll(): Call<ArrayList<TokenSymbolResp>>

    @POST("/api/trade/orders")
    fun orders(
        @Field("symbol") symbol: String, @Field("user") user: String, @Field("direction") direction: Int?,
        @Field("status") status: Int?, @Field("starttime") startTime: Long?, @Field("endtime") endTime: Long?
    ): Call<ArrayList<PageResp<RecordOrderResp>>>
}