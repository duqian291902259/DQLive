package com.duqian.app.live.controller

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Description:直播间底部，聊天按钮的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
class RoomChatBtnController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    override fun initView(rootView: View) {
        rootView.findViewById<ImageView>(R.id.ivRoomChat)?.setOnClickListener {
            ToastUtils.show("弹起面板，发送聊天消息，isPushRoom=${isPushRoom()}")
        }
    }

    override fun onResume() {
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {

        }
    }

    override fun onDestroy() {
    }
}