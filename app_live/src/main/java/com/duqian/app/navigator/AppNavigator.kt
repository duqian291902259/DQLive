package com.duqian.app.navigator

/**
 * 直播间房间类型，screens.
 */
enum class RoomScreens {
    LIVE_ROOM,
    PUSH_ROOM,
}

/**
 * Description:定义页面导航
 *
 * Created by 杜乾 on 2022/8/14 - 13:47.
 * E-mail: duqian2010@gmail.com
 */
interface AppNavigator {
    fun navigateTo(screen: RoomScreens)
}
