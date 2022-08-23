package com.duqian.app.live.pushroom.ui

import android.os.Bundle
import com.duqian.app.live.R
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.live.utils.setViewVisible
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.ParamKey
import com.duqian.app.live.pushroom.data.PushRoomEvent
import com.duqian.app.live.pushroom.data.PushRoomEventCode
import kotlinx.android.synthetic.main.fragment_pushroom_preview.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.io.Serializable

/**
 * Description:开播预览界面
 * Created by 杜乾 on 2022/8/10 - 18:10.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
class LivePreviewFragment : BaseLiveFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_pushroom_preview
    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun initListener() {
        tvStartLive.setOnClickListener {
            //todo-dq 请求接口，成功后，隐藏当前按钮，切换显示直播间主体内容
            tvStartLive.setViewVisible(false)
            EventBusHelper.sendEvent(PushRoomEvent(PushRoomEventCode.EVENT_ID_PUSH_ROOM_START_LIVE))
        }
    }

    override fun observerData() {
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: PushRoomEvent) {
        when (event.eventCode) {
            //PushRoomEventCode.EVENT_ID_PUSH_ROOM_START_LIVE -> { }
        }
    }

    companion object {
        private const val TAG = "LivePreviewFragment"

        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: Serializable? = null): LivePreviewFragment {
            val fragment = LivePreviewFragment()
            val bundle = Bundle()
            if (data != null) {
                bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}