package com.duqian.app.live.liveroom

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.util.Log
import com.duqian.app.live.R
import com.duqian.app.helper.RegisterEvent
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.base_comm.*
import com.duqian.app.live.fragment.RoomMainFragment
import com.duqian.app.live.fragment.RoomTestFragment
import com.duqian.app.live.liveroom.controller.RoomLivePlayerController
import com.duqian.app.live.liveroom.data.LiveRoomParams
import com.duqian.app.live.services.IRoomApi
import com.duqian.app.live.utils.FragmentUtils
import com.duqian.app.live.utils.LiveEventBus
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

/**
 * Description:直播间Activity
 * 目前没有支持上下滑动，后面直接加入一个触摸事件处理，不使用ViewPager，可以不改动已有的框架
 * Created by 杜乾 on 2022/8/10 - 14:05.
 * E-mail: duqian2010@gmail.com
 */
@RegisterEvent
@AndroidEntryPoint
class LiveRoomActivity : BaseLiveActivity() {

    private var mRoomLivePlayerController: RoomLivePlayerController? = null

    @Inject
    lateinit var mRoomApi: IRoomApi

    override fun getLayoutId(): Int {
        return R.layout.activity_live_room
    }

    override fun initData() {
        super.initData()
        val roomInfo = mRoomApi.getRoomInfo() ?: ""
        ToastUtils.show(roomInfo)
    }

    override fun initView() {
        super.initView()
        //主fragment,放在roomType改变的时候处理
    }

    override fun observerData() {
        super.observerData()
        mGlobalViewModel.roomType.observe(this) {
            initRoomFragmentByRoomType()
        }

        // TODO-dq: remove 清屏
        LiveEventBus.get().with(LiveEventKey.EVENT_KEY_SEND_GIFT_START, Boolean::class.java)
            .observe(this) {
                if (it)
                    FragmentUtils.removeFragmentByTag(
                        supportFragmentManager,
                        RoomMainFragment::class.java.simpleName
                    )
                Log.d(TAG, "dq EVENT_KEY_CLEAR_LIVE_SCREEN $it")
            }
    }

    override fun initControllers() {
        super.initControllers()
        mRoomLivePlayerController = RoomLivePlayerController(mRootView, this)
    }

    /**
     * 初始化房间类型，方便拓展/修改主体内容，根据直播间类型的初始化
     */
    private fun initRoomFragmentByRoomType() {
        var mainFragment: BaseLiveFragment = RoomMainFragment.newInstance(mRoomParams)
        when (mGlobalViewModel.roomType.value) {
            RoomType.ROOM_TYPE_SINGLE -> {
                mainFragment = RoomMainFragment.newInstance(mRoomParams)
            }
            RoomType.ROOM_TYPE_DOUBLE -> {//todo-dq test remove
                mainFragment = RoomTestFragment.newInstance(mRoomParams)
            }
        }
        FragmentUtils.addFragment(
            supportFragmentManager, R.id.rootRoomContainer, mainFragment
        )
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRoomEvent(event: LiveRoomEvent) {
        when (event.eventCode) {
            LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END -> {
                ToastUtils.show("Live End")
            }
        }
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        val isLandscape = newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE
        mGlobalViewModel.isLandscape.value = isLandscape
        Log.d(TAG, "dq-isLandscape=$isLandscape")
    }

    override fun onDestroy() {
        super.onDestroy()
        //todo-dq 销毁资源
    }

    companion object {
        private const val TAG = "LiveRoomActivity"

        fun startActivity(context: Context?, roomParams: LiveRoomParams) {
            val intent = Intent(context, LiveRoomActivity::class.java)
            roomParams.isPushRoom = false
            intent.putExtra(ParamKey.PARAM_KEY_ROOM_DATA, roomParams)
            if (context !is Activity) {
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context?.startActivity(intent)
        }
    }
}