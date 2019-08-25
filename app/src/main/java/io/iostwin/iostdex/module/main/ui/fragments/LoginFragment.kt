package io.iostwin.iostdex.module.main.ui.fragments

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.LayoutLoginBinding
import io.iostwin.iostdex.domain.User

class LoginFragment : BaseFragment() {
    override fun initView(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding = DataBindingUtil.inflate<LayoutLoginBinding>(
            inflater,
            R.layout.layout_login,
            container,
            false
        )
        binding.user = User
        return binding.root
    }

    override fun initData() {
    }
}