package com.duqian.live.pusher

import android.content.Context
import android.view.ViewGroup
import com.duqian.live.pusher.helper.LifecycleEvent

/**
 * Description:推流相关接口定义
 *
 * Created by 杜乾 on 2022/8/18 - 15:08.
 * E-mail: duqian2010@gmail.com
 */
interface IBasePusher {

    /**
     * 初始化推流配置
     */
    fun init(context: Context, videoContainer: ViewGroup)

    /**
     * 开始摄像头预览
     */
    fun startPreview()

    /**
     * 停止摄像头预览
     */
    fun stopPreview()

    /**
     * 进入房间/频道
     */
    fun joinChannel()

    /**
     * true则停止发送本地媒体流
     */
    fun muteLocalAudioStream(isMute: Boolean)

    /**
     * 退出房间/频道
     */
    fun leaveChannel()

    /**
     * 销毁资源
     * mRtcEngine.leaveChannel();mRtcEngine.destroy();
     */
    fun release()

    /**
     * 检测声音
     */
    fun startEchoTest(isTest: Boolean)

    /**
     * 禁止视频，声音功能
     */
    fun enableAudioVideo(isVideo: Boolean = false, isEnable: Boolean = true)

    /**
     * 开启CDN推流
     */
    fun startPushToCDN(isPublish: Boolean)

    /**
     * onResume
     */
    fun onLifecycleChanged(@LifecycleEvent event: Int)

}