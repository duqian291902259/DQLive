package com.duqian.app.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import com.duqian.app.helper.EventBusHelper
import com.gyf.immersionbar.ktx.immersionBar
import com.duqian.app.live.utils.LiveEventBus
import io.github.inflationx.viewpump.ViewPumpContextWrapper

/**
 * Description:Activity 基类
 * Created by 杜小菜 on 2022/8/9 - 19:42.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseActivity : AppCompatActivity() {

    val mHandler = Handler(Looper.getMainLooper())
    lateinit var mRootView: View
    override fun onCreate(savedInstanceState: Bundle?) {
        handleUnmarshalling(savedInstanceState)
        super.onCreate(savedInstanceState)
        val root = getRootView()
        when {
            root != null -> {
                this.mRootView = root
            }
            else -> {
                val layoutId = getLayoutId()
                this.mRootView = LayoutInflater.from(this).inflate(layoutId, null)
            }
        }
        setContentView(mRootView)
        EventBusHelper.bind(this)
        initConfig()
        initData()
        initView()
        initListener()
        observerData()
    }

    override fun finish() {
        super.finish()
        EventBusHelper.unbind(this)
        mHandler.removeCallbacksAndMessages(null)
        //确保去除粘性事件
        LiveEventBus.get().clear()
    }

    override fun dispatchTouchEvent(ev: MotionEvent?): Boolean {
        return super.dispatchTouchEvent(ev)
    }

    private fun handleUnmarshalling(savedInstanceState: Bundle?) {
        try {
            savedInstanceState?.classLoader = this.javaClass.classLoader
        } catch (t: Throwable) {
            t.printStackTrace()
        }
    }

    open fun initConfig() {
        immersionBar {
            transparentStatusBar()
            statusBarDarkFont(isStatusBarDark())
        }
    }

    open fun isStatusBarDark(): Boolean {
        return true
    }

    abstract fun getLayoutId(): Int

    abstract fun initData()

    open fun initView() {}

    open fun initListener() {}

    open fun observerData() {}

    open fun getRootView(): View? {
        return null
    }

    override fun attachBaseContext(newBase: Context?) {
        if (newBase != null) {
            super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase))
        } else {
            super.attachBaseContext(newBase)
        }
    }

}