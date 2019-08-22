package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.AccountResp
import io.iostwin.iostdex.domain.TokenBalanceResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IOSTService {

    @Headers("header_extend:iost")
    @GET("/getAccount/{account}/0")
    fun getAccount(@Path("account") account: String): Call<AccountResp>

    @Headers("header_extend:iost")
    @GET("/getTokenBalance/{account}/{tokenSymbol}/1")
    fun getTokenBalance(@Path("account") account: String, @Path("tokenSymbol") tokenSymbol: String): Call<TokenBalanceResp>
}