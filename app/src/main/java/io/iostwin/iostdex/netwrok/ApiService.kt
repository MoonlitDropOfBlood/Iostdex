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
        @Field("direction") direction: Int? = null, @Field("status") status: Int? = null,
        @Field("starttime") startTime: Int? = null, @Field("endtime") endTime: Int? = null
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

    @GET("/api/trade/detail/{symbol}/{id}")
    fun tradeDetail(@Path("symbol") symbol: String, @Path("id") id: Int): Call<TradeDetailResp>
}