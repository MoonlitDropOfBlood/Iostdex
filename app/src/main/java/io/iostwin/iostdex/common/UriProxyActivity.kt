package io.iostwin.iostdex.common

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.sankuai.waimai.router.core.UriRequest
import androidx.annotation.NonNull
import com.sankuai.waimai.router.core.OnCompleteListener
import com.sankuai.waimai.router.common.DefaultUriRequest



class UriProxyActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        DefaultUriRequest.startFromProxyActivity(this, object : OnCompleteListener {
            override fun onSuccess(request: UriRequest) {
                finish()
            }

            override fun onError(request: UriRequest, resultCode: Int) {
                finish()
            }
        })
    }
}
