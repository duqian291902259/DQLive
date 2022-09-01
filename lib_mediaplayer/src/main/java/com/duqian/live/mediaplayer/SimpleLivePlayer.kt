package com.duqian.live.mediaplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import android.view.View
import com.google.android.exoplayer2.ExoPlaybackException
import com.google.android.exoplayer2.ExoPlayerFactory
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelection
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout
import com.google.android.exoplayer2.ui.PlayerView
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.google.auto.service.AutoService
import java.util.*

/**
 * Description:简单的拉流播放器，基于ExoPlayer 2.9.6
 *
 * Created by 杜乾 on 2022/8/12 - 13:38.
 * E-mail: duqian2010@gmail.com
 */
@AutoService(value = [ILivePlayerService::class])
class SimpleLivePlayer : ILivePlayerService {

    private var player: SimpleExoPlayer? = null//lateinit property player has not been initialized
    private lateinit var playerView: PlayerView

    override fun initPlayer(context: Context, playerView: PlayerView, loadingView: View?) {
        this.playerView = playerView

        initPlayer(context, playerView)

        player?.also {
            it.addListener(object : Player.EventListener {
                override fun onLoadingChanged(isLoading: Boolean) {
                    super.onLoadingChanged(isLoading)
                    Log.d("dq-player", "isLoading=$isLoading")
                    //loadingView?.visibility = if (isLoading) View.VISIBLE else View.GONE
                    mCallback?.onLoadingChanged(isLoading)
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    super.onPlayerStateChanged(playWhenReady, playbackState)
                    //playWhenReady=true
                    //playbackState=1,failed
                    //playbackState=2,停止播放
                    //playbackState=3,拉流成功，播放
                    /* if (playbackState == Player.STATE_READY) {
                         loadingView?.visibility = View.GONE
                     }*/
                    Log.d(
                        "dq-player",
                        "onPlayerStateChanged,playWhenReady=$playWhenReady,playbackState=$playbackState"
                    )
                    mCallback?.onPlayerStateChanged(playbackState)
                }

                override fun onPlayerError(error: ExoPlaybackException?) {
                    super.onPlayerError(error)
                    Log.d("dq-player", "error=$error")
                    mCallback?.onPlayerError(error)
                }
            })
        }

    }

    private fun initPlayer(context: Context, playerView: PlayerView) {
        if (player == null) {
            //val bandwidthMeter: BandwidthMeter = DefaultBandwidthMeter()
            val videoTrackSelectionFactory: TrackSelection.Factory =
                AdaptiveTrackSelection.Factory()
            val trackSelector: TrackSelector = DefaultTrackSelector(videoTrackSelectionFactory)

            player = ExoPlayerFactory.newSimpleInstance(context, trackSelector)

            playerView.player = player

            playerView.useController = false
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FILL
        }
    }

    /**
     * 播放的方法，待完善
     */
    override fun startPlay(url: String) {
        stopPlay()
        //val url = "rtmp://examplepull.agoramdn.com/live/duqian"
        //val url = "https://examplepull.agoramdn.com/live/duqian.flv"
        val videoSource = createMediaSourceByUrl(url)
        player?.also {
            it.prepare(videoSource)
            it.playWhenReady = true
            if (url.endsWith("mp4")) {
                //it.repeatMode = Player.REPEAT_MODE_ONE //REPEAT_MODE_ALL闪烁
            } else {
                it.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
    }

    override fun onResume() {

    }

    private fun createMediaSourceByUrl(url: String): MediaSource {
        val isRtmpStream = url.lowercase(Locale.getDefault()).startsWith("rtmp://")
        val dataSourceFactory: DataSource.Factory =
            if (isRtmpStream) {
                RtmpDataSourceFactory()
            } else {
                DefaultDataSourceFactory(
                    playerView.context,
                    Util.getUserAgent(playerView.context, "DQLive")
                )
            }
        return ExtractorMediaSource.Factory(dataSourceFactory)
            .createMediaSource(Uri.parse(url))
    }

    override fun stopPlay() {
        player?.stop()
    }

    private var mCallback: PlayerCallback? = null

    override fun addCallback(callback: PlayerCallback?) {
        this.mCallback = callback
    }

    override fun onDestroy() {
        player?.stop()
        player?.release()
    }
}