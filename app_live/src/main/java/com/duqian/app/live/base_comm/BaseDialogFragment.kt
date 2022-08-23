package com.duqian.app.live.base_comm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import pub.devrel.easypermissions.EasyPermissions.PermissionCallbacks
import pub.devrel.easypermissions.EasyPermissions

/**
 * Description:DialogFragment基类
 *
 * Created by 杜乾 on 2022/8/13 - 22:22.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseDialogFragment : DialogFragment() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val bundle = arguments
        bundle?.let { parseBundle(it) }
    }

    private fun parseBundle(bundle: Bundle) {}
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layoutId, container)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        iniView()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        iniListener()
        iniData()
    }

    abstract val layoutId: Int
    abstract fun iniView()
    abstract fun iniData()
    abstract fun iniListener()

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (this is PermissionCallbacks) {
            EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this)
        }
    }

    val isShowing: Boolean get() = dialog != null && dialog!!.isShowing
}