package com.duqian.app.live.pushroom.data

import com.duqian.app.live.base_comm.BaseEvent

/**
 * 主播端，通用事件，一般很多事件会和直播间一样，这个类只定义主播端不一样的id
 * 尽量通过eventType区分事件类型，而不是重复新建各种event类。如果要新建，后续在这里补充添加其他直播间的事件类型，
 *
 * Created by 杜小菜 on 2022/8/10 - 12:48.
 * E-mail: duqian2010@gmail.com
 */
class PushRoomEvent(eventCode: Int, data: Any? = null) : BaseEvent(eventCode, data) {
}

object PushRoomEventCode {
    //定义事件常量,新增的事件id，注意自增，不要写重复了，遇到冲突的时候，确保id不一样即可。
    private const val EVENT_ID_PUSH_ROOM_BASE = 2000 //主播端模块基准id
    const val EVENT_ID_PUSH_ROOM_START_LIVE = EVENT_ID_PUSH_ROOM_BASE + 1  //开播事件
    const val EVENT_ID_PUSH_ROOM_LEAVE = EVENT_ID_PUSH_ROOM_BASE + 2 // 主播主动离开的事件
}
