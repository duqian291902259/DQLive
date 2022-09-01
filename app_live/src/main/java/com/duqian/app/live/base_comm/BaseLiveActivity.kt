package com.duqian.app.live.base_comm

import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.duqian.app.live.R
import com.duqian.app.base.BaseActivity
import com.duqian.app.live.liveroom.data.LiveRoomEvent
import com.duqian.app.live.liveroom.data.LiveRoomEventCode
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.live.utils.UIUtil
import com.duqian.app.live.viewmodel.RoomGlobalViewModel
import pub.devrel.easypermissions.EasyPermissions

/**
 * Description:房间专属的Activity基类，方便管理房间的状态和回调一些房间内的事件
 * 开播端和观众端共同继承的基类，提供给后续扩展相同等功能逻辑
 * Created by 杜乾 on 2022/8/10 - 15:39.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseLiveActivity : BaseActivity(), EasyPermissions.PermissionCallbacks {

    var mIsPushRoom = false //activity初始化的时候会设值
    var mRoomId = 0

    /**
     * 进入房间传递的参数，参数不同时，观众端/主播端按需转换成实现类
     */
    lateinit var mRoomParams: BaseRoomParams
    lateinit var mGlobalViewModel: RoomGlobalViewModel

    override fun initData() {
        mRoomParams = intent.getSerializableExtra(ParamKey.PARAM_KEY_ROOM_DATA) as BaseRoomParams
        mRoomId = mRoomParams.roomId
        mIsPushRoom = mRoomParams.isPushRoom
        Log.d("dq-room", "mRoomParams=$mRoomParams")

        //全局的viewModel
        mGlobalViewModel = ViewModelProvider(this)[RoomGlobalViewModel::class.java]
        mGlobalViewModel.isPushRoom.value = mIsPushRoom
        mGlobalViewModel.roomId.value = mRoomId
        mGlobalViewModel.roomType.value = RoomType.ROOM_TYPE_SINGLE

        mGlobalViewModel.isLandscape.observe(this) {
            onConfigurationChanged(it)
        }

    }

    open fun onConfigurationChanged(isLandscape: Boolean) {

    }

    override fun initView() {
        super.initView()
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        initControllers()
    }

    open fun initControllers() {
        //初始化各自activity的controllers
    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
    }

    override fun isStatusBarDark(): Boolean {
        return false
    }

    override fun initListener() {
        super.initListener()
        mRootView.findViewById<View>(R.id.iv_close_room)?.setOnClickListener {
            closeRoom()
        }
    }

    override fun onBackPressed() {
        super.onBackPressed()
        closeRoom()
    }

    private fun closeRoom() {
        //发送关闭直播间给其他业务逻辑处理下播和释放资源逻辑
        if (mIsPushRoom) {
            //ToastUtils.show("主播端，弹窗确认再退出直播间")
        }
        EventBusHelper.sendEvent(
            LiveRoomEvent(
                LiveRoomEventCode.EVENT_ID_LIVE_ROOM_END,
                mIsPushRoom
            )
        )
        finish()
    }

    open fun runUiThread(action: Runnable) {
        if (UIUtil.isOnUiThread) {
            action.run()
        } else {
            mHandler.post(action)
        }
    }

    open fun runOnUiThreadDelay(action: Runnable, delay: Long) {
        mHandler.postDelayed(action, delay)
    }

    open fun runOnUiThreadAtFront(action: Runnable) {
        mHandler.postAtFrontOfQueue(action)
    }

    open fun removeCallbacks(action: Runnable) {
        mHandler.removeCallbacks(action)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        // Forward results to EasyPermissions
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        Log.d("dq-room", "onPermissionsGranted requestCode=$requestCode")
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        Log.d("dq-room", "onPermissionsDenied requestCode=$requestCode")
    }
}