package com.duqian.live.pusher.cmm_interface

import androidx.annotation.IntDef

/**
 * Description:推流相关 状态 定义
 *
 * Created by 杜乾 on 2022/8/18 - 15:08.
 * E-mail: duqian2010@gmail.com
 */

/**
 * 当前的直播状态，用于回调出去
 */
@IntDef(value = [LiveState.STATE_INIT, LiveState.STATE_ONLINE, LiveState.STATE_OFFLINE])
@Retention(AnnotationRetention.SOURCE)
annotation class LiveState {
    companion object {
        const val STATE_INIT = BASE_NONE + 1
        const val STATE_ONLINE = BASE_NONE + 2
        const val STATE_OFFLINE = BASE_NONE + 3
    }
}

/**
 * 当前用户的直播状态，0在线，1离线
 */
@IntDef(value = [LiveType.LIVE_TYPE_VIDEO_SINGLE, LiveType.LIVE_TYPE_VIDEO_MULTI, LiveType.LIVE_TYPE_AUDIO])
@Retention(AnnotationRetention.SOURCE)
annotation class LiveType {
    companion object {
        const val LIVE_TYPE_VIDEO_SINGLE = 0 // 单人视频房间
        const val LIVE_TYPE_VIDEO_MULTI = 1 // 多人视频房间
        const val LIVE_TYPE_AUDIO = 2 // 语音房间
    }
}

/**
 * 直播互动的角色类型
 */
@IntDef(value = [LiveRole.ROLE_ANCHOR, LiveRole.ROLE_AUDIENCE, LiveRole.ROLE_SPEAKER])
@Retention(AnnotationRetention.SOURCE)
annotation class LiveRole {
    companion object {
        const val ROLE_ANCHOR = 0   //主播
        const val ROLE_AUDIENCE = 1 //观众
        const val ROLE_SPEAKER = 2  //连麦说话者
    }
}

private const val BASE_NONE = 0

/**
 * 推流整个过程，涉及到的流程，常量，可拓展
 * 通过注解限定类型
 */
@IntDef(
    value = [LiveAction.ACTION_INIT, LiveAction.ACTION_CHECK_DEVICES, LiveAction.ACTION_PREVIEW,
        LiveAction.ACTION_JOIN_CHANNEL, LiveAction.ACTION_LEAVE_CHANNEL, LiveAction.ACTION_SETUP_REMOTE_VIDEO,
        LiveAction.ACTION_DESTROY]
)
@Retention(AnnotationRetention.SOURCE)
annotation class LiveAction {
    //object LiveAction {
    companion object {

        //初始化
        const val ACTION_INIT = BASE_NONE + 1

        //设备检测
        const val ACTION_CHECK_DEVICES = BASE_NONE + 2

        //摄像头预览
        const val ACTION_PREVIEW = BASE_NONE + 3

        //开启本地视频 enableVideo setupLocalVideo
        const val ACTION_SETUP_LOCAL_VIDEO = BASE_NONE + 4

        //设置多媒体参数
        const val ACTION_SET_MEDIA_OPTIONS = BASE_NONE + 5

        //加入频道
        const val ACTION_JOIN_CHANNEL = BASE_NONE + 6

        //设置远端视频
        const val ACTION_SETUP_REMOTE_VIDEO = BASE_NONE + 7

        //停止预览
        const val ACTION_STOP_PREVIEW = BASE_NONE + 8

        //离开频道
        const val ACTION_LEAVE_CHANNEL = BASE_NONE + 9

        //重新进入频道
        const val ACTION_REJOIN_CHANNEL = BASE_NONE + 10

        //destroy
        const val ACTION_DESTROY = BASE_NONE + 11
    }
}

/**
 * 连接状态
 */
@IntDef(
    value = [ConnectionState.CNN_STATE_DISCONNECTED, ConnectionState.CNN_STATE_CONNECTING,
        ConnectionState.CNN_STATE_CONNECTED, ConnectionState.CNN_STATE_RECONNECTING, ConnectionState.CNN_STATE_FAILED]
)
@Retention(AnnotationRetention.SOURCE)
annotation class ConnectionState {
    companion object {
        const val CNN_STATE_DISCONNECTED = 0
        const val CNN_STATE_CONNECTING = 1
        const val CNN_STATE_CONNECTED = 2
        const val CNN_STATE_RECONNECTING = 3
        const val CNN_STATE_FAILED = 4
    }
}

private const val ERROR_BASE = 100

/**
 * 定义错误状态码：
 * 0: 方法调用成功。< 0: 方法调用失败。
 */
@IntDef(value = [LiveErrorCode.ERROR_NONE, LiveErrorCode.ERROR_INIT, LiveErrorCode.ERROR_JOIN_CHANNEL])
@Retention(AnnotationRetention.SOURCE)
annotation class LiveErrorCode {
    companion object {
        const val ERROR_NONE = 0
        const val ERROR_INIT = ERROR_BASE + 1
        const val ERROR_JOIN_CHANNEL = ERROR_BASE + 2
    }
}
