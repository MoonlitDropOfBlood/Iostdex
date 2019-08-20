package io.iostwin.iostdex.netwrok

import io.iostwin.iostdex.R
import io.iostwin.iostdex.utils.Utils.toast
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException

/**
 * 统一返回处理类
 */
class HttpCallBack<T>(
    private val success: (response: T) -> Unit,
    private val fail: ((t: Throwable) -> Unit)? = null
) : Callback<T> {

    override fun onResponse(call: Call<T>, response: Response<T>) {
        if (response.isSuccessful && response.body() != null) {
            onSuccess(response.body()!!)
        } else {
            onFailure(call, IOException(response.errorBody()!!.string()))
        }
    }

    private fun onSuccess(response: T) {
        success(response)
    }

    override fun onFailure(call: Call<T>, t: Throwable) {
        fail?.invoke(t)
        if (t is IOException) {
            toast(R.string.app_network_socket_timeout)
        }
        t.printStackTrace()
    }
}