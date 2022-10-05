package com.duqian.live.mediaplayer

import androidx.annotation.StringDef

/**
 * 直播互动的角色类型
 */
@StringDef(
    value = [PlayState.STATE_IDLE, PlayState.STATE_BUFFERING, PlayState.STATE_READY, PlayState.STATE_ENDED, PlayState.STATE_ERROR]
)
@Retention(AnnotationRetention.SOURCE)
annotation class PlayState {
    companion object {
        /** The player does not have any media to play.  */
        const val STATE_IDLE = "STATE_IDLE"

        /**
         * The player is not able to immediately play from its current position. This state typically
         * occurs when more data needs to be loaded.
         */
        const val STATE_BUFFERING = "STATE_BUFFERING"

        /**
         * The player is able to immediately play from its current position. The player will be playing if
         * [.getPlayWhenReady] is true, and paused otherwise.
         */
        const val STATE_READY = "STATE_READY"

        /** The player has finished playing the media.  */
        const val STATE_ENDED = "STATE_ENDED"

        /**
         * 自定义的错误状态
         */
        const val STATE_ERROR = "STATE_ERROR"
    }
}

