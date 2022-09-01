package com.duqian.app.live.base_comm

import android.text.TextUtils
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelStoreOwner
import androidx.lifecycle.observe
import com.duqian.app.base.BaseFragment
import com.duqian.app.live.utils.UIUtil
import com.duqian.app.live.viewmodel.RoomGlobalViewModel

/**
 * Description:房间专属的fragment基类，方便回调一些房间内的事件
 * 提供给后续扩展都有的功能逻辑
 * Created by 杜乾 on 2022/8/10 - 15:39.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseLiveFragment : BaseFragment() {
    var mRoomParams: BaseRoomParams? = null
    var mRoomId = 0
    var mGlobalViewModel: RoomGlobalViewModel? = null

    override fun initListener() {

    }

    override fun observerData() {
        val owner = activity as ViewModelStoreOwner
        mGlobalViewModel = ViewModelProvider(owner)[RoomGlobalViewModel::class.java]
        mGlobalViewModel?.isLandscape?.observe(this) {
            onConfigurationChanged(it)
        }
    }

    open fun onConfigurationChanged(isLandscape: Boolean) {

    }

    override fun parseParams() {
        mRoomParams = arguments?.getSerializable(ParamKey.PARAM_KEY_ROOM_DATA) as BaseRoomParams?
        mRoomId = mRoomParams?.roomId ?: 0
    }

    fun isPushRoom(): Boolean {
        return mRoomParams?.isPushRoom == true
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

    /**
     * 房间内只允许增加继承自BaseLiveFragment的fragment
     * 方便监听房间内的事件和通用处理逻辑
     */
    open fun addFragment(contentId: Int, fragment: BaseLiveFragment, tag: String? = "") {
        activity?.supportFragmentManager?.beginTransaction()
            ?.replace(contentId, fragment, tag)
            //.addToBackStack((fragment as Any).javaClass.simpleName)
            ?.disallowAddToBackStack()
            ?.commitAllowingStateLoss()
    }

    open fun removeFragment(fragment: Fragment) {
        activity?.supportFragmentManager?.beginTransaction()
            ?.remove(fragment)
            ?.commitAllowingStateLoss()
    }

    open fun removeFragmentByTag(fragmentTag: String? = "") {
        if (TextUtils.isEmpty(fragmentTag)) return
        val fragmentByTag =
            activity?.supportFragmentManager?.findFragmentByTag(fragmentTag) as Fragment
        removeFragment(fragmentByTag)
    }
}