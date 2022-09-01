package com.duqian.app.live.fragment

import android.os.Bundle
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.BaseRoomParams
import com.duqian.app.live.base_comm.ParamKey

/**
 * Description:Description:直播间 特效 fragment
 *
 * Created by 杜乾 on 2022/8/12 - 14:23.
 * E-mail: duqian2010@gmail.com
 */
class RoomEffectFragment : BaseLiveFragment() {

    override fun getLayoutId(): Int {
        return R.layout.fragment_room_gift_effect
    }

    override fun initData() {

    }

    override fun initView() {

    }

    companion object {
        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: BaseRoomParams? = null): RoomEffectFragment {
            val fragment = RoomEffectFragment()
            val bundle = Bundle()
            bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}