package com.duqian.live.pusher

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import com.google.auto.service.AutoService
import com.duqian.live.pusher.helper.LifecycleEvent
import io.agora.rtc.Constants
import io.agora.rtc.IRtcEngineEventHandler
import io.agora.rtc.RtcEngine
import io.agora.rtc.RtcEngineConfig
import io.agora.rtc.models.ChannelMediaOptions
import io.agora.rtc.video.VideoCanvas
import io.agora.rtc.video.VideoEncoderConfiguration
import io.agora.rtc.video.VideoEncoderConfiguration.STANDARD_BITRATE

/**
 * Description:声网SDK实现的推流器
 *
 * Created by 杜乾 on 2022/8/18 - 22:30.
 * E-mail: duqian2010@gmail.com
 */
@AutoService(value = [IBasePusher::class])
class AgoraPusherImpl : IBasePusher {
    companion object {
        private const val TAG = "AgoraPusher-dq"
    }

    private var mHandler = Handler(Looper.getMainLooper())
    private val mAppId = "eeed2eb052d24a4bb13b0aa9e6d014d5"
    private val mChannelName = "duqian"
    private val mUid = 20220820
    private val mToken =
        "007eJxTYHh7o0zUZyW3dWdTUUjV7cWzeIzW3ukN4JCUmD6p+8+t7UIKDKmpqSlGqUkGpkYpRiaJJklJhsZJBomJlqlmKQaGJimmU1L/JQlv/5/ku1qfgREKQXw2hpTSwszEPAYGAMJtIyU="

    //"006eeed2eb052d24a4bb13b0aa9e6d014d5IAD1HUJUYAOrGSxe7G5fKFX+Lu7WouKyWFLzTeZjOkdvub+i/FEAAAAAEACGukDP9pPwYgEAAQD1k/Bi"
    private var mRtcEngine: RtcEngine? = null
    private lateinit var mContext: Context
    private var mVideoContainer: ViewGroup? = null
    private val AGORA_CHANNEL_PREFIX = "rtmps://examplepush.agoramdn.com/live/"

    private var mCurrentLocalVideoState = 0
    private var mCurrentErrorState = 0
    private val mRtcEventHandler: IRtcEngineEventHandler = object : IRtcEngineEventHandler() {
        // 监听频道内的远端主播，获取主播的 uid 信息。
        override fun onJoinChannelSuccess(channel: String?, uid: Int, elapsed: Int) {
            val msg = "onJoinChannelSuccess=$channel,uid=$uid,elapsed=$elapsed"
            Log.d(TAG, msg)
            showToast(msg)
        }

        override fun onLeaveChannel(stats: RtcStats?) {
            val msg = "onLeaveChannel stats=$stats"
            Log.d(TAG, msg)
            showToast(msg)
        }

        override fun onLocalVideoStateChanged(localVideoState: Int, error: Int) {
            mCurrentLocalVideoState = localVideoState
            mCurrentErrorState = error
            val tips = "onLocalVideoStateChanged,localVideoState=$localVideoState, error=$error"
            Log.d(TAG, tips)
            showToast(tips)
        }

        override fun onConnectionLost() {
            Log.e(TAG, "onConnectionLost")
            showToast("onConnectionLost")
        }

        override fun onRtmpStreamingStateChanged(url: String?, state: Int, errCode: Int) {
            val tips = "onRtmpStreamingStateChanged,state=$state, errCode=$errCode,url=$url"
            Log.d(TAG, tips)
            showToast(tips)
        }

        override fun onError(err: Int) {
            //119 ERR_TOKEN_EXPIRED(109)：当前使用的 Token 过期，不再有效
            // 110 token无效或者过期
            val errMsg = "onError $err"
            Log.e(TAG, errMsg)
            showToast(errMsg)
            if (err == Constants.ERR_TOKEN_EXPIRED || err == Constants.ERR_INVALID_TOKEN) {
                mRtcEngine?.renewToken(mToken)
            }
        }

        override fun onFirstLocalVideoFrame(width: Int, height: Int, elapsed: Int) {
            Log.d(TAG, "onFirstLocalVideoFrame,width=$width,height=$height")
        }

        override fun onLastmileProbeResult(result: LastmileProbeResult?) {
            Log.d(TAG, "onLastmileProbeResult,result=$result")
        }
    }

    private fun showToast(msg: String) {
        mHandler.post {
            Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun init(context: Context, videoContainer: ViewGroup) {
        this.mVideoContainer = videoContainer
        this.mContext = context

        try {
            val config = RtcEngineConfig()
            config.mContext = context
            config.mAppId = mAppId
            config.mEventHandler = mRtcEventHandler

            mRtcEngine = RtcEngine.create(config)

            mRtcEngine?.also { it ->
                //加入频道后不需要改视频配置，以加快首帧出图的时间。
                // val videoEncoderConfiguration = PusherConfigHelper.initVideoEncoderConfiguration(4, 3)
                val videoEncoderConfiguration = VideoEncoderConfiguration(
                    VideoEncoderConfiguration.VD_640x480,
                    VideoEncoderConfiguration.FRAME_RATE.FRAME_RATE_FPS_15,
                    STANDARD_BITRATE,
                    //ORIENTATION_MODE_ADAPTIVE会导致观众端拉流画面横屏
                    VideoEncoderConfiguration.ORIENTATION_MODE.ORIENTATION_MODE_FIXED_PORTRAIT
                )

                it.setVideoEncoderConfiguration(videoEncoderConfiguration)
                //microPhone
                it.setDefaultAudioRoutetoSpeakerphone(true)
                // 直播场景下，设置频道场景为 BROADCASTING。
                it.setChannelProfile(Constants.CHANNEL_PROFILE_LIVE_BROADCASTING)
                //主播,根据场景将用户角色设置为 BROADCASTER 或 AUDIENCE。
                it.setClientRole(Constants.CLIENT_ROLE_BROADCASTER)

                // 启用视频流。
                it.enableVideo()

                //禁止音频 it.disableAudio()

                // 创建一个 SurfaceView 对象，并将其作为 FrameLayout 的子对象。不能自己new，视频会横屏
                //val surfaceView = SurfaceView(videoContainer.context)
                val surfaceView = RtcEngine.CreateRendererView(videoContainer.context)
                videoContainer.let {
                    surfaceView.setZOrderMediaOverlay(true)

                    if (it.childCount > 0) {
                        it.removeAllViews()
                    }
                    val lp = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    it.addView(surfaceView, lp)
                }

                //设置摄像头
                /*val direction = CameraCapturerConfiguration.CAMERA_DIRECTION.CAMERA_FRONT
                val dimensions = CameraCapturerConfiguration.CaptureDimensions(
                    videoContainer.measuredWidth,
                    videoContainer.measuredHeight
                )
                it.setCameraCapturerConfiguration(
                    CameraCapturerConfiguration(
                        dimensions,
                        direction
                    )
                )*/
                //渲染本地视频。RENDER_MODE_FIT
                it.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mUid))

                // 开启本地视频预览。
                it.startPreview()

            }
        } catch (e: Exception) {
            Log.e(TAG, "init onError $e")
        }
    }

    /**
     * push to cdn
     */
    override fun startPushToCDN(isPublish: Boolean) {
        val url = getUrl()
        Log.e(TAG, "init url= $url")
        if (isPublish) {
            mRtcEngine?.addPublishStreamUrl(url, false)
        } else {
            mRtcEngine?.removePublishStreamUrl(url)

        }
    }

    override fun onLifecycleChanged(event: Int) {
        if (event == LifecycleEvent.ON_PAUSE) {
            stopPreview()
            //mRtcEngine?.enableLocalVideo(false)
            //startPushToCDN(false)
        } else if (event == LifecycleEvent.ON_RESUME) {
            startPreview()
            /*if (mCurrentLocalVideoState == Constants.LOCAL_VIDEO_STREAM_ERROR_CAPTURE_FAILURE
                || mCurrentLocalVideoState == Constants.LOCAL_VIDEO_STREAM_STATE_STOPPED ||
                mCurrentErrorState == Constants.LOCAL_VIDEO_STREAM_ERROR_CAPTURE_FAILURE
            ) {
                mRtcEngine?.enableLocalVideo(false)
                mRtcEngine?.enableLocalVideo(true)
                startPushToCDN(true)
            }*/
        }
    }

    private fun getUrl(): String {
        return AGORA_CHANNEL_PREFIX + mChannelName
    }

    override fun startEchoTest(isTest: Boolean) {
        if (isTest) {
            mRtcEngine?.startEchoTest(10)
        } else {
            mRtcEngine?.stopEchoTest()
            //mRtcEngine?.stopLastmileProbeTest()
        }
    }

    override fun enableAudioVideo(isVideo: Boolean, isEnable: Boolean) {
        if (isVideo) {
            if (isEnable) mRtcEngine?.enableVideo() else mRtcEngine?.disableVideo()
        } else {
            if (isEnable) mRtcEngine?.enableAudio() else mRtcEngine?.disableAudio()
        }
    }

    override fun startPreview() {
        val startPreview = mRtcEngine?.startPreview()
        showToast("startPreview=$startPreview")
    }

    override fun stopPreview() {
        val stopPreview = mRtcEngine?.stopPreview()
        showToast("stopPreview=$stopPreview")
    }

    override fun joinChannel() {
        //指定用户ID，并确保其在频道内的唯一性。
        val option = ChannelMediaOptions()
        option.autoSubscribeAudio = true
        option.autoSubscribeVideo = true
        mRtcEngine?.joinChannel(mToken, mChannelName, null, mUid)
    }

    override fun muteLocalAudioStream(isMute: Boolean) {
        mRtcEngine?.muteLocalAudioStream(isMute)
    }

    override fun leaveChannel() {
        mRtcEngine?.leaveChannel()
    }

    override fun release() {
        //mRtcEngine?.stopLastmileProbeTest()
        mRtcEngine?.stopPreview()
        mRtcEngine?.removePublishStreamUrl(getUrl())
        mRtcEngine?.leaveChannel()
        mHandler.post(RtcEngine::destroy)
        mRtcEngine = null
    }
}