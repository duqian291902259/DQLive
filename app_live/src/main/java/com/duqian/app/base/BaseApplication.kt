package com.duqian.app.base

import android.app.Application
import android.util.Log
import com.duqian.app.helper.UEToolHelper
import com.tencent.mmkv.MMKV
import dagger.hilt.android.HiltAndroidApp
import io.github.inflationx.calligraphy3.CalligraphyConfig
import io.github.inflationx.calligraphy3.CalligraphyInterceptor
import io.github.inflationx.viewpump.ViewPump

/**
 * Description:Application基类
 * Created by 杜小菜 on 2022/8/9 - 18:22.
 * E-mail: duqian2010@gmail.com
 */
@HiltAndroidApp
class BaseApplication : Application() {

    companion object {
        lateinit var instance: BaseApplication
    }

    var mmkv: MMKV? = null

    override fun onCreate() {
        super.onCreate()
        instance = this

        //Hilt
        //mmkv
        val rootDir = MMKV.initialize(this)
        mmkv = MMKV.defaultMMKV()
        Log.d("dq-app", "mmkv root: $rootDir.mmkv=$mmkv")

        //自定义字体
        initFonts()

        //dev工具，debug包使用,release包是空实现
        UEToolHelper.initUeTool(this)
    }

    private fun initFonts() {
        ViewPump.init(
            ViewPump.builder()
                .addInterceptor(
                    CalligraphyInterceptor(
                        CalligraphyConfig.Builder()
                            .setDefaultFontPath("fonts/font_normal.ttf")
                            //.setFontAttrId(R.attr.fontPath)
                            .build()
                    )
                ).build()
        )
    }
}