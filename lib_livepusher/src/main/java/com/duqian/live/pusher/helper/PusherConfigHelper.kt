package com.duqian.live.pusher.helper

import io.agora.rtc.video.VideoEncoderConfiguration

/**
 * Description:配置参数转换为声网的配置，先跟目前的1v1通话的配置AvConfig类似
 *
 * Created by 杜乾 on 2022/8/18 - 22:00.
 * E-mail: duqian2010@gmail.com
 */
object PusherConfigHelper {

    /**
     * 根绝视频分辨率和帧率档位，初始化视频配置
     */
    fun initVideoEncoderConfiguration(
        videoDimensionsLevel: Int,
        fpsLevel: Int
    ): VideoEncoderConfiguration {
        // 档位 1：160x120  2：320x240   3：480x360  4：640x480  5：960x720  6：1280x720
        val videoDimensions: VideoEncoderConfiguration.VideoDimensions =
            when (videoDimensionsLevel) {
                1 -> VideoEncoderConfiguration.VD_160x120
                2 -> VideoEncoderConfiguration.VD_320x240
                3 -> VideoEncoderConfiguration.VD_480x360
                4 -> VideoEncoderConfiguration.VD_640x480
                5 -> VideoEncoderConfiguration.VD_960x720
                6 -> VideoEncoderConfiguration.VD_1280x720
                else -> VideoEncoderConfiguration.VD_640x480
            }

        // 档位 1：7帧    2：10帧   3：15帧  4：24帧   5：30帧
        val frameFPS: VideoEncoderConfiguration.FRAME_RATE = when (fpsLevel) {
            1 -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_7
            2 -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_10
            3 -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15
            4 -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_24
            5 -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_30
            else -> VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15
        }

        return VideoEncoderConfiguration(
            videoDimensions,
            frameFPS,
            VideoEncoderConfiguration.STANDARD_BITRATE,
            VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
        )
    }
}