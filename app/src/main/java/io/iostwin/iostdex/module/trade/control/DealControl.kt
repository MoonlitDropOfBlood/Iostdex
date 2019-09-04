package io.iostwin.iostdex.module.trade.control

import android.app.AlertDialog
import android.os.Handler
import android.view.LayoutInflater
import android.widget.AdapterView
import androidx.databinding.DataBindingUtil
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.R
import io.iostwin.iostdex.common.ListAdapter
import io.iostwin.iostdex.databinding.DialogDealBinding
import io.iostwin.iostdex.domain.History
import io.iostwin.iostdex.domain.HistoryMessage
import io.iostwin.iostdex.domain.TradeDetailResp
import io.iostwin.iostdex.netwrok.ApiService
import io.iostwin.iostdex.netwrok.HttpCallBack
import io.iostwin.iostdex.netwrok.NetConfig
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

class DealControl(val tradeSymbol: String, val symbol: String) {
    val mainSymbol = "IOST"
    val data = arrayListOf<History>()
    val adapter = ListAdapter(data, R.layout.item_deal, BR.viewModel)
    private val handler = Handler()
    private var dialog: AlertDialog? = null
    private lateinit var dialogControl: DealDialogControl

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onHistoryMessage(message: HistoryMessage) {
        data.addAll(0, message.data)
        handler.post {
            adapter.notifyDataSetChanged()
        }
    }

    fun onItemClick(parent: AdapterView<*>, position: Int) {
        if (dialog == null) {
            val binding = DataBindingUtil.inflate<DialogDealBinding>(
                LayoutInflater.from(parent.context),
                R.layout.dialog_deal,
                null,
                false
            )
            dialog = AlertDialog.Builder(parent.context).setView(binding.root).create()
            dialogControl = DealDialogControl(tradeSymbol, dialog!!)
            binding.control = dialogControl
        }
        val item = parent.getItemAtPosition(position) as History
        val call = NetConfig.getService(ApiService::class.java).tradeDetail(symbol, item.id)
        call.enqueue(HttpCallBack(this::success))
    }

    private fun success(response: TradeDetailResp) {
        dialogControl.detail.set(response)
        dialog!!.show()
    }
}