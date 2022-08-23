package com.duqian.app.base

import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.duqian.app.helper.EventBusHelper
import com.duqian.app.helper.PageRecordHelper
import com.duqian.app.live.utils.LiveEventBus

/**
 * Description:Fragment基类
 * Created by 杜乾 on 2022/8/10 - 15:00.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseFragment : Fragment() {

    companion object {
        private val TAG = "BaseFragment"
    }

    lateinit var rootView: View
    var isVisibleToUser: Boolean = false

    var mHandler = Handler(Looper.getMainLooper())

    open lateinit var mContext: Context
    open lateinit var mActivity: FragmentActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        parseParams()
    }

    open fun parseParams() {
        //解析参数
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        rootView = inflater.inflate(getLayoutId(), container, false)
        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mContext = requireContext()
        mActivity = requireActivity()
        initConfig()
        initData()
        initView()
        initListener()
        observerData()
        initControllers()
    }

    /**
     * 初始化宿主fragment的所有逻辑之后，开始初始化子模块,非必须实现
     */
    open fun initControllers() {}

    override fun onStart() {
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        // onResume并不代表fragment可见
        // 如果是在viewpager里，就需要判断getUserVisibleHint，不在viewpager时，getUserVisibleHint默认为true
        // 如果是其它情况，就通过isHidden判断，因为show/hide时会改变isHidden的状态
        // 所以，只有当fragment原来是可见状态时，进入onResume就回调onVisible
        if (userVisibleHint && !isHidden) {
            onVisible()
        }
    }

    override fun onPause() {
        super.onPause()
        // onPause时也需要判断，如果当前fragment在viewpager中不可见，就已经回调过了，onPause时也就不需要再次回调onInvisible了
        // 所以，只有当fragment是可见状态时进入onPause才加调onInvisible
        if (userVisibleHint && !isHidden) {
            onInvisible()
        }
    }

    override fun onStop() {
        super.onStop()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        EventBusHelper.unbind(this)
        mHandler.removeCallbacksAndMessages(null)
        //确保去除粘性事件
        LiveEventBus.get().clear()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onDetach() {
        super.onDetach()
    }

    private fun initConfig() {
        EventBusHelper.bind(this)
    }

    abstract fun getLayoutId(): Int

    abstract fun initData()

    abstract fun initView()

    abstract fun initListener()

    abstract fun observerData()

    /**
     * 当fragment与viewpager、FragmentPagerAdapter一起使用时，切换页面时会调用此方法
     *
     * @param isVisibleToUser 是否对用户可见
     */
    override fun setUserVisibleHint(isVisibleToUser: Boolean) {
        val change = isVisibleToUser != userVisibleHint
        super.setUserVisibleHint(isVisibleToUser)
        // 在viewpager中，创建fragment时就会调用这个方法，但这时还没有resume，为了避免重复调用visible和invisible，
        // 只有当fragment状态是resumed并且初始化完毕后才进行visible和invisible的回调
        if (isResumed && change) {
            if (userVisibleHint) {
                onVisible()
            } else {
                onInvisible()
            }
        }
    }

    /**
     * 当使用show/hide方法时，会触发此回调
     *
     * @param hidden fragment是否被隐藏
     */
    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (hidden) {
            onInvisible()
        } else {
            onVisible()
        }
    }

    @CallSuper
    open fun onVisible() {
        isVisibleToUser = true
        if (isRecordPage()) {
            PageRecordHelper.record(getPageCode())
        }
    }

    @CallSuper
    open fun onInvisible() {
        isVisibleToUser = false
    }

    //该界面的编号
    open fun getPageCode(): Int {
        return -1
    }

    //是否记录该界面
    open fun isRecordPage(): Boolean {
        return false
    }

}