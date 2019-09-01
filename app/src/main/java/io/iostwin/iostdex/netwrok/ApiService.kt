package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.*
import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("/api/chart/all/")
    fun chartAll(): Call<ArrayList<TokenSymbolResp>>

    @FormUrlEncoded
    @POST("/api/trade/orders/")
    fun orders(
        @Field("page") page: Int, @Field("symbol") symbol: String, @Field("user") user: String,
        @Field("direction") direction: Int?, @Field("status") status: Int?,
        @Field("starttime") startTime: Int?, @Field("endtime") endTime: Int?
    ): Call<PageResp<RecordOrderResp>>

    @FormUrlEncoded
    @POST("/api/trade/his/")
    fun his(
        @Field("page") page: Int, @Field("symbol") symbol: String, @Field("user") user: String
    ): Call<PageResp<HistoryOrderResp>>

    @GET("/api/chart/history")
    fun cartHistory(
        @Query("symbol") symbol: String, @Query("resolution") resolution: String, @Query(
            "from"
        ) from: Int, @Query("to") to: Int
    ): Call<ChartHistoryResp>
}