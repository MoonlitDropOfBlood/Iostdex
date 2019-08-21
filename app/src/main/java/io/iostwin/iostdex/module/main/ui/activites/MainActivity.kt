package io.iostwin.iostdex.module.main.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.ActivityMainBinding
import io.iostwin.iostdex.module.main.ui.fragments.HomeFragment
import com.sankuai.waimai.router.annotation.RouterUri
import io.iostwin.iostdex.domain.LoginMessage
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.module.main.ui.fragments.AssetsFragment
import io.iostwin.iostdex.module.main.ui.fragments.LoginFragment
import io.iostwin.iostdex.module.main.ui.fragments.OrderFragment
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe

@RouterUri(path = ["/"])
class MainActivity : AppCompatActivity() {
    private val loginFragment = LoginFragment()
    private val tabFragment = arrayListOf(HomeFragment(), OrderFragment(), AssetsFragment())
    private var mCurrentFragment: BaseFragment = tabFragment[0]
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        EventBus.getDefault().register(this)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportFragmentManager.beginTransaction()
            .add(R.id.main_content, mCurrentFragment, HomeFragment::class.java.name)
            .show(mCurrentFragment).commit()
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    changeFragment(tabFragment[0])
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_order -> {
                    if (!User.isLogin()) {
                        changeFragment(loginFragment)
                    } else {
                        changeFragment(tabFragment[1])
                    }
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_assets -> {
                    if (!User.isLogin()) {
                        changeFragment(loginFragment)
                    } else {
                        changeFragment(tabFragment[2])
                    }
                    return@setOnNavigationItemSelectedListener true
                }
            }
            false
        }
    }

    private fun changeFragment(fragment: BaseFragment) {
        val begin = supportFragmentManager.beginTransaction().hide(mCurrentFragment)
        if (!supportFragmentManager.fragments.contains(fragment)) {
            fragment.arguments = Bundle()
            begin.add(R.id.main_content, fragment, fragment::class.java.name)
        }
        begin.show(fragment).commitAllowingStateLoss()
        mCurrentFragment = fragment
    }

    @Subscribe
    fun onLoginSuccess(message: LoginMessage) {
        User.account = message.account
        when (binding.navView.selectedItemId) {
            R.id.navigation_order -> {
                changeFragment(tabFragment[1])
            }
            R.id.navigation_assets -> {
                changeFragment(tabFragment[2])
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        EventBus.getDefault().unregister(this)
    }
}
