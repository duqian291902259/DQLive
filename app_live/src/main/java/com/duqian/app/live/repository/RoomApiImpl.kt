package com.duqian.app.live.repository

import com.duqian.app.live.services.IRoomApi
import javax.inject.Inject

/**
 * Description:接口实现类
 *
 * Created by 杜乾 on 2022/8/14 - 12:50.
 * E-mail: duqian2010@gmail.com
 */
class RoomApiImpl @Inject constructor() : IRoomApi {
    override fun getRoomInfo(): String? {
        return "度小菜 roomInfo"
    }
}