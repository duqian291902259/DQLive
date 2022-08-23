package com.duqian.app.navigator

import androidx.fragment.app.FragmentActivity
import com.duqian.app.live.base_comm.RoomFrom
import com.duqian.app.live.liveroom.LiveRoomActivity
import com.duqian.app.live.liveroom.data.LiveRoomParams
import com.duqian.app.live.pushroom.PushRoomActivity
import com.duqian.app.live.pushroom.data.PushRoomParams
import javax.inject.Inject

/**
 * Description:实现房间跳转
 *
 * Created by 杜乾 on 2022/8/14 - 13:50.
 * E-mail: duqian2010@gmail.com
 */
class AppNavigatorImpl @Inject constructor(private val activity: FragmentActivity) : AppNavigator {

    override fun navigateTo(screen: RoomScreens) {
        when (screen) {
            RoomScreens.LIVE_ROOM -> {
                //ToastUtils.show("enter live room")
                //粘性事件
                //LiveEventBus.get().with(LiveEventKey.EVENT_KEY_VIEWER_COUNT, Int::class.java).postValue(68899)

                val roomParams = LiveRoomParams(12345, RoomFrom.ROOM_FROM_LIVE)
                LiveRoomActivity.startActivity(activity, roomParams)
            }
            RoomScreens.PUSH_ROOM -> {
                //ToastUtils.show("enter push room")
                val roomParams = PushRoomParams(6666, RoomFrom.ROOM_FROM_LIVE)
                PushRoomActivity.startActivity(activity, roomParams)
            }
        }

        /*activity.supportFragmentManager.beginTransaction()
            .replace(R.id.main_container, fragment)
            .addToBackStack(fragment::class.java.canonicalName)
            .commit()*/
    }
}
