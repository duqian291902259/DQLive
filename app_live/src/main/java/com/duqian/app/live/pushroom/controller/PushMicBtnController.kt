package com.duqian.app.live.pushroom.controller

import android.view.View
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.base_comm.BaseController

/**
 * Description:开播端，底部，禁音 按钮的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class PushMicBtnController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    override fun initView(rootView: View) {
        //val ivRoomMicVoice = rootView.findViewById<ImageView>(R.id.ivRoomMicVoice)
        rootView.setOnClickListener {
            ToastUtils.show("关闭麦克风")
        }
    }

    override fun onResume() {
    }

    override fun onDestroy() {
    }
}