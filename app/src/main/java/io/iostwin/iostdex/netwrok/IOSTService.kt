package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.AccountResp
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface IOSTService {

    @Headers("header_extend:iost")
    @GET("/getAccount/{account}/0")
    fun getAccount(@Path("account") account:String):Call<AccountResp>
}