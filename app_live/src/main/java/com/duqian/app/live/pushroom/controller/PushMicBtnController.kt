package com.duqian.app.live.pushroom.controller

import android.Manifest
import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.pushroom.data.PushRoomEvent
import com.duqian.app.live.pushroom.data.PushRoomEventCode

/**
 * Description:开播端，底部，禁音 按钮的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class PushMicBtnController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    private lateinit var ivMicBtn: ImageView
    private var isMicOn = true
    override fun initView(rootView: View) {
        ivMicBtn = rootView.findViewById(R.id.ivRoomMicVoice)
        ivMicBtn.setOnClickListener {
            if (hasMicPermission()) {
                isMicOn = !isMicOn
                updateMicIcon()

                EventBusHelper.sendEvent(
                    PushRoomEvent(
                        PushRoomEventCode.EVENT_PUSH_MIC_BTN_CLICKED,
                        isMicOn
                    )
                )
            } else {
                ToastUtils.show("No audio permission")
            }
        }
    }

    private fun hasMicPermission(): Boolean {
        return true //todo-dq  权限检测不正常，
        //return CommonUtils.checkSelfPermission(mActivity, Manifest.permission.RECORD_AUDIO)
    }

    private fun updateMicIcon() {
        if (isMicOn) {
            ivMicBtn.setImageResource(R.mipmap.ic_live_pushroom_btn_mic_on)
        } else {
            ivMicBtn.setImageResource(R.mipmap.ic_live_pushroom_btn_mic_off)
        }
    }

    override fun onResume() {
    }

    override fun onDestroy() {
    }
}