package com.duqian.app.helper

import android.app.Application.ActivityLifecycleCallbacks
import android.app.Activity
import android.app.Application
import android.os.Bundle
import android.util.Log
import com.duqian.app.live.BuildConfig
import com.duqian.app.base.BaseApplication
import com.duqian.app.live.utils.dpInt
import me.ele.uetool.UETool

/**
 * Created by 杜小菜 on 2022/7/21 - 20:07.
 * E-mail: duqian2010@gmail.com
 * Description:初始化UETool，设置监听，给了个设置的开关
 */
object UEToolHelper {
    fun initUeTool(application: Application) {
        //仅供debug包使用，egg调试页面，也给了设置的开关
        if (!BuildConfig.DEBUG) return
        //UETool.putFilterClass(FilterOutView.class);
        //UETool.putAttrsProviderClass(CustomAttribution.class);
        Log.d("dq-ue", "initUeTool")
        application.registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            private var visibleActivityCount = 0
            private var UEToolDismissY = 80.dpInt //不要挡住返回按钮

            override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}

            override fun onActivityStarted(activity: Activity) {
                visibleActivityCount++
                val enable: Boolean =
                    BaseApplication.instance.mmkv?.decodeBool("UEToolEnable", false) ?: false
                if (visibleActivityCount == 1 && enable) {
                    val showUETMenu = UETool.showUETMenu(UEToolDismissY)
                    Log.d("dq-ue", "showUETMenu $showUETMenu")
                }
            }

            override fun onActivityResumed(activity: Activity) {}
            override fun onActivityPaused(activity: Activity) {}
            override fun onActivityStopped(activity: Activity) {
                visibleActivityCount--
                if (visibleActivityCount == 0) {
                    UEToolDismissY = UETool.dismissUETMenu()
                }
            }

            override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
            override fun onActivityDestroyed(activity: Activity) {}
        })
    }
}