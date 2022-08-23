package com.duqian.app.live.controller

import android.view.View
import android.widget.ImageView
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.base_comm.LiveEventKey
import com.duqian.app.live.fragment.dialog.RoomGiftDialog
import com.duqian.app.live.services.AutoServiceHelper
import com.duqian.app.live.utils.FragmentUtils
import com.duqian.app.live.utils.LiveEventBus
import com.duqian.live.mediaplayer.ILivePlayerService

/**
 * Description:直播间底部，礼物按钮的处理逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class RoomGiftBtnController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    override fun initView(rootView: View) {
        rootView.findViewById<ImageView>(R.id.ivRoomGift).setOnClickListener {

            val dialogFragment = RoomGiftDialog.newInstance()
            dialogFragment.callback = object : RoomGiftDialog.DialogCallback {
                override fun onClicked() {
                    testGiftEffect()
                }
            }
            FragmentUtils.show(mActivity, dialogFragment)
        }
    }

    private fun testGiftEffect() {
        LiveEventBus.get().with(LiveEventKey.EVENT_KEY_SEND_GIFT_START, Boolean::class.java)
            .postValue(true)
        FragmentUtils.dismissAllowingStateLoss(
            mActivity.supportFragmentManager,
            RoomGiftDialog::class.java.simpleName
        )
    }

    override fun onResume() {
    }

    override fun onDestroy() {
        val playerService = AutoServiceHelper.load(ILivePlayerService::class.java)
        playerService?.stopPlay()
    }
}