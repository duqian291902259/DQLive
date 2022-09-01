package com.duqian.app.main

import android.Manifest
import androidx.lifecycle.ViewModelProvider
import com.duqian.app.live.R
import com.duqian.app.base.BaseApplication
import com.duqian.app.base.BaseFragment
import com.duqian.app.helper.CommonUtils
import com.duqian.app.live.base_comm.RoomConstants
import com.duqian.app.navigator.AppNavigator
import com.duqian.app.navigator.RoomScreens
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_main.*
import me.ele.uetool.UETool
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/**
 * Description:首页MainFragment
 *
 * Created by 杜乾 on 2022/8/8 - 17:08.
 * E-mail: duqian2010@gmail.com
 */

@AndroidEntryPoint
class MainFragment : BaseFragment() {

    companion object {
        private const val TAG = "MainFragment"

        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var navigator: AppNavigator

    override fun getLayoutId(): Int {
        return R.layout.fragment_main
    }

    override fun initData() {

    }

    override fun initView() {
        mainContainer.setOnLongClickListener {
            val enable: Boolean =
                BaseApplication.instance.mmkv?.decodeBool("UEToolEnable", false) ?: false
            BaseApplication.instance.mmkv?.encode("UEToolEnable", !enable)
            if (enable) UETool.dismissUETMenu() else UETool.showUETMenu()
            false
        }
    }

    override fun initListener() {
        tv_go_live.setOnClickListener {
            requestLivePermission()
        }
        tv_live_room.setOnClickListener {
            navigator.navigateTo(RoomScreens.LIVE_ROOM)
            /*val roomParams = LiveRoomParams(12345, RoomFrom.ROOM_FROM_LIVE)
            LiveRoomActivity.startActivity(context, roomParams)*/
        }
    }

    override fun observerData() {
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
    }

    fun startLiveActivity() {
        //val roomParams = PushRoomParams(6666, RoomFrom.ROOM_FROM_LIVE)
        //PushRoomActivity.startActivity(context, roomParams)
        navigator.navigateTo(RoomScreens.PUSH_ROOM)
    }

    private fun requestLivePermission() {
        if (CommonUtils.checkSelfPermission(
                requireActivity(),
                Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO
            )
        ) {
            startLiveActivity()
        } else {
            //ToastUtils.show("no camera permission")
            EasyPermissions.requestPermissions(
                requireActivity(),
                getString(R.string.text_camera_and_audio_rationale),
                RoomConstants.REQUEST_CODE_CAMERA_AND_AUDIO,
                Manifest.permission.CAMERA,
                Manifest.permission.RECORD_AUDIO
            )
        }
    }
}