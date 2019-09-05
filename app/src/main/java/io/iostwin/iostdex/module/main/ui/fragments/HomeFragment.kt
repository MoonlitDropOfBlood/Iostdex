package io.iostwin.iostdex.module.main.ui.fragments

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentHomeBinding
import io.iostwin.iostdex.module.main.control.HomeControl
import io.iostwin.iostdex.module.main.ui.activites.MainActivity
import io.iostwin.iostdex.utils.MultiLanguageUtils


class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.control = HomeControl(binding)
        binding.toolbar.inflateMenu(R.menu.language_nav_menu)
        val moreIcon =
            ContextCompat.getDrawable(binding.root.context, R.drawable.ic_language_white_24dp)
        binding.toolbar.overflowIcon = moreIcon
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.hk_cn -> {
                    MultiLanguageUtils.changeLanguage(binding.root.context, "zh", "HK")
                }
                R.id.gb_en -> {
                    MultiLanguageUtils.changeLanguage(binding.root.context, "en", "GB")
                }
                R.id.kr_ko -> {
                    MultiLanguageUtils.changeLanguage(binding.root.context, "ko", "KR")
                }
                else -> {
                    MultiLanguageUtils.changeLanguage(binding.root.context, "", "")
                }
            }
            val intent = Intent(binding.root.context, MainActivity::class.java)
            //清空任务栈确保当前打开activit为前台任务栈栈顶
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK.or(Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
            return@setOnMenuItemClickListener true
        }
        return binding.root
    }

    override fun initData() {
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            binding.control?.stop()
        } else {
            binding.control?.start()
        }
    }

    override fun onResume() {
        super.onResume()
        binding.control?.start()
    }

    override fun onPause() {
        super.onPause()
        binding.control?.stop()
    }
}