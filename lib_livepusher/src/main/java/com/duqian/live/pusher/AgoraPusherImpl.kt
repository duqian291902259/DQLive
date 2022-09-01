package com.duqian.live.pusher

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.text.TextUtils
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
        const val TEST_RTMP_URL = "rtmp://examplepush.agoramdn.com/live/duqian"
    }

    private var mHandler = Handler(Looper.getMainLooper())
    private val mAppId = "aa5637f7878d4668b444fc13e85309d9"
    private val mChannelName = "duqian"
    private val mUid = 20220820
    private val mToken =
        "007eJxTYLDVMtvWyM17/ojstAWVz6VuHRBvVi859bC4hH917ObpbFsVGBITTc2MzdPMLcwtUkzMzCySTExM0pINjVMtTI0NLFMseZ/wJ/eaCiY/enWThZEBAkF8NoaU0sLMxDwGBgA/oiBb"
    private var mRtcEngine: RtcEngine? = null
    private lateinit var mContext: Context
    private var mVideoContainer: ViewGroup? = null

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
            showToast(tips, false)
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

    private fun showToast(msg: String, isToast: Boolean = true) {
        mHandler.post {
            if (isToast)
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show()
        }
    }

    override fun init(context: Context, videoContainer: ViewGroup): Int {
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

                //渲染本地视频。RENDER_MODE_FIT
                it.setupLocalVideo(VideoCanvas(surfaceView, VideoCanvas.RENDER_MODE_HIDDEN, mUid))

                // 开启本地视频预览。
                it.startPreview()
            }
        } catch (e: Exception) {
            Log.e(TAG, "init onError $e")
            return ResultCode.STATUS_FAILED
        }
        return ResultCode.STATUS_OK
    }

    /**
     * push to cdn
     */
    override fun startPushToCDN(url: String?): Int {
        //val url = getUrl()
        Log.e(TAG, "init url= $url")
        val result = if (!TextUtils.isEmpty(url)) {
            mRtcEngine?.addPublishStreamUrl(url, false)
        } else {
            mRtcEngine?.removePublishStreamUrl(url)
        }
        return result ?: ResultCode.STATUS_FAILED
    }

    override fun onLifecycleChanged(event: Int) {
        if (event == LifecycleEvent.ON_PAUSE) {
            stopPreview()
        } else if (event == LifecycleEvent.ON_RESUME) {
            startPreview()
        }
    }

    override fun adjustRecordingSignalVolume(volume: Int): Int {
        return mRtcEngine?.adjustRecordingSignalVolume(volume) ?: ResultCode.STATUS_FAILED
    }

    private var mCallback: ILiveCallback? = null

    override fun setCallBack(callback: ILiveCallback?) {
        this.mCallback = callback
    }

    override fun startEchoTest(isTest: Boolean): Int {
        val result = if (isTest) {
            mRtcEngine?.startEchoTest(10)
        } else {
            mRtcEngine?.stopEchoTest()
            //mRtcEngine?.stopLastmileProbeTest()
        }
        return result ?: ResultCode.STATUS_FAILED
    }

    override fun enableAudioVideo(isVideo: Boolean, isEnable: Boolean): Int {
        val result = if (isVideo) {
            if (isEnable) mRtcEngine?.enableVideo() else mRtcEngine?.disableVideo()
        } else {
            if (isEnable) mRtcEngine?.enableAudio() else mRtcEngine?.disableAudio()
        }
        return result ?: ResultCode.STATUS_FAILED
    }

    override fun startPreview(): Int {
        val startPreview = mRtcEngine?.startPreview()
        showToast("startPreview=$startPreview")
        return startPreview ?: ResultCode.STATUS_FAILED
    }

    override fun stopPreview(): Int {
        val stopPreview = mRtcEngine?.stopPreview()
        showToast("stopPreview=$stopPreview")
        return stopPreview ?: ResultCode.STATUS_FAILED
    }

    override fun joinChannel(): Int {
        //指定用户ID，并确保其在频道内的唯一性。
        val option = ChannelMediaOptions()
        option.autoSubscribeAudio = true
        option.autoSubscribeVideo = true
        return mRtcEngine?.joinChannel(mToken, mChannelName, null, mUid) ?: ResultCode.STATUS_FAILED
    }

    override fun muteLocalAudioStream(isMute: Boolean): Int {
        return mRtcEngine?.muteLocalAudioStream(isMute) ?: ResultCode.STATUS_FAILED
    }

    override fun leaveChannel(): Int {
        return mRtcEngine?.leaveChannel() ?: ResultCode.STATUS_FAILED
    }

    override fun release() {
        //mRtcEngine?.stopLastmileProbeTest()
        mRtcEngine?.stopPreview()
        mRtcEngine?.removePublishStreamUrl(TEST_RTMP_URL)
        mRtcEngine?.leaveChannel()
        mHandler.post(RtcEngine::destroy)
        mRtcEngine = null
    }
}