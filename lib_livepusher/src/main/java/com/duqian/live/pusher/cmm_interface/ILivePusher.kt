package com.duqian.live.pusher.cmm_interface

import android.content.Context
import com.duqian.live.pusher.IBasePusher

/**
 * Description:推流相关接口定义
 *
 * Created by 杜乾 on 2022/8/18 - 15:08.
 * E-mail: duqian2010@gmail.com
 */
interface ILivePusher : IBasePusher {

    /**
     * 初始化推流配置
     */
    fun init(context: Context, config: LivePushConfig)

    /**
     * 更新配置，主要是用于token刷新，mRtcEngine.renewToken(token);
     */
    fun updateConfig(config: CommonConfig) {}

    /**
     * 检测推流设备状况，开播前处理，麦克风权限，录音权限等
     */
    fun checkDevice() {}

    /**
     * 调节本地播放的远程用户音量，
     * isAllUser决定了adjustPlaybackSignalVolume还是adjustUserPlaybackSignalVolume
     */
    fun adjustPlayerVolume(isAllUser: Boolean, volume: Int) {}

    /**
     * 切换摄像头:CameraCaptureConfiguration
     */
    fun switchCamera()

    /**
     * 调节本地采集的音量
     * 调整采集信号音量 rtcEngine.adjustRecordingSignalVolume(50);
     */
    fun adjustLocalVolume(volume: Int)


    /**
     * 外部设置推流整个过程中的状态，action值越大，流程越后面
     */
    fun setCallBack(callback: ILiveCallback?)

    /**
     * 设置日志打印，暴露给外部自定义log输出，或者保存声网日志的逻辑封装
     * mRtcEngine?.setLogFile(agoraPath)
     */
    fun setLogger(log: IPushLog)
}