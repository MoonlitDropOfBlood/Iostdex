package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentHomeBinding
import io.iostwin.iostdex.module.main.control.HomeControl


class HomeFragment : BaseFragment() {
    private lateinit var binding: FragmentHomeBinding
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        binding.control = HomeControl(binding)
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