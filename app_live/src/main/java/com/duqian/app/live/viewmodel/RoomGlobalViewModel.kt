package com.duqian.app.live.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

/**
 * Description:直播间全局的状态管理，使用MutableLiveData，方便监听变化
 * LifecycleOwner为activity（如：LiveRoomActivity），子模块内部非直播间全局的ViewModel，请用fragment作为owner
 * 如果直播间的数据要暴露给外部，请定义在GlobalViewModel中。
 * Created by 杜乾 on 2022/8/11 - 14:02.
 * E-mail: duqian2010@gmail.com
 */
class RoomGlobalViewModel : ViewModel() {

    /**
     * 房间类型，房间改变，一般都要改变UI
     */
    val roomType = MutableLiveData<Int>()
    val roomId = MutableLiveData<Int>()
    val isPushRoom = MutableLiveData<Boolean>()
    val isLandscape = MutableLiveData<Boolean>()
    val isRoomLoading = MutableLiveData<Boolean>() //直播间loading

    //拓展直播间全局
}