package io.iostwin.iostdex.module.main.ui.activites

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.ActivityMainBinding
import io.iostwin.iostdex.module.main.ui.fragments.HomeFragment
import com.sankuai.waimai.router.annotation.RouterUri

@RouterUri(path = ["/"])
class MainActivity : AppCompatActivity() {
    private val tabFragment = arrayListOf(HomeFragment())
    private var mCurrentFragment: BaseFragment = tabFragment[0]

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)

        supportFragmentManager.beginTransaction()
            .add(R.id.main_content, mCurrentFragment, HomeFragment::class.java.name).show(mCurrentFragment).commit()
        binding.navView.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.navigation_home -> {
                    changeFragment(tabFragment[0])
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_dashboard -> {
                    return@setOnNavigationItemSelectedListener true
                }
                R.id.navigation_notifications -> {
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
}
