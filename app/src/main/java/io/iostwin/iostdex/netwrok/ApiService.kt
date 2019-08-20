package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.domain.TokenSymbolResp
import retrofit2.Call
import retrofit2.http.GET

interface ApiService {

    @GET("/api/chart/all/")
    fun chartAll(): Call<ArrayList<TokenSymbolResp>>

}