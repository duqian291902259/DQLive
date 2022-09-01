package com.duqian.app.live.base_comm

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.*
import com.duqian.app.base.BaseActivity
import com.duqian.app.base.BaseFragment
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.live.utils.UIUtil
import com.duqian.app.live.viewmodel.RoomGlobalViewModel

/**
 * Description:controller基类,生命周期感知
 * Created by 杜乾 on 2022/8/11 - 11:15.
 * E-mail: duqian2010@gmail.com
 */
//如果要监听event消息，请在实现类中添加注解： @RegisterEvent
abstract class BaseController(val mRootView: View, val mLifecycleOwner: LifecycleOwner) :
    DefaultLifecycleObserver {

    var mHandler = Handler(Looper.getMainLooper())
    lateinit var mActivity: FragmentActivity //这个不为空
    lateinit var mContext: Context //这个不为空
    var mGlobalViewModel: RoomGlobalViewModel? = null

    //注意初始化的时候传入了fragment，才会不为空
    var mFragment: BaseFragment? = null

    init {
        initBaseContext()
    }

    private fun initBaseContext() {
        //约束LifecycleOwner必须实现基类BaseFragment或则BaseActivity
        if (mLifecycleOwner is BaseFragment) {
            this.mFragment = mLifecycleOwner
            this.mActivity = mFragment!!.activity as BaseActivity
            mContext = mLifecycleOwner.requireContext()
        }
        if (mLifecycleOwner is BaseActivity) {
            this.mActivity = mLifecycleOwner
            mContext = mLifecycleOwner
        }
        val owner = mActivity as ViewModelStoreOwner
        mGlobalViewModel = ViewModelProvider(owner)[RoomGlobalViewModel::class.java]
        mGlobalViewModel?.isLandscape?.observe(mLifecycleOwner) {
            onConfigurationChanged(it)
        }
        register(mLifecycleOwner)
    }

    open fun onConfigurationChanged(isLandscape: Boolean) {

    }

    abstract fun initView(rootView: View)
    open fun onResume() {}
    open fun onStart() {}
    open fun onPause() {}
    open fun onStop() {}
    abstract fun onDestroy()

    fun isPushRoom(): Boolean {
        return mGlobalViewModel?.isPushRoom?.value ?: false
    }

    private fun register(owner: LifecycleOwner) {
        owner.lifecycle.addObserver(this)
    }

    override fun onCreate(owner: LifecycleOwner) {
        super.onCreate(owner)
        EventBusHelper.bind(this)
        initView(mRootView)
        observerData()
        initData()
    }

    open fun observerData() {}

    open fun initData() {}

    override fun onStart(owner: LifecycleOwner) {
        super.onStart(owner)
        onStart()
    }

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)
        onResume()
    }

    override fun onPause(owner: LifecycleOwner) {
        super.onPause(owner)
        onPause()
    }

    override fun onStop(owner: LifecycleOwner) {
        super.onStop(owner)
        onStop()
    }

    override fun onDestroy(owner: LifecycleOwner) {
        super.onDestroy(owner)
        EventBusHelper.unbind(this)
        onDestroy()
        mHandler.removeCallbacksAndMessages(null)
        mLifecycleOwner.lifecycle.removeObserver(this)
    }

    open fun runOnUiThread(action: Runnable) {
        if (UIUtil.isOnUiThread) {
            action.run()
        } else {
            mHandler.post(action)
        }
    }

    open fun runOnUiThreadDelay(action: Runnable, delay: Long) {
        mHandler.postDelayed(action, delay)
    }

    open fun runOnUiThreadAtFront(action: Runnable) {
        mHandler.postAtFrontOfQueue(action)
    }

    open fun removeCallbacks(action: Runnable) {
        mHandler.removeCallbacks(action)
    }
}