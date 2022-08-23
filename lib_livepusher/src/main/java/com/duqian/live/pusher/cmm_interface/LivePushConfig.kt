package com.duqian.live.pusher.cmm_interface

import android.view.ViewGroup
import com.duqian.live.pusher.helper.PusherConfigHelper

/**
 * Description:推流的配置信息
 *
 * Created by 杜乾 on 2022/8/18 - 15:31.
 * E-mail: duqian2010@gmail.com
 */
class LivePushConfig(
    commonConfig: CommonConfig,
    audioConfig: AudioConfig? = AudioConfig(0, 0),
    videoConfig: VideoConfig? = null,
) : IConfig {}

/**
 * Description:基础的配置，app_id,token,uid，role，开关控制等
 * 是否开启视频直播:enableVideo = true标识开启
 */
class CommonConfig(
    val uid: String = "0",
    val appId: String = "",
    val token: String? = "",
    val enableVideo: Boolean = false//enableVideo=false，表示视频配置不生效，非互动直播，只是音频房间
) : IConfig {

    //加入频道的角色
    var role = LiveRole.ROLE_ANCHOR

    //频道场景
    var liveType = LiveType.LIVE_TYPE_VIDEO_SINGLE

    //日志文件的本地路径
    var logFilePath: String? = ""

    //频道名称
    var channelName = uid //默认就是uid
    override fun toString(): String {
        return "BaseConfig(uid='$uid', appId='$appId', token=$token, role=$role, liveType=$liveType, logFilePath=$logFilePath, channelName='$channelName')"
    }
}

/**
 * Description:视频推流配置，给定默认的配置
 * 如果在加入频道后不需要重新设置视频编码属性，Agora 建议在
 * enableVideo 前调用 setVideoEncoderConfiguration，以加快首帧出图的时间。
 */
class VideoConfig(
    val localContainer: ViewGroup,
    val videoDimensionsLevel: Int = 4,
    val fpsLevel: Int = 3,
) : IConfig {
    //var mRemoteContainer: ViewGroup? = null
    var uploadBitrate: Int = 500 * 1000 // 上行质量
    var downloadBitrate: Int = 500 * 1000 // 上行质量

    //以下参数备用，目前是默认值
    var width = 480
    var height = 640
    var frameRate = 15
    var bitrate = 500    //agora的码率用的单位是kbps

    //Agora最终的视频配置
    var config = PusherConfigHelper.initVideoEncoderConfiguration(videoDimensionsLevel, fpsLevel)
}

/**
 * audioProfile = 0 //设置采样率，码率，编码模式和声道数,0~5
 * var audioScenario = 0//设置音频应用场景,0~5
 */
class AudioConfig(val audioProfile: Int = 0, val audioScenario: Int = 0) : IConfig {
    //以下参数备用，目前是默认值
    var audioSampleRate = 48
    var audioBitrate = 64
    var audioChannels = 1

    /**
     * Description:音频推流配置，给定默认的配置
     * 对应 mAudioScenario
     * 设置音频编码属性 RtcEngine.setAudioProfile(Constants.AUDIO_PROFILE_DEFAULT);
     *
     * profile
    音频编码属性，包含采样率、码率、编码模式和声道数。
    DEFAULT (0)：默认值
    直播场景下：48 kHz 采样率，音乐编码，单声道，编码码率最大值为 64 Kbps。
    通信场景下：32 kHz 采样率，语音编码，单声道，编码码率最大值为 18 Kbps。
    SPEECH_STANDARD (1)：指定 32 kHz 采样率，语音编码，单声道，编码码率最大值为 18 Kbps。
    MUSIC_STANDARD (2)：指定 48 kHz 采样率，音乐编码，单声道，编码码率最大值为 64 Kbps。
    MUSIC_STANDARD_STEREO (3)：指定 48 kHz 采样率，音乐编码，双声道，编码码率最大值为 80 Kbps。
    MUSIC_HIGH_QUALITY (4)：指定 48 kHz 采样率，音乐编码，单声道，编码码率最大值为 96 Kbps。
    MUSIC_HIGH_QUALITY_STEREO (5)：指定 48 kHz 采样率，音乐编码，双声道，编码码率最大值为 128 Kbps。
    scenario
    音频场景。不同的音频场景下，设备的音量类型是不同的。
    AUDIO_SCENARIO_DEFAULT (0): （默认）自动场景，根据用户角色和音频路由自动匹配合适的音质。
    AUDIO_SCENARIO_GAME_STREAMING (3): 高音质场景，适用于音乐为主的场景。
    AUDIO_SCENARIO_CHATROOM (5): 聊天室场景，适用于用户需要频繁上下麦的场景。该场景下，观众会收到申请麦克风权限的弹窗提示。
    AUDIO_SCENARIO_CHORUS (7): 合唱场景。适用于网络条件良好，要求极低延时的实时合唱场景。
    注意：使用该枚举前，你需要调用 getAudioDeviceInfo 获取音频设备是否支持极低延时采集和播放。只有在支持极低延时（isLowLatencyAudioSupported = true）的音频设备上，才能够体验到极低延时。
    AUDIO_SCENARIO_MEETING (8): 会议场景，适用于人声为主的多人会议
     */

}

interface IConfig {

}