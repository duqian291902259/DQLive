package com.duqian.app.live.fragment

import android.os.Bundle
import com.duqian.app.live.R
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.services.IRoomApi
import com.duqian.app.live.base_comm.BaseLiveFragment
import com.duqian.app.live.base_comm.BaseRoomParams
import com.duqian.app.live.base_comm.ParamKey
import com.duqian.app.live.base_comm.RoomType
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_room_test_layout.*
import javax.inject.Inject

/**
 * Description:测试直播间内容的fragment
 *
 * Created by 杜乾 on 2022/8/11 - 19:20.
 * E-mail: duqian2010@gmail.com
 */
@AndroidEntryPoint
class RoomTestFragment : BaseLiveFragment() {

    @Inject
    lateinit var mRoomApi: IRoomApi

    override fun getLayoutId(): Int {
        return R.layout.fragment_room_test_layout
    }

    override fun initData() {
        ToastUtils.show("mRoomApi: ${mRoomApi.getRoomInfo()}")
    }

    override fun initView() {
        tvRoomTestTips.setOnClickListener {
            mGlobalViewModel?.roomType?.value = RoomType.ROOM_TYPE_SINGLE
        }
    }

    override fun onConfigurationChanged(isLandscape: Boolean) {
        super.onConfigurationChanged(isLandscape)
        tvRoomTestTips.text = "isLandscape= $isLandscape"
    }

    override fun observerData() {
        super.observerData()
    }

    companion object {
        /**
         * 传入的参数，后续按需扩展，进入房间的参数放BaseRoomParams，其他额外的参数视情况添加
         */
        fun newInstance(data: BaseRoomParams? = null): RoomTestFragment {
            val fragment = RoomTestFragment()
            val bundle = Bundle()
            bundle.putSerializable(ParamKey.PARAM_KEY_ROOM_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}