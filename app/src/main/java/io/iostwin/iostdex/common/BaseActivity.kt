package io.iostwin.iostdex.common

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import io.iostwin.iostdex.utils.MultiLanguageUtils

abstract class BaseActivity: AppCompatActivity() {
    override fun attachBaseContext(newBase: Context) {
        super.attachBaseContext(MultiLanguageUtils.attachBaseContext(newBase))
    }
}