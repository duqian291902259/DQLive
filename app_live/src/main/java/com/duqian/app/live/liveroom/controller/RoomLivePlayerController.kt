package com.duqian.app.live.liveroom.controller

import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.view.animation.LinearInterpolator
import androidx.lifecycle.LifecycleOwner
import com.duqian.app.live.R
import com.duqian.app.live.base_comm.BaseController
import com.duqian.app.live.base_comm.LiveEventKey
import com.duqian.app.live.services.AutoServiceHelper
import com.duqian.app.live.utils.LiveEventBus
import com.duqian.app.live.utils.setViewVisible
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.ui.PlayerView
import com.duqian.live.mediaplayer.ILivePlayerService
import com.duqian.live.mediaplayer.PlayerCallback
import java.lang.Exception

/**
 * Description:直播间拉流播放器处理逻辑
 *
 * Created by 杜乾 on 2022/8/12 - 14:43.
 * E-mail: duqian2010@gmail.com
 */
class RoomLivePlayerController(view: View, owner: LifecycleOwner) : BaseController(view, owner) {

    private lateinit var rootPlayerView: View
    private lateinit var playerView: PlayerView
    private lateinit var rootRoomLoading: View
    private lateinit var ivLoading: View
    private val playerService = AutoServiceHelper.load(ILivePlayerService::class.java)

    override fun initView(rootView: View) {
        rootPlayerView = rootView.findViewById(R.id.rootPlayerView)
        playerView = mRootView.findViewById(R.id.playerView)
        rootRoomLoading = mRootView.findViewById(R.id.rootRoomLoading)

        mGlobalViewModel?.isRoomLoading?.value = true

        handleLoadingDelay()
    }

    private fun handleLoadingDelay() {
        //loading
        showLoading()
        runOnUiThreadDelay({
            ivLoading.clearAnimation()
            ivLoading.setViewVisible(false)
            mGlobalViewModel?.isRoomLoading?.postValue(false)

            val player = playerView.player ?: return@runOnUiThreadDelay
            if (player.playbackState == Player.STATE_READY) {
                Log.d(TAG, "playing live stream")
                rootPlayerView.setViewVisible(true)
            } else if (player.playbackState == Player.STATE_IDLE) {
                Log.d(TAG, "playing live stream")
            } else {
                Log.d(TAG, "Pull stream failed")
            }
        }, 3 * 1000)
    }

    private fun showLoading() {
        playerView.setViewVisible(false)
        rootRoomLoading.setViewVisible(true)
        ivLoading = mRootView.findViewById(R.id.ivLoading)
        val animation = AnimationUtils.loadAnimation(mContext, R.anim.loading)
        animation.interpolator = LinearInterpolator()
        ivLoading.setViewVisible(true)
        ivLoading.startAnimation(animation)
    }

    override fun initData() {
        super.initData()
        initPlayer()
    }

    private fun initPlayer() {
        //player
        playerService?.initPlayer(mContext, playerView, rootRoomLoading)
        playerService?.startPlay("rtmp://examplepull.agoramdn.com/live/duqian")

        playerService?.addCallback(object : PlayerCallback {
            override fun onLoadingChanged(isLoading: Boolean) {
                if (isLoading) {
                    rootRoomLoading.setViewVisible(true)
                }
            }

            override fun onPlayerStateChanged(playbackState: Int) {
                if (playbackState == Player.STATE_READY) {
                    handlePullSuccess()
                } else {
                    rootRoomLoading.setViewVisible(false)
                    Log.d(TAG, "Pull stream failed,playbackState=$playbackState")
                }
            }

            override fun onPlayerError(e: Exception?) {
                mGlobalViewModel?.isRoomLoading?.postValue(false)
                rootRoomLoading.setViewVisible(false)
            }
        })
    }

    private fun handlePullSuccess() {
        rootRoomLoading.setViewVisible(false)
        playerView.setViewVisible(true)
        mGlobalViewModel?.isRoomLoading?.postValue(false)
        ivLoading.clearAnimation()
        ivLoading.setViewVisible(false)
        Log.d(TAG, "Pull stream STATE_READY")
    }

    override fun onResume() {
        if (!isTestMp4) {
            playerService?.startPlay("rtmp://examplepull.agoramdn.com/live/duqian")
        }
    }

    private var isTestMp4 = false
    override fun observerData() {
        super.observerData()
        //todo-dq remove 监听
        LiveEventBus.get().with(LiveEventKey.EVENT_KEY_SEND_GIFT_START, Boolean::class.java)
            .observe(mLifecycleOwner) {
                isTestMp4 = it
                if (it)
                    testLocalMp4()
            }
    }

    private fun testLocalMp4() {
        playerView.setViewVisible(true)
        rootRoomLoading.setViewVisible(false)
        //timeRecords is at its maximum size[64]. Ignore this when unittesting.Source error.
        playerService?.initPlayer(mContext, playerView, null)
        playerService?.startPlay("/sdcard/gift.mp4")
    }

    override fun onDestroy() {
        playerService?.onDestroy()
    }

    companion object {
        private const val TAG = "RoomLivePlayerController-dq"
    }
}