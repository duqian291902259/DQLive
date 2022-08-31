package com.duqian.app.live.pushroom.controller

import android.util.Log
import android.view.View
import android.widget.FrameLayout
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.base.BaseApplication
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import com.duqian.app.live.pushroom.data.PushRoomEvent
import com.duqian.app.live.pushroom.data.PushRoomEventCode
import com.duqian.app.live.services.AutoServiceHelper
import com.duqian.live.pusher.IBasePusher
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Description:声网sdk推流逻辑
 *
 * Created by 杜乾 on 2022/8/18 - 21:11.
 * E-mail: duqian2010@gmail.com
 */

@RegisterEvent
class AgoraPusherController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    private lateinit var previewView: FrameLayout
    private val mAgoraPush: IBasePusher? = AutoServiceHelper.load(IBasePusher::class.java)

    override fun initView(rootView: View) {
        previewView = rootView.findViewById(R.id.agoraContainer)
    }

    override fun initData() {
        super.initData()
        //todo-dq 接口获取token

        mAgoraPush?.init(BaseApplication.instance.baseContext, previewView)
        //禁止声音
        mAgoraPush?.enableAudioVideo(false, false)
        mAgoraPush?.muteLocalAudioStream(true)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {
            LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END -> {
                //重复调用，会失败 ERR_LEAVE_CHANNEL_REJECTED(18)：离开频道失败
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onPushRoomEvent(event: PushRoomEvent) {
        when (event.eventCode) {
            PushRoomEventCode.EVENT_ID_PUSH_ROOM_START_LIVE -> {
                mAgoraPush?.joinChannel()

                mAgoraPush?.startPushToCDN(true)
            }

            PushRoomEventCode.EVENT_PUSH_MIC_BTN_CLICKED -> {
                val isMicEnable = event.data as Boolean
                mAgoraPush?.enableAudioVideo(
                    false,
                    isMicEnable
                )
                Log.d(TAG, "isMicEnable=$isMicEnable")
                if (isMicEnable) {
                    mAgoraPush?.adjustRecordingSignalVolume(100)
                } else {
                    mAgoraPush?.adjustRecordingSignalVolume(0)
                }
            }
        }
    }

    override fun onResume() {
        // TODO: 声网的这个处理有问题，停止预览，无法恢复，暂时不处理
        val startPreview = mAgoraPush?.startPreview()
        Log.d(TAG, "startPreview=$startPreview")
        //mAgoraPush?.onLifecycleChanged(LifecycleEvent.ON_RESUME)
    }

    override fun onPause() {
        val stopPreview = mAgoraPush?.stopPreview()
        Log.d(TAG, "startPreview=$stopPreview")
        //mAgoraPush?.onLifecycleChanged(LifecycleEvent.ON_PAUSE)
    }

    override fun onDestroy() {
        mAgoraPush?.release()
    }

    companion object {
        private const val TAG = "dq-agora-pusher"
    }
}