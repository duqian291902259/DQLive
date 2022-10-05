package com.duqian.live.mediaplayer

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.ext.rtmp.RtmpDataSourceFactory
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
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
 * E-mail: duqian@flatincbr.com
 */
@AutoService(value = [ILivePlayerService::class])
class SimpleLivePlayer : ILivePlayerService {

    private var mPlayer: SimpleExoPlayer? = null

    private lateinit var playerView: PlayerView
    private var mCallback: PlayerCallback? = null

    private val playerListener = object : Player.Listener {
        override fun onIsLoadingChanged(isLoading: Boolean) {
            super.onIsLoadingChanged(isLoading)
            mCallback?.onLoadingChanged(isLoading)
            Log.d(TAG, "onLoadingChanged=$isLoading")
        }

        override fun onPlaybackStateChanged(state: Int) {
            super.onPlaybackStateChanged(state)
            val playbackState: String = translateState(state)
            Log.d(TAG, "onPlaybackStateChanged=$playbackState")
            mCallback?.onPlayerStateChanged(playbackState)
        }

        override fun onIsPlayingChanged(isPlaying: Boolean) {
            super.onIsPlayingChanged(isPlaying)
            Log.d(TAG, "onIsPlayingChanged=$isPlaying")
            mCallback?.onIsPlayingChanged(isPlaying)
        }

        override fun onPlayerError(error: PlaybackException) {
            super.onPlayerError(error)
            Log.d(TAG, "onPlayerError=$error")
            super.onPlayerError(error)
            mCallback?.onPlayerError(error)
        }

        override fun onRenderedFirstFrame() {
            super.onRenderedFirstFrame()
            Log.d(TAG, "onRenderedFirstFrame")
        }
    }

    private fun translateState(state: Int) = when (state) {
        Player.STATE_IDLE -> {
            PlayState.STATE_IDLE
        }
        Player.STATE_BUFFERING -> {
            PlayState.STATE_BUFFERING
        }
        Player.STATE_READY -> {
            PlayState.STATE_READY
        }
        Player.STATE_ENDED -> {
            PlayState.STATE_ENDED
        }
        else -> {
            PlayState.STATE_ERROR
        }
    }

    override fun initPlayer(context: Context, playerView: PlayerView) {
        this.playerView = playerView

        initPlayer(context)

        mPlayer?.also {
            it.addListener(playerListener)
        }
    }

    private fun initPlayer(context: Context) {
        if (mPlayer == null) {
            mPlayer = SimpleExoPlayer.Builder(context).build()
            playerView.player = mPlayer
            playerView.useController = false
            playerView.resizeMode = AspectRatioFrameLayout.RESIZE_MODE_ZOOM
        }
    }

    /**
     * 播放的方法
     */
    override fun startPlay(url: String) {
        stopPlay()

        mPlayer?.also {
            val mediaSource = createMediaSourceByUrl(url)
            it.setMediaSource(mediaSource)
            it.prepare()
            it.playWhenReady = true
            if (url.endsWith("mp4")) {
                //it.repeatMode = Player.REPEAT_MODE_ONE //REPEAT_MODE_ALL闪烁
            } else {
                it.repeatMode = Player.REPEAT_MODE_OFF
            }
        }
    }

    override fun getPlayState(): String {
        val state = mPlayer?.playbackState ?: 0
        return translateState(state)
    }

    override fun isPlaying(): Boolean {
        return mPlayer?.isPlaying == true || mPlayer?.playbackState == Player.STATE_READY
    }

    override fun resumePlay() {
        mPlayer?.play()
    }

    override fun pausePlay() {
        mPlayer?.pause()
    }

    private fun createMediaSourceByUrl(url: String): MediaSource {
        val isRtmpStream = url.toLowerCase(Locale.getDefault()).startsWith("rtmp://")
        val dataSourceFactory: DataSource.Factory =
            if (isRtmpStream) {
                RtmpDataSourceFactory()
            } else {
                val context = playerView.context
                DefaultDataSourceFactory(context, Util.getUserAgent(context, context.packageName))
            }

        return ProgressiveMediaSource.Factory(dataSourceFactory)
            //.setDrmSessionManager(drmSessionManager)
            .createMediaSource(MediaItem.fromUri(url))
    }

    override fun stopPlay() {
        mPlayer?.stop()
        //Log.d(TAG, "stopPlay")
    }

    override fun setVolume(audioVolume: Float) {
        mPlayer?.volume = audioVolume
    }

    override fun addCallback(callback: PlayerCallback?) {
        this.mCallback = callback
    }

    override fun onDestroy() {
        stopPlay()
        mPlayer?.release()
        mPlayer?.removeListener(playerListener)
        mPlayer = null
        Log.d(TAG, "onDestroy")
    }

    companion object {
        private const val TAG = "dq-SimpleLivePlayer"
    }
}