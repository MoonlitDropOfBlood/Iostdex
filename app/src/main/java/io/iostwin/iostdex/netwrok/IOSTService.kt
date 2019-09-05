package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.*
import retrofit2.Call
import retrofit2.http.*

interface IOSTService {

    @Headers("header_extend:iost")
    @GET("/getAccount/{account}/0")
    fun getAccount(@Path("account") account: String): Call<AccountResp>

    @Headers("header_extend:iost")
    @GET("/getTokenBalance/{account}/{tokenSymbol}/1")
    fun getTokenBalance(@Path("account") account: String, @Path("tokenSymbol") tokenSymbol: String): Call<TokenBalanceResp>

    @Headers("header_extend:iost")
    @GET("/getChainInfo")
    fun getChainInfo(): Call<ChainInfo>

    @Headers("header_extend:iost")
    @POST("/getBatchContractStorage")
    fun getBatchContractStorage(@Body body: BatchContractStorageReq): Call<BatchContractStorageResp>
}