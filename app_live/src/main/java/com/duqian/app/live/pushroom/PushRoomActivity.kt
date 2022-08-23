package com.duqian.app.live.pushroom

import android.app.Activity
import android.content.Context
import android.content.Intent
import com.duqian.app.live.R
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.live.fragment.RoomMainFragment
import com.duqian.app.live.pushroom.controller.PushCameraController
import com.duqian.app.live.pushroom.data.PushRoomEvent
import com.duqian.app.live.pushroom.data.PushRoomEventCode
import com.duqian.app.live.pushroom.data.PushRoomParams
import com.duqian.app.live.pushroom.ui.LivePreviewFragment
import com.duqian.app.live.utils.FragmentUtils
import com.duqian.app.live.base_comm.BaseLiveActivity
import com.duqian.app.live.base_comm.ParamKey
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import com.duqian.app.live.pushroom.controller.AgoraPusherController
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Description:主播端，push直播间
 * Created by 杜乾 on 2022/8/10 - 14:05.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
class PushRoomActivity : BaseLiveActivity() {

    //CameraX预览
    private var mPushCameraController: PushCameraController? = null

    //声网推流
    private var mAgoraPusherController: AgoraPusherController? = null

    override fun getLayoutId(): Int {
        return R.layout.activity_live_push_room
    }

    override fun initView() {
        super.initView()
        initFragments()
    }

    override fun initControllers() {
        //mPushCameraController = PushCameraController(mRootView, this)
        mAgoraPusherController = AgoraPusherController(mRootView, this)
    }

    private fun initFragments() {
        FragmentUtils.addFragment(
            supportFragmentManager, R.id.rootRoomContainer,
            LivePreviewFragment.newInstance()
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {
            LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END -> {
                // TODO-dq: 处理下播逻辑
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: PushRoomEvent) {
        when (event.eventCode) {
            PushRoomEventCode.EVENT_ID_PUSH_ROOM_START_LIVE -> {
                FragmentUtils.addFragment(
                    supportFragmentManager, R.id.rootRoomContainer,
                    RoomMainFragment.newInstance(mRoomParams)
                )
            }
        }
    }

    companion object {
        fun startActivity(context: Context?, roomParams: PushRoomParams) {
            val intent = Intent(context, PushRoomActivity::class.java)
            roomParams.isPushRoom = true
            intent.putExtra(ParamKey.PARAM_KEY_ROOM_DATA, roomParams)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(intent)
        }
    }
}