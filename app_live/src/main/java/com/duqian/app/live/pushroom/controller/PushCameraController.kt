package com.duqian.app.live.pushroom.controller

import android.view.View
import androidx.camera.core.CameraSelector
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseController
import com.google.common.util.concurrent.ListenableFuture
import com.duqian.app.live.utils.setViewVisible

/**
 * Description:开播端，基于CameraX的摄像头预览逻辑
 *
 * Created by 杜乾 on 2022/8/11 - 19:43.
 * E-mail: duqian2010@gmail.com
 */
class PushCameraController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    private var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>? = null

    private lateinit var previewView: PreviewView
    override fun initView(rootView: View) {
        previewView = rootView.findViewById(R.id.previewView)
        previewView.setViewVisible(true)
        initCamera()
    }

    private fun initCamera() {
        cameraProviderFuture = ProcessCameraProvider.getInstance(mActivity)
        cameraProviderFuture?.addListener({
            val cameraProvider = cameraProviderFuture!!.get()
            bindPreview(cameraProvider)
        }, ContextCompat.getMainExecutor(mActivity))
    }

    private fun bindPreview(cameraProvider: ProcessCameraProvider) {
        val preview = Preview.Builder().build()

        val cameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            //.requireLensFacing(CameraSelector.LENS_FACING_FRONT)
            .build()

        preview.setSurfaceProvider(previewView.surfaceProvider)

        val camera = cameraProvider.bindToLifecycle(mLifecycleOwner, cameraSelector, preview)
    }

    override fun onResume() {

    }

    override fun onDestroy() {

    }
}