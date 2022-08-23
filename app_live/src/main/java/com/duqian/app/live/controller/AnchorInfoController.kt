package com.duqian.app.live.controller

import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.utils.setViewVisible
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.base_comm.LiveEventKey
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import com.duqian.app.live.utils.LiveEventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Description:主播头像，昵称，观众人数，follow等逻辑，目前直播间/开播端目前公用
 *
 * Created by 杜乾 on 2022/8/11 - 13:38.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
class AnchorInfoController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    companion object {
        private const val TAG = "AnchorInfoController"
    }

    //获取view的方式，也可以搞成dataBinding,参看RoomCommonController
    override fun initView(rootView: View) {
        val tvViewerCount = rootView.findViewById<TextView>(R.id.tvOnlineNum)
        //房间人数更新监听
        LiveEventBus.get().with(LiveEventKey.EVENT_KEY_VIEWER_COUNT, Int::class.java)
            .observe(mLifecycleOwner, true) {
                tvViewerCount.text = "$it"
                Log.d(TAG, "dq onCreate count = $it，activity=$mActivity fragment=$mFragment")
            }

        val followIcon = rootView.findViewById<ImageView>(R.id.ivAnchorFollow)
        followIcon.setViewVisible(!isPushRoom())
        followIcon.setOnClickListener {
            ToastUtils.show("Followed")
            followIcon.setViewVisible(false)
        }
    }

    override fun initData() {
        super.initData()
    }

    override fun onResume() {
        Log.d(TAG, "dq onResume")
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {
            LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END -> {
            }
        }
    }


    override fun onDestroy() {
        Log.d(TAG, "dq onDestroy")
    }
}