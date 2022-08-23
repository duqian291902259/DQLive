package com.duqian.app.live.base_comm

import androidx.databinding.ViewDataBinding
import android.view.LayoutInflater
import android.view.ViewGroup
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil

/**
 * Description:使用DataBinding的dialog基类
 *
 * Created by 杜乾 on 2022/8/13 - 22:21.
 * E-mail: duqian2010@gmail.com
 */
abstract class BaseDataBindingDialog<V : ViewDataBinding?> : BaseDialogFragment() {
    //暴露给外部调用
    open var mDataBinding: V? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        mDataBinding = DataBindingUtil.inflate(inflater, layoutId, container, true)
        return mDataBinding!!.root
    }
}