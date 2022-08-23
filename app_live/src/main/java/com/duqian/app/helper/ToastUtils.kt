package com.duqian.app.helper

import android.widget.Toast
import com.duqian.app.base.BaseApplication
import com.duqian.app.live.utils.UIUtil

/**
 * Description:简易的ToastUtils
 *
 * Created by 杜乾 on 2022/8/11 - 14:23.
 * E-mail: duqian2010@gmail.com
 */
object ToastUtils {
    fun show(content: String) {
        val context = BaseApplication.instance
        if (UIUtil.isOnUiThread) {
            Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
        } else {
            UIUtil.runOnUiThread({
                Toast.makeText(context, content, Toast.LENGTH_SHORT).show()
            })
        }
    }
}