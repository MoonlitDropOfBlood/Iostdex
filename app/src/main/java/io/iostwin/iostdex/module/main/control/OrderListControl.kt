package io.iostwin.iostdex.module.main.control

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.domain.*
import io.iostwin.iostdex.module.main.ui.adapters.OrderListAdapter
import io.iostwin.iostdex.netwrok.HttpCallBack
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call

abstract class OrderListControl<T> {
    protected val data = arrayListOf<T>()
    @Suppress("LeakingThis")
    val adapter = OrderListAdapter(data, getLayoutRes(), BR.viewModel)
    var symbol = ""
    protected var page = 1
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun itemClick(view: View, position: Int)

    private val fail = fun(_: Throwable) {
        EventBus.getDefault().post(OrderFinishMessage(isTop = page == 1,success = false, isEnd = true))
    }

    @Subscribe
    fun onHttpMessage(message: OrderHttpMessage) {
        if (message.isTop) {
            page = 1
        } else {
            page++
        }
        sendHttp().enqueue(HttpCallBack(this::success, fail))
    }

    @Subscribe
    open fun onOrderFiltrate(message: OrderFiltrateMessage) {
        symbol = message.symbol
        adapter.symbol.set(symbol)
        if (symbol == "") {
            data.clear()
            adapter.notifyDataSetChanged()
        }
    }

    abstract fun sendHttp(): Call<PageResp<T>>

    private fun success(response: PageResp<T>) {
        if (response.maxPage >= this.page) {
            EventBus.getDefault().post(OrderFinishMessage(isTop = page == 1,success = true, isEnd = false))
        } else {
            EventBus.getDefault().post(OrderFinishMessage(isTop = page == 1,success = true, isEnd = true))
        }
        if (this.page == 1) {
            data.clear()
        }
        data.addAll(response.list)
        adapter.notifyDataSetChanged()
    }
}