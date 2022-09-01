package com.duqian.app.live.fragment

import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import com.duqian.app.live.R
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.live.controller.AnchorInfoController
import com.duqian.app.live.controller.RoomCommonController
import com.duqian.app.live.utils.setViewVisible
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.BaseRoomParams
import com.duqian.app.live.base_comm.ParamKey
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import kotlinx.android.synthetic.main.fragment_room_main.*
import kotlinx.android.synthetic.main.include_room_top_layout.*
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

/**
 * Description:观众端，直播间，主fragment
 * Created by 杜乾 on 2022/8/10 - 16:14.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
class RoomMainFragment : BaseLiveFragment() {

    //通用逻辑模块（不方便归类，或者无UI逻辑端，可以暂时放这里）
    private lateinit var mRoomCommonController: RoomCommonController

    //主播信息/follow模块
    private lateinit var mAnchorInfoController: AnchorInfoController

    override fun getLayoutId(): Int {
        return R.layout.fragment_room_main
    }

    override fun initControllers() {
        //通用模块
        mRoomCommonController = RoomCommonController(rootCommonContainer, this)
        //主播信息
        mAnchorInfoController = AnchorInfoController(rootLiveTopContainer, this)
        //公屏
        addFragment(R.id.rootPublicChatContainer, PublicChatFragment.newInstance(mRoomParams))
        //礼物特效
        addFragment(R.id.rootEffectContainer, RoomEffectFragment.newInstance(mRoomParams))
        //底部按钮
        addFragment(R.id.rootBottomContainer, RoomBottomBtnFragment.newInstance(mRoomParams))
    }

    override fun initData() {

    }

    override fun initView() {
        //rootRoomContainer.setViewVisible(false)
    }

    override fun observerData() {
        super.observerData()
        mGlobalViewModel?.isRoomLoading?.observe(this) {
            rootRoomContainer?.setViewVisible(!it)
            //Log.d(TAG,"dq isRoomLoading=$it")
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {
            LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END -> {
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)

        Log.d(TAG, "onConfigurationChanged=$")
    }

    companion object {
        private const val TAG = "LiveRoomFragment"

        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: BaseRoomParams? = null): RoomMainFragment {
            val fragment = RoomMainFragment()
            val bundle = Bundle()
            if (data != null) {
                bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
                //bundle.putParcelable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            }
            fragment.arguments = bundle
            return fragment
        }
    }
}