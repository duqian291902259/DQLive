package com.duqian.live.pusher.cmm_interface

/**
 * Description:日志接口
 *
 * Created by 杜乾 on 2022/8/18 - 16:01.
 * E-mail: duqian2010@gmail.com
 */
interface IPushLog {
    fun i(tag: String = DEFAULT_TAG, msg: String)

    fun d(tag: String = DEFAULT_TAG, msg: String)

    fun e(tag: String = DEFAULT_TAG, msg: String)

    fun saveLogToLocal(filePath:String)
}

const val DEFAULT_TAG = "dq-pusher"

object PushLog {
    var isDebug = false

    var log: IPushLog? = null

    fun i(tag: String, msg: String) {
        log?.i(tag, msg)
    }

    fun d(tag: String, msg: String) {
        if (isDebug) {
            log?.d(tag, msg)
        }
    }

    fun e(tag: String, msg: String) {
        log?.e(tag, msg)
    }

    fun e(tag: String, msg: String, tr: Throwable) {
        log?.e(tag, "$msg,$tr")
    }
}

/*  private fun initLog() {
        ALog.isDebug = BuildConfig.DEBUG
        ALog.log = ALogImp()
    }


    class ALogImp : IALog {
        override fun i(tag: String, msg: String) {
            Log.i(tag, msg)
        }

        override fun d(tag: String, msg: String) {
            Log.d(tag, msg)
        }

        override fun e(tag: String, msg: String) {
            Log.e(tag, msg)
        }
    }*/


