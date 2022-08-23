package com.duqian.live.pusher.helper

import androidx.annotation.IntDef

class CommonState {
}

/**
 * 当前的页面状态
 */
@IntDef(value = [LifecycleEvent.ON_RESUME, LifecycleEvent.ON_PAUSE, LifecycleEvent.ON_DESTROY])
@Retention(AnnotationRetention.SOURCE)
annotation class LifecycleEvent {
    companion object {
        const val ON_RESUME = 0
        const val ON_PAUSE = 1
        const val ON_DESTROY = 2
    }
}
