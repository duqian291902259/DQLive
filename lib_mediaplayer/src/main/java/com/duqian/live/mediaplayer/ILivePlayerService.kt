package com.duqian.live.mediaplayer

import android.content.Context
import com.google.android.exoplayer2.ui.PlayerView

/**
 * Description:播放器接口
 *
 * Created by 杜乾 on 2022/8/12 - 17:15.
 * E-mail: duqian@flatincbr.com
 */
interface ILivePlayerService {

    fun initPlayer(context: Context, playerView: PlayerView)

    fun startPlay(url: String)

    fun isPlaying(): Boolean

    fun getPlayState(): String

    fun resumePlay()

    fun pausePlay()

    fun stopPlay()

    fun setVolume(audioVolume: Float)

    fun addCallback(callback: PlayerCallback?)

    fun onDestroy()

}

/**
 * Description:播放器监听
 *
 * Created by 杜乾 on 2022/8/15 - 07:37.
 * E-mail: duqian@flatincbr.com
 */
interface PlayerCallback {

    fun onLoadingChanged(isLoading: Boolean)

    fun onPlayerStateChanged(@PlayState playbackState: String)

    /**
     * 首帧回调
     */
    fun onRenderedFirstFrame() {}

    fun onPlayerError(e: Exception?)

    fun onIsPlayingChanged(isPlaying: Boolean) {}
}

