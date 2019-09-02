package io.iostwin.iostdex.module.trade.ui.adapters

import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.databinding.library.baseAdapters.BR
import io.iostwin.iostdex.common.ListAdapter
import java.math.BigDecimal

class OrderAdapter<T>(data: ArrayList<T>, itemLayout: Int, brId: Int) : ListAdapter<T>(
    data, itemLayout,
    brId
) {
    var max = BigDecimal.ONE

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = super.getView(position, convertView, parent)
        val viewBinding = DataBindingUtil.findBinding<ViewDataBinding>(view)
        viewBinding!!.setVariable(BR.max, max)
        return view
    }
}