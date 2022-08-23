package com.duqian.app.live.base_comm

/**
 * Description:房间常量
 * Created by 杜乾 on 2022/8/10 - 16:22.
 * E-mail: duqian2010@gmail.com
 */
//房间常量
object RoomConstants {
    const val HOST_STATUS_ON_LIVE = 0
    const val HOST_STATUS_LEAVE = 1

    //权限申请
    private const val REQUEST_CODE_BASE = 5000
    const val REQUEST_CODE_CAMERA_AND_AUDIO = REQUEST_CODE_BASE + 1
    const val REQUEST_CODE_EXTERNAL_STORAGE = REQUEST_CODE_BASE + 2
}

//本地的房间类型
object RoomType {
    const val ROOM_TYPE_SINGLE = 0 //目前只有单人直播的直播间
    const val ROOM_TYPE_DOUBLE = 2 //双人连麦的直播间
}

//from，进入直播间的来源
object RoomFrom {
    const val ROOM_FROM_LIVE = "from_live" //从主页live列表进入直播间
}

//传参数的Key
object ParamKey {
    const val PARAM_KEY_ROOM_DATA = "room_data"
    const val PARAM_KEY_DIALOG_DATA = "dialog_data"
}

//LiveEventBus的Key
object LiveEventKey {
    const val EVENT_KEY_VIEWER_COUNT = "viewer_count" //房间人数变化
    const val EVENT_KEY_CLEAR_LIVE_SCREEN = "clear_live_screen" //清屏
    const val EVENT_KEY_SEND_GIFT_START = "send_gift_start" //开始送礼
}

