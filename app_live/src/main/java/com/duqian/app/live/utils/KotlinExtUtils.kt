package com.duqian.app.live.utils

import android.content.res.Resources
import android.view.View

/**
 * Description:description:Kotlin扩展系统函数
 * Created by 杜小菜 on 2022/8/1 - 07:59.
 * E-mail: duqian2010@gmail.com
 */
// View Extensions
var View.isVisible
    get() = visibility == View.VISIBLE
    set(value) {
        visibility = if (value) View.VISIBLE else View.GONE
    }

fun View.setViewVisible(isVisible: Boolean) {
    visibility = if (isVisible) View.VISIBLE else View.GONE
}


fun Float.dp2px(): Float {
    return (0.5f + this * Resources.getSystem().displayMetrics.density)
}

fun Int.dp2px(): Float {
    return this * 1.0f.dp2px()
}

val Float.dp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
    )

val Int.dp: Int
    get() = (this * 1.0f.dp).toInt()

val Int.dpInt: Int
    get() = this * 1f.dp.toInt()

val Int.dpFloat: Float
    get() = this * 1f.dp


val Float.sp: Float
    get() = android.util.TypedValue.applyDimension(
        android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
    )

val Int.sp: Float
    get() = this * 1.0f.sp
