package com.duqian.app

import android.Manifest
import android.os.Bundle
import android.util.Log
import android.view.Window
import android.view.WindowManager
import com.duqian.app.base.BaseActivity
import com.duqian.app.live.R
import com.duqian.app.helper.CommonUtils
import com.duqian.app.helper.ToastUtils
import com.duqian.app.live.base_comm.RoomConstants
import com.duqian.app.main.MainFragment
import com.duqian.app.navigator.AppNavigator
import com.duqian.nativelib.NativeLib
import dagger.hilt.android.AndroidEntryPoint
import pub.devrel.easypermissions.EasyPermissions
import javax.inject.Inject

/**
 * Description:首页
 *
 * Created by 杜乾 on 2022/8/08 - 07:02.
 * E-mail: duqian2010@gmail.com
 */
@AndroidEntryPoint
class MainActivity : BaseActivity(), EasyPermissions.PermissionCallbacks,
    EasyPermissions.RationaleCallbacks {

    @Inject
    lateinit var navigator: AppNavigator

    lateinit var mainFragment: MainFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        this.window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    private fun requirePermission() {
        if (!CommonUtils.checkSelfPermission(
                this,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        ) {
            EasyPermissions.requestPermissions(
                this,
                getString(R.string.text_external_storage_permission_rationale),
                RoomConstants.REQUEST_CODE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
            )
        }
    }

    override fun getLayoutId(): Int {
        return R.layout.activity_main
    }

    override fun initData() {
        initMainFragment()

        requirePermission()

        testNative()
    }

    private fun testNative() {
        ToastUtils.show("Native:${NativeLib().stringFromJNI()}")
    }

    override fun isStatusBarDark(): Boolean {
        return false
    }

    private fun initMainFragment() {
        mainFragment = MainFragment.newInstance()
        supportFragmentManager.beginTransaction()
            .replace(R.id.container, mainFragment)
            .commitNow()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
        if (requestCode == RoomConstants.REQUEST_CODE_CAMERA_AND_AUDIO) {
            Log.d(TAG, "Granted camera permission....")
            mainFragment.startLiveActivity()
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        ToastUtils.show("no camera permission")
    }

    override fun onRationaleAccepted(requestCode: Int) {
    }

    override fun onRationaleDenied(requestCode: Int) {
    }

    companion object {
        private const val TAG = "dq-main"
    }
}