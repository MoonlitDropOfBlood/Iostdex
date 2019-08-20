package io.iostwin.iostdex.common

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.Nullable
import androidx.fragment.app.Fragment


abstract class BaseFragment : Fragment() {

    //Fragment的View加载完毕的标记
    private var isViewCreated: Boolean = false
    //Fragment对用户可见的标记
    private var isUIVisible: Boolean = false

    @Nullable
    override fun onCreateView(inflater: LayoutInflater, @Nullable container: ViewGroup?, @Nullable savedInstanceState: Bundle?): View {
        return initView(inflater, container)
    }

    protected abstract fun initView(inflater: LayoutInflater, @Nullable container: ViewGroup?): View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        isViewCreated = true
        lazyLoad()
    }

    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        super.setUserVisibleHint(isVisibleToUser)
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
        if (isVisibleToUser) {
            isUIVisible = true
            lazyLoad()
        } else {
            isUIVisible = false
        }
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden && isViewCreated)
            initData()
    }

    private fun lazyLoad() {
        //这里进行双重标记判断,是因为setUserVisibleHint会多次回调,并且会在onCreateView执行前回调,必须确保onCreateView加载完毕且页面可见,才加载数据
        if (isViewCreated && isUIVisible) {
            //数据加载完毕,恢复标记,防止重复加载
            isUIVisible = false
            initData()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //页面销毁,恢复标记
        isViewCreated = false
        isUIVisible = false
    }

    /**
     * 执行数据的加载
     */
    abstract fun initData()
}
