package com.duqian.app.live.utils

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.os.Handler
import android.os.Looper
import android.os.Message

/**
 * Description:主线程UI工具类
 * Created by 杜乾 on 2022/8/10 - 15:23.
 * E-mail: duqian2010@gmail.com
 */
object UIUtil {
    private var sMainHandler: Handler? = Handler(Looper.getMainLooper())

    val isOnUiThread: Boolean
        get() = Looper.myLooper() == Looper.getMainLooper()

    fun runOnUiThread(runnable: Runnable?, token: Any? = null) {
        runOnUiThreadDelay(runnable, 0, token)
    }

    fun runOnUiThread(runnable: Runnable?, delayMillis: Long) {
        runOnUiThreadDelay(runnable, delayMillis, null)
    }

    private fun runOnUiThreadDelay(runnable: Runnable?, delayMillis: Long, token: Any?) {
        if (sMainHandler == null) {
            sMainHandler = Handler(Looper.getMainLooper())
        }
        val message = Message.obtain(sMainHandler, runnable)
        if (token != null) {
            message.what = token.hashCode()
        }
        sMainHandler?.sendMessageDelayed(message, delayMillis)
    }

    fun runOnUiThread(action: Runnable) {
        if (isOnUiThread) {
            action.run()
        } else {
            sMainHandler?.post(action)
        }
    }

    fun runOnUiThreadDelay(action: Runnable, delay: Long) {
        sMainHandler?.postDelayed(action, delay)
    }

    fun runOnUiThreadAtFront(action: Runnable) {
        sMainHandler?.postAtFrontOfQueue(action)
    }

    fun removeCallbacks(action: Runnable) {
        sMainHandler?.removeCallbacks(action)
    }

    @JvmStatic
    fun isActivityIllegal(context: Context?): Boolean {
        return context !is Activity || context.isFinishing || context.isDestroyed
    }

    fun isLandscape(context: Context): Boolean {
        var isLand = false
        try {
            val mConfiguration = context.resources.configuration //获取设置的配置信息
            val ori = mConfiguration.orientation //获取屏幕方向
            isLand = ori == Configuration.ORIENTATION_LANDSCAPE
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return isLand
    }
}