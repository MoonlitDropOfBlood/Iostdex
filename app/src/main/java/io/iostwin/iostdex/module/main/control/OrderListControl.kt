package io.iostwin.iostdex.module.main.control

import android.view.View
import androidx.annotation.LayoutRes
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.common.ListAdapter
import io.iostwin.iostdex.domain.*
import io.iostwin.iostdex.netwrok.HttpCallBack
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call

abstract class OrderListControl<T> {
    protected val data = arrayListOf<T>()
    @Suppress("LeakingThis")
    val adapter = ListAdapter(data, getLayoutRes(), BR.viewModel)
    var symbol = ""
    protected var page = 1
    @LayoutRes
    abstract fun getLayoutRes(): Int

    abstract fun OnItemClick(view: View, position: Int)

    protected val fail = fun(_: Throwable) {
        EventBus.getDefault().post(OrderFinishMessage(success = false, isEnd = true))
    }

    @Subscribe
    fun onOrderMessage(message: OrderHttpMessage) {
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
        if (symbol == "") {
            data.clear()
            adapter.notifyDataSetChanged()
        }
    }

    abstract fun sendHttp(): Call<PageResp<T>>

    private fun success(response: PageResp<T>) {
        if (response.maxPage >= this.page) {
            EventBus.getDefault().post(OrderFinishMessage(success = true, isEnd = false))
        } else {
            EventBus.getDefault().post(OrderFinishMessage(success = true, isEnd = true))
        }
        if (this.page == 1) {
            data.clear()
        }
        data.addAll(response.list)
        adapter.notifyDataSetChanged()
    }
}