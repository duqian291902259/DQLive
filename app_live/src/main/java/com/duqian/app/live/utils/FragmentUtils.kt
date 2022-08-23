package com.duqian.app.live.utils

import androidx.fragment.app.FragmentActivity
import android.app.Activity
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager

/**
 * Description:Fragment工具类
 * Created by 杜乾 on 2022/8/10 - 15:26.
 * E-mail: duqian2010@gmail.com
 */
object FragmentUtils {

    private const val TAG = "FragmentUtils"

    fun show(activity: FragmentActivity?, dialogFragment: DialogFragment?) {
        if (activity != null) {
            show(activity, activity.supportFragmentManager, false, dialogFragment)
        }
    }

    fun show(
        activity: Activity?, fragmentMgr: FragmentManager?, ignoreShowOnce: Boolean,
        dialogFragment: DialogFragment?
    ) {
        if (dialogFragment == null) {
            return
        }
        //val tag = dialogFragment.javaClass.simpleName
        val tag = dialogFragment::class.java.simpleName
        show(activity, fragmentMgr, ignoreShowOnce, dialogFragment, tag)
    }

    fun show(
        activity: Activity?,
        manager: FragmentManager?,
        fragment: DialogFragment,
        tag: String?
    ) {
        show(activity, manager, false, fragment, tag)
    }

    fun show(
        activity: Activity?,
        manager: FragmentManager?,
        ignoreShowOnce: Boolean,
        fragment: DialogFragment,
        tag: String?
    ) {
        //ignoreShowOnce,忽略只能展示一次
        if (activity == null || manager == null ||
            !ignoreShowOnce && (manager.findFragmentByTag(tag) != null || fragment.isAdded)
        ) {
            return
        }
        showDialogFragmentByCommitAllowingStateLoss(activity, manager, fragment, tag)
    }

    fun dismissAllowingStateLoss(fragmentMgr: FragmentManager?, tag: String?) {
        if (fragmentMgr != null) {
            val fragment = fragmentMgr.findFragmentByTag(tag)
            if (fragment is DialogFragment) {
                fragment.dismissAllowingStateLoss()
            }
        }
    }

    /**
     * 关闭DialogFragment（全局DialogFragment属性使用时，可以先dismiss再重新show避免出现：“java.lang.IllegalStateException: Fragment already
     * added:”）
     */
    fun dismissAllowingStateLoss(fragment: DialogFragment?): Boolean {
        val needDismiss = fragment != null && fragment.fragmentManager != null
        if (needDismiss) {
            fragment!!.dismissAllowingStateLoss()
        }
        return needDismiss
    }

    fun findFragmentByTag(fragmentActivity: FragmentActivity, tag: String?): Boolean {
        return fragmentActivity.supportFragmentManager.findFragmentByTag(tag) != null
    }

    fun findFragmentByTag(fm: FragmentManager, tag: String?): Fragment? {
        return fm.findFragmentByTag(tag)
    }

    fun <T> findByTag(fm: FragmentManager?, clazz: Class<T>): T? {
        val tag = clazz.simpleName
        return if (fm == null) {
            null
        } else fm.findFragmentByTag(tag) as T?
    }

    fun hasFragmentByTag(fragmentActivity: FragmentActivity, tag: String): Boolean {
        return fragmentActivity.supportFragmentManager.findFragmentByTag(tag) != null
    }

    fun isFragmentShowing(fragmentMgr: FragmentManager?, tag: String?): Boolean {
        if (fragmentMgr?.findFragmentByTag(tag) != null) { //instanceof DialogFragment
            val fragment = fragmentMgr.findFragmentByTag(tag)
            return fragment != null && fragment.isAdded
        }
        return false
    }

    fun addFragment(fm: FragmentManager?, layoutId: Int, fragment: Fragment?) {
        if (fm == null || fragment == null) {
            return
        }
        fm.beginTransaction().replace(layoutId, fragment, fragment.javaClass.simpleName)
            .commitAllowingStateLoss()
    }

    fun removeFragment(fm: FragmentManager?, fragment: Fragment?) {
        try {
            if (fm == null || fragment == null || !fragment.isAdded) {
                return
            }
            fm.beginTransaction().remove(fragment).commitAllowingStateLoss()
        } catch (e: Exception) {
            Log.e(TAG, "removeFragment error $e")
        }
    }

    fun removeFragmentByTag(fm: FragmentManager?, fragmentTag: String? = "") {
        if (TextUtils.isEmpty(fragmentTag)) return
        val fragmentByTag =
            fm?.findFragmentByTag(fragmentTag)
        removeFragment(fm, fragmentByTag)
    }

    /**
     * 直接show，可能报java.lang.IllegalStateException: Can not perform this action after onSaveInstanceState
     * 关闭DialogFragment，推荐使用commitAllowingStateLoss()方法
     */
    private fun showDialogFragmentByCommitAllowingStateLoss(
        activity: Activity?, fm: FragmentManager?,
        dialogFragment: DialogFragment?, tag: String?
    ) {
        if (fm != null && dialogFragment != null &&
            tag != null && !UIUtil.isActivityIllegal(activity)
        ) {
            val ft = fm.beginTransaction()
            ft.add(dialogFragment, tag)
            ft.commitAllowingStateLoss()
            if (UIUtil.isOnUiThread) {
                fm.executePendingTransactions()
            } else {
                activity?.runOnUiThread { fm.executePendingTransactions() }
            }
        }
    }
}