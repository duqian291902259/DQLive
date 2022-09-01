package com.duqian.live.mediaplayer

import android.content.Context
import android.view.View
import com.google.android.exoplayer2.ui.PlayerView
import java.lang.Exception

/**
 * Description:播放器接口
 *
 * Created by 杜乾 on 2022/8/12 - 17:15.
 * E-mail: duqian2010@gmail.com
 */
interface ILivePlayerService {

    fun initPlayer(context: Context, playerView: PlayerView, loadingView: View?)

    fun startPlay(url: String)

    fun onResume()

    fun stopPlay()

    fun addCallback(callback: PlayerCallback?)

    fun onDestroy()

}

/**
 * Description:播放器监听
 *
 * Created by 杜乾 on 2022/8/15 - 07:37.
 * E-mail: duqian2010@gmail.com
 */
interface PlayerCallback {

    fun onLoadingChanged(isLoading: Boolean)

    fun onPlayerStateChanged(playbackState: Int)

    fun onPlayerError(e: Exception?)
}