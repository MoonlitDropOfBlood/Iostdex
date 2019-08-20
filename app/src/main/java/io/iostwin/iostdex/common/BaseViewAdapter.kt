package io.iostwin.iostdex.common

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import io.iostwin.iostdex.R

class BaseViewAdapter<T>(
    private val mValues: List<T>,
    @LayoutRes private val layoutRes: Int,
    private val brId: Int,
    private val brIndex: Int = 0,
    private val mItemClickListener: ((item: T) -> Unit)? = null,
    private val mItemLongClickListener: ((item: T) -> Unit)? = null,
    private val viewHolder: ((viewHolder: ViewHolder) -> Unit)? = null
) : RecyclerView.Adapter<BaseViewAdapter.ViewHolder>() {

    private val mOnClickListener: View.OnClickListener
    private val mOnLongClickListener: View.OnLongClickListener

    init {
        mOnClickListener = View.OnClickListener { v ->
            @Suppress("UNCHECKED_CAST")
            mItemClickListener?.invoke(v.getTag(R.id.tag_more_key) as T)
        }
        mOnLongClickListener = View.OnLongClickListener { v ->
            if (mItemLongClickListener == null)
                return@OnLongClickListener false
            else {
                @Suppress("UNCHECKED_CAST")
                mItemLongClickListener.invoke(v.getTag(R.id.tag_more_key) as T)
                return@OnLongClickListener true
            }
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            DataBindingUtil.inflate<ViewDataBinding>(LayoutInflater.from(parent.context), layoutRes, parent, false)
        return ViewHolder(binding.root, viewHolder)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemView = DataBindingUtil.findBinding<ViewDataBinding>(holder.itemView)
        itemView?.setVariable(brId, mValues[position])
        if (brIndex != 0) {
            itemView?.setVariable(brIndex, position)
        }
        itemView?.root?.apply {
            setTag(R.id.tag_more_key, mValues[position])
            if (mItemClickListener != null)
                setOnClickListener(mOnClickListener)
            if (mItemLongClickListener != null)
                setOnLongClickListener(mOnLongClickListener)
        }
    }

    override fun getItemCount(): Int = mValues.size

    class ViewHolder(mView: View, viewHolder: ((viewHolder: ViewHolder) -> Unit)? = null) :
        RecyclerView.ViewHolder(mView) {
        init {
            viewHolder?.invoke(this)
        }
    }
}
