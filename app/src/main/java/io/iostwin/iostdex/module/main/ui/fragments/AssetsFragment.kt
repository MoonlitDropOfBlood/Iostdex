package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentAssetsBinding
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.module.main.control.AssetsControl

class AssetsFragment : BaseFragment() {
    private lateinit var control: AssetsControl
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<FragmentAssetsBinding>(
            inflater,
            R.layout.fragment_assets,
            container,
            false
        )
        control = AssetsControl(binding)
        binding.control = control
        binding.user = User
        initData()
        return binding.root
    }

    override fun initData() {
        control.autoRefresh()
    }
}