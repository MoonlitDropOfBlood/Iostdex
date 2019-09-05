package io.iostwin.iostdex.module.main.ui.fragments

import android.view.*
import android.widget.Toolbar
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.BaseFragment
import io.iostwin.iostdex.databinding.FragmentAssetsBinding
import io.iostwin.iostdex.domain.LogoutMessage
import io.iostwin.iostdex.domain.User
import io.iostwin.iostdex.module.main.control.AssetsControl
import org.greenrobot.eventbus.EventBus
import java.util.*
import androidx.recyclerview.widget.DividerItemDecoration
import com.annimon.stream.operator.IntArray



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
        binding.listView.layoutManager =
            LinearLayoutManager(binding.root.context, LinearLayoutManager.VERTICAL, false)
        binding.listView.addItemDecoration(DividerItemDecoration(binding.listView.context, DividerItemDecoration.VERTICAL))
        binding.toolbar.inflateMenu(R.menu.assets_nav_menu)
        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.navigation_logout -> {
                    EventBus.getDefault().post(LogoutMessage(Date()))
                    return@setOnMenuItemClickListener true
                }
                R.id.navigation_assets_all -> {
                    if (control.assetsAll) {
                        it.setTitle(R.string.assets_no_zero)
                    } else {
                        it.setTitle(R.string.assets_all)
                    }
                    control.assetsAll = !control.assetsAll
                    control.autoRefresh()
                    return@setOnMenuItemClickListener true
                }

            }
            return@setOnMenuItemClickListener false
        }
        initData()
        return binding.root
    }


    override fun initData() {
        control.autoRefresh()
    }
}