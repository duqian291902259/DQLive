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
    fun init(context: Context, videoContainer: ViewGroup): Int

    /**
     * 开始摄像头预览
     */
    fun startPreview(): Int

    /**
     * 停止摄像头预览
     */
    fun stopPreview(): Int

    /**
     * 进入房间/频道
     */
    fun joinChannel(): Int

    /**
     * true则停止发送本地媒体流
     */
    fun muteLocalAudioStream(isMute: Boolean): Int

    /**
     * 退出房间/频道
     */
    fun leaveChannel(): Int

    /**
     * 销毁资源
     * mRtcEngine.leaveChannel();mRtcEngine.destroy();
     */
    fun release()

    /**
     * 检测声音
     */
    fun startEchoTest(isTest: Boolean): Int

    /**
     * 禁止视频，声音功能
     */
    fun enableAudioVideo(isVideo: Boolean = false, isEnable: Boolean = true): Int

    /**
     * 开启CDN推流
     */
    fun startPushToCDN(url: String?): Int

    /**
     * onResume
     */
    fun onLifecycleChanged(@LifecycleEvent event: Int)

    /**
     * 推流声音
     */
    fun adjustRecordingSignalVolume(volume: Int): Int

    /**
     * 外部设置推流整个过程中的状态，action值越大，流程越后面
     */
    fun setCallBack(callback: ILiveCallback?)
}