package io.iostwin.iostdex.module.main.control

import android.widget.ArrayAdapter
import androidx.core.view.GravityCompat
import androidx.databinding.ObservableBoolean
import androidx.databinding.ObservableField
import io.iostwin.iostdex.R
import io.iostwin.iostdex.databinding.FragmentOrderBinding
import io.iostwin.iostdex.domain.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList


class OrderControl(private val binding: FragmentOrderBinding) {
    val symbolAdapter =
        ArrayAdapter<TokenSymbolResp>(binding.root.context, android.R.layout.simple_spinner_item)
    val tab = ObservableBoolean(true)
    val startTime = ObservableField<String>()
    val endTime = ObservableField<String>()

    init {
        symbolAdapter.add(
            TokenSymbolResp(
                -1,
                binding.root.context.getString(R.string.please_select),
                "",
                null,
                null,
                ""
            )
        )
        binding.refreshLayout.setOnRefreshListener {
            EventBus.getDefault().post(OrderHttpMessage(true))
        }
        binding.refreshLayout.setOnLoadMoreListener {
            EventBus.getDefault().post(OrderHttpMessage(false))
        }
    }

    fun addSymbol(lists: ArrayList<TokenSymbolResp>) {
        symbolAdapter.addAll(lists)
    }

    fun reset() {
        binding.orderDirection.clearCheck()
        binding.orderPair.setSelection(0, false)
        binding.orderStatus.setSelection(0, false)
        startTime.set("")
        endTime.set("")
    }

    @Subscribe
    fun onOrderFinish(message: OrderFinishMessage) {
        binding.refreshLayout.finishRefresh(300, message.success, message.isEnd)
    }

    fun ok() {
        binding.drawerLayout.closeDrawer(GravityCompat.END)
        val symbol = (binding.orderPair.selectedItem as TokenSymbolResp).symbol
        if (tab.get()) {
            var startTime: Int? = null
            var endTime: Int? = null
            val format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            this.startTime.get()?.apply {
                if (this != "")
                    startTime = (format.parse(this)!!.time / 1000).toInt()
            }
            this.endTime.get()?.apply {
                if (this != "")
                    endTime = (format.parse(this)!!.time / 1000).toInt() + 86400
            }
            EventBus.getDefault().post(
                OrderFiltrateMessage(
                    symbol,
                    when (binding.orderDirection.checkedRadioButtonId) {
                        R.id.order_buy -> 1
                        R.id.order_sell -> 0
                        else -> null
                    },
                    binding.orderStatus.selectedItemPosition,
                    startTime,
                    endTime
                )
            )
        } else {
            EventBus.getDefault().post(OrderFiltrateMessage(symbol))
        }
        binding.refreshLayout.autoRefresh()
    }

    fun filtrate() {
        binding.drawerLayout.openDrawer(GravityCompat.END)
    }
}