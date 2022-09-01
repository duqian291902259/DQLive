package com.duqian.app.live.fragment

import android.os.Bundle
import com.duqian.app.live.R
import com.duqian.app.live.utils.setViewVisible
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.BaseRoomParams
import com.duqian.app.live.base_comm.ParamKey
import kotlinx.android.synthetic.main.fragment_room_public_chat.*

/**
 * Description:直播间 公屏fragment
 *
 * Created by 杜乾 on 2022/8/11 - 19:20.
 * E-mail: duqian2010@gmail.com
 */
class PublicChatFragment : BaseLiveFragment() {

    //controller或者子fragment实现

    override fun getLayoutId(): Int {
        return R.layout.fragment_room_public_chat
    }

    override fun initControllers() {

    }

    override fun initData() {

    }

    override fun initView() {

    }

    override fun observerData() {
        super.observerData()
        mGlobalViewModel?.isLandscape?.observe(this) {
            rootPublicChat.setViewVisible(!it)
        }
    }

    companion object {
        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: BaseRoomParams? = null): PublicChatFragment {
            val fragment = PublicChatFragment()
            val bundle = Bundle()
            bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}