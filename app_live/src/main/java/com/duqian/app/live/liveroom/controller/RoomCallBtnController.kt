package com.duqian.app.live.liveroom.controller

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.base_comm.BaseController

/**
 * Description:直播间底部，打电话按钮的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class RoomCallBtnController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    companion object {
        private const val TAG = "RoomCallBtnController"
    }

    override fun initView(rootView: View) {
        rootView.findViewById<ImageView>(R.id.ivRoomCall).setOnClickListener {
            ToastUtils.show("打电话")
        }
    }

    override fun onResume() {
    }

    override fun onDestroy() {
    }
}