package com.duqian.live.pusher

/**
 * Description:推流相关 回调 定义
 *
 * Created by 杜乾 on 2022/8/18 - 15:08.
 * E-mail: duqian@flatincbr.com
 */
interface ILiveCallback {

    /**
     * action值越大，流程越后面
     */
    fun onSuccess(action: Int, uid: Int = 0)

    fun onFailed(errorCode: Int? = 0, errorMsg: String? = "")

    fun onConnectionStateChanged(state: Int, reason: Int) {}
}

object ResultCode {
    const val STATUS_OK = 0 //成功
    const val STATUS_FAILED = -1 //失败
}
