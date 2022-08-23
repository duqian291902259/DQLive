package com.duqian.app.live.liveroom.data

import com.duqian.app.live.base_comm.BaseRoomParams

/**
 * Description:外部进入直播房间，传参数做下封装
 * Created by 杜乾 on 2022/8/10 - 18:23.
 * E-mail: duqian2010@gmail.com
 */
class LiveRoomParams(roomId: Int, from: String? = "") :
    BaseRoomParams(roomId, from) {

    override fun toString(): String {
        return "RoomParams(roomId=$roomId, from=$from, mRoomType=$roomType)"
    }
}
