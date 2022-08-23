package com.duqian.live.pusher.cmm_interface

/**
 * Description:推流相关 回调 定义
 *
 * Created by 杜乾 on 2022/8/18 - 15:08.
 * E-mail: duqian2010@gmail.com
 */
interface ILiveCallback {

    /**
     * action值越大，流程越后面
     */
    fun onSuccess(@LiveAction action: Int, uid: Int = 0)

    fun onFailed(
        @LiveAction action: Int,
        @LiveErrorCode errorCode: Int? = LiveErrorCode.ERROR_NONE,
        errorMsg: String? = ""
    )

    fun notifyEvent(@LiveState liveState: Int)
}