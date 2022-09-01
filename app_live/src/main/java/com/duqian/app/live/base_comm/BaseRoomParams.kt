package com.duqian.app.live.base_comm

import java.io.Serializable

/**
 * Description:房间参数基类，外部进入房间，传参用对应的子类做下封装：直播间用RoomParams，开播端用LiveRoomParams
 *
 * Created by 杜乾 on 2022/8/10 - 18:23.
 * E-mail: duqian2010@gmail.com
 */
open class BaseRoomParams(val roomId: Int, val from: String? = "") :
    Serializable {
    //parcelable效率高，但是要实现比较多方法，暂时用Serializable，参数多后用parcelable

    var isPushRoom = false //activity初始化的时候会设值

    var roomType = RoomType.ROOM_TYPE_SINGLE

    var payFrom = ""

    override fun toString(): String {
        return "BaseRoomParams(roomId=$roomId, from=$from, isPushRoom=$isPushRoom, payFrom='$payFrom')"
    }

}