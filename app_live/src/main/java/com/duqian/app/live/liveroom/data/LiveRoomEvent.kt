package com.duqian.app.live.liveroom.data

import com.duqian.app.live.base_comm.BaseEvent

/**
 * Description:直播间通用事件
 * Created by 杜小菜 on 2022/8/10 - 12:48.
 * E-mail: duqian2010@gmail.com
 */
/**
 * 观众端，通用事件
 */
class LiveRoomEvent(eventCode: Int, data: Any? = null) : BaseEvent(eventCode, data) {
}

//Note 尽量通过eventType区分事件类型，而不是重复新建各种event类。如果要新建，后续在这里补充添加其他直播间的事件类型，

//定义事件常量,新增的事件id，注意自增，不要写重复了，遇到冲突的时候，确保id不一样即可。
object LiveRoomEventCode {
    private const val EVENT_ID_LIVE_ROOM_BASE = 0x1000  //直播间模块基准id
    const val EVENT_ID_LIVE_ROOM_ENTER = EVENT_ID_LIVE_ROOM_BASE + 1  //进入房间的事件
    const val EVENT_ID_LIVE_ROOM_END = EVENT_ID_LIVE_ROOM_BASE + 2 // 退出直播间的事件
}

