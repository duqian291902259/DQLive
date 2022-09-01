package com.duqian.app.live.utils

import androidx.lifecycle.*

/**
 * Description:基于LiveData的事件通信
 * Created by 杜乾 on 2022/8/11 - 11:04.
 * E-mail: duqian2010@gmail.com
 */
class LiveEventBus private constructor() {
    private val mLiveDataMap: MutableMap<String, BusMutableLiveData<Any?>>

    /**
     * 存、取LiveData
     */
    @Synchronized
    fun <T> with(key: String, clazz: Class<T>?): BusMutableLiveData<T> {
        if (!mLiveDataMap.containsKey(key)) {
            mLiveDataMap[key] = BusMutableLiveData()
        }
        return mLiveDataMap[key] as BusMutableLiveData<T>
    }

    @Synchronized
    fun with(key: String): BusMutableLiveData<Any> {
        return with(key, Any::class.java)
    }

    /**
     * 去除粘性事件,BusMutableLiveData isDisableSticky为true的都要清空
     */
    fun clear() {
        val saveMap: MutableMap<String, BusMutableLiveData<Any?>> = HashMap()
        mLiveDataMap.forEach {
            val busMutableLiveData = it.value
            if (busMutableLiveData.isDisableSticky) {
                //要移除 Log.d(TAG, "removed $busMutableLiveData")
            } else {
                saveMap[it.key] = busMutableLiveData
            }
        }
        mLiveDataMap.clear()
        if (saveMap.isNotEmpty()) {
            mLiveDataMap.putAll(saveMap)
        }
    }

    class BusMutableLiveData<T> : MutableLiveData<T>() {
        //是否需要阻止粘性事件,true则不会粘性传递
        var isDisableSticky = true

        fun observe(
            lifecycleOwner: LifecycleOwner,
            isDisableSticky: Boolean,
            observer: Observer<in T>
        ) {
            this.isDisableSticky = isDisableSticky
            observe(lifecycleOwner, observer)
        }

        //重写observe的方法
        override fun observe(lifecycleOwner: LifecycleOwner, observer: Observer<in T>) {
            if (isDisableSticky) {// 反射可能兼容性问题，避免有bug，组件onDestroy处理，保留粘性事件
                removeEventWhenDestroy(lifecycleOwner, this, observer)
                //改变observer.mLastVersion >= mVersion这个判断  然后拦截onChanged
                /*try {
                    hook(observer as Observer<T>)
                } catch (e: Exception) {
                    Log.d("dq-live", "hook Observer error $e")
                    super.observe(lifecycleOwner, observer)
                }*/
            } else {
                super.observe(lifecycleOwner, observer)
            }
            super.observe(lifecycleOwner, observer)
        }

        private fun removeEventWhenDestroy(
            lifecycleOwner: LifecycleOwner,
            busMutableLiveData: BusMutableLiveData<T>,
            observer: Observer<in T>
        ) {
            lifecycleOwner.lifecycle.addObserver(object : LifecycleEventObserver {
                override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                    if (event == Lifecycle.Event.ON_DESTROY) {
                        busMutableLiveData.removeObserver(observer)
                        lifecycleOwner.lifecycle.removeObserver(this)
                    }
                }
            })
        }

        /**
         * hook LiveData 改变一些参数
         */
        @Throws(Exception::class)
        private fun hook(observer: Observer<T>) {
            val liveDataClass = LiveData::class.java
            val mObserversField = liveDataClass.getDeclaredField("mObservers")
            mObserversField.isAccessible = true
            val mObservers = mObserversField[this]
            //获取到mObservers的get方法的反射对象
            val get = mObservers.javaClass.getDeclaredMethod("get", Any::class.java)
            get.isAccessible = true
            val invokeEntry = get.invoke(mObservers, observer)
            var observerWrapper: Any? = null
            if (invokeEntry != null && invokeEntry is Map.Entry<*, *>) {
                observerWrapper = invokeEntry.value
            }
            if (observerWrapper == null) {
                throw NullPointerException("ObserverWrapper不能为空")
            }
            //获取到ObserverWrapper的类对象
            val superclass: Class<*> = observerWrapper.javaClass.superclass
            val mLastVersionField = superclass.getDeclaredField("mLastVersion")
            mLastVersionField.isAccessible = true
            val mVersionField = liveDataClass.getDeclaredField("mVersion")
            mVersionField.isAccessible = true
            //得到mVersion在当前类中的值
            val o = mVersionField[this]
            //把它的值给mLastVersion
            mLastVersionField[observerWrapper] = o
        }
    }

    companion object {
        private val liveDataBus = LiveEventBus()
        fun get(): LiveEventBus {
            return liveDataBus
        }
    }

    init {
        mLiveDataMap = HashMap()
    }
}