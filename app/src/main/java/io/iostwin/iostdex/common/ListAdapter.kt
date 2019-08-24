package io.iostwin.iostdex.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import java.lang.Exception

open class ListAdapter<T>(private val data: List<T>, @LayoutRes private val itemLayout: Int, private val brId: Int, private val hasStableIds: Boolean = false, private val brIndex: Int = 0) : BaseAdapter() {
    open override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val viewBinding = run {
            if (convertView == null) {
                val li = LayoutInflater.from(parent!!.context)
                DataBindingUtil.inflate<ViewDataBinding>(li, itemLayout, parent, false)
            } else {
                DataBindingUtil.findBinding(convertView)
            }
        }


        try{
            //错误处理
            viewBinding.setVariable(brId, data[position])
            if (brIndex != 0) {
                viewBinding.setVariable(brIndex, position)
            }
        }catch (e:Exception){
        }

        return viewBinding.root
    }

    override fun getItem(position: Int): T = data[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getCount(): Int = data.size

    override fun hasStableIds(): Boolean = hasStableIds
}