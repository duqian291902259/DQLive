package com.duqian.app.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.live.utils.LiveEventBus

/**
 * Description:Fragment基类
 * Created by 杜乾 on 2022/8/10 - 15:00.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseFragment : Fragment() {

    companion object {
        private val TAG = "BaseFragment"
    }

    lateinit var rootView: View
    var isVisibleToUser: Boolean = false
    var mHandler = Handler(Looper.getMainLooper())
    open lateinit var mContext: Context
    open lateinit var mActivity: FragmentActivity

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    open fun parseParams() {
        //解析参数
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mContext = requireContext()
        mActivity = requireActivity()
        EventBusHelper.bind(this)
        initData()
        initView()
        initListener()
        observerData()
        initControllers()
    }

    /**
     * 初始化宿主fragment的所有逻辑之后，开始初始化子模块,非必须实现
     */
    open fun initControllers() {}


    override fun onResume() {
        super.onResume()
        // onResume并不代表fragment可见
        if (userVisibleHint && !isHidden) {
            onVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        if (userVisibleHint && !isHidden) {
            onInvisible()
        }
    }


    override fun onDestroyView() {
        super.onDestroyView()
        EventBusHelper.unbind(this)
        mHandler.removeCallbacksAndMessages(null)
        //确保去除粘性事件
        LiveEventBus.get().clear()
    }

    abstract fun getLayoutId(): Int

    abstract fun initData()

    abstract fun initView()

    open fun initListener() {}

    open fun observerData() {}

    /**
     * 当fragment与viewpager、FragmentPagerAdapter一起使用时，切换页面时会调用此方法
     *
     * @param isVisibleToUser 是否对用户可见
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        val change = isVisibleToUser != userVisibleHint
        super.setUserVisibleHint(isVisibleToUser)
        if (isResumed && change) {
            if (userVisibleHint) {
                onVisible()
            } else {
                onInvisible()
            }
        }
    }

    /**
     * 当使用show/hide方法时，会触发此回调
     *
     * @param hidden fragment是否被隐藏
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onInvisible()
        } else {
            onVisible()
        }
    }

    @CallSuper
    open fun onVisible() {
        isVisibleToUser = true

    }

    @CallSuper
    open fun onInvisible() {
        isVisibleToUser = false
    }
}