package com.duqian.app.helper

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

/**
 * Description:通用工具方法
 *
 * Created by 杜乾 on 2022/8/12 - 14:10.
 * E-mail: duqian2010@gmail.com
 */
object CommonUtils {

    /**
     * 检查是否有某个权限
     */
    fun checkSelfPermission(ctx: Context, vararg permission: String): Boolean {
        var isGranted = false
        for (per in permission) {
            isGranted = isGranted && (ContextCompat.checkSelfPermission(ctx.applicationContext, per)
                    == PackageManager.PERMISSION_GRANTED)
        }
        return isGranted
    }

    /**
     * 动态申请多个权限
     */
    fun requestPermissions(activity: Activity?, permissions: Array<String>, code: Int) {
        ActivityCompat.requestPermissions(activity!!, permissions, code)
    }
}