package com.duqian.app.live.fragment

import android.os.Bundle
import com.duqian.app.live.R
import com.duqian.app.live.liveroom.controller.RoomCallBtnController
import com.duqian.app.live.liveroom.controller.RoomCoinsBtnController
import com.duqian.app.live.pushroom.controller.PushMicBtnController
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.BaseRoomParams
import com.duqian.app.live.base_comm.ParamKey
import com.duqian.app.live.controller.RoomChatBtnController
import com.duqian.app.live.controller.RoomGiftBtnController

/**
 * Description:直播间底部按钮fragment
 *
 * Created by 杜乾 on 2022/8/11 - 19:20.
 * E-mail: duqian2010@gmail.com
 */
class RoomBottomBtnFragment : BaseLiveFragment() {

    //聊天按钮对应的逻辑
    private lateinit var mChatBtnController: RoomChatBtnController
    //送礼
    private lateinit var mGiftBtnController: RoomGiftBtnController
    //充值
    private lateinit var mCoinsBtnController: RoomCoinsBtnController
    //打电话
    private lateinit var mCallBtnController: RoomCallBtnController
    //麦克风
    private lateinit var mMicVoiceBtnController: PushMicBtnController

    override fun getLayoutId(): Int {
        if (isPushRoom()) {
            return R.layout.fragment_pushroom_bottom_btns
        }
        return R.layout.fragment_liveroom_bottom_btns
    }

    override fun initControllers() {
        //通用的按钮
        mChatBtnController = RoomChatBtnController(rootView, this)
        mGiftBtnController = RoomGiftBtnController(rootView, this)

        if (isPushRoom()) {
            //主播端初始化底部按钮
            mMicVoiceBtnController = PushMicBtnController(rootView.findViewById(R.id.ivRoomMicVoice), this)
        } else {
            //用户端才需要初始化的按钮逻辑
            mCoinsBtnController = RoomCoinsBtnController(rootView, this)
            mCallBtnController = RoomCallBtnController(rootView, this)
        }
    }

    override fun initData() {

    }

    override fun initView() {

    }

    companion object {
        private const val TAG = "LiveRoomBottomFragment"

        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: BaseRoomParams? = null): RoomBottomBtnFragment {
            val fragment = RoomBottomBtnFragment()
            val bundle = Bundle()
            bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}