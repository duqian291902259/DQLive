package com.duqian.app.live.controller

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.live.databinding.IncludeRoomCommonLayoutBinding
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.base_comm.LiveEventKey
import com.duqian.app.live.base_comm.RoomType
import com.duqian.app.live.utils.LiveEventBus
import com.duqian.app.live.utils.dp

/**
 * Description:直播间一些通用的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class RoomCommonController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    override fun initView(rootView: View) {
        val binding: IncludeRoomCommonLayoutBinding = DataBindingUtil.inflate(
            mActivity.layoutInflater,
            R.layout.include_room_common_layout,
            mRootView as ViewGroup,
            true
        )
//            rootView.findViewById(R.id.rootCommonContainer) as ViewGroup,
        //databinding层级最高的问题,父布局是Include的话，会直接加载直播间布局的最上层
        binding.tvRoomCommonTips.text = "点击模拟切换直播间"
        binding.tvRoomCommonTips.setOnClickListener {
            mGlobalViewModel?.roomType?.value = RoomType.ROOM_TYPE_DOUBLE
        }

        if (isPushRoom()) {
            binding.tvLive.text = "主播端"
            binding.tvLive.setTextColor(mContext.resources.getColor(R.color.white, null))
        } else {
            binding.tvLive.text = "直播间"
        }

        binding.tvLive.setOnClickListener {
            LiveEventBus.get().with(LiveEventKey.EVENT_KEY_VIEWER_COUNT, Int::class.java)
                .postValue((Math.random() * 1000).toInt() + 1999)
        }
    }

    override fun onConfigurationChanged(isLandscape: Boolean) {
        val tvLive = mRootView.findViewById<TextView>(R.id.tvLive)
        if (!isPushRoom()) {
            tvLive.text = if (isLandscape) "横屏直播间" else "直播间.."
        } else {
            tvLive.text = "主播端"
        }
        val layoutParams = tvLive.layoutParams as ConstraintLayout.LayoutParams
        layoutParams.topMargin = 100.dp
        tvLive.layoutParams = layoutParams
        tvLive.setOnClickListener {
            LiveEventBus.get().with(LiveEventKey.EVENT_KEY_VIEWER_COUNT, Int::class.java)
                .postValue((Math.random() * 1000).toInt() + 1999)
        }
    }

    override fun onResume() {

    }

    override fun onDestroy() {
    }
}