package com.duqian.app.live.fragment.dialog

import android.os.Bundle
import android.os.Parcelable
import com.duqian.app.live.R
import com.duqian.app.live.databinding.DialogRoomGiftLayoutBinding
import com.duqian.app.live.base_comm.BaseDataBindingDialog
import com.duqian.app.live.base_comm.ParamKey

/**
 * Description:送礼弹窗
 *
 * Created by 杜乾 on 2022/8/13 - 22:29.
 * E-mail: duqian2010@gmail.com
 */
class RoomGiftDialog : BaseDataBindingDialog<DialogRoomGiftLayoutBinding>() {

    override val layoutId: Int
        get() = R.layout.dialog_room_gift_layout

    override fun iniView() {

    }

    override fun iniData() {
    }

    var callback: DialogCallback? = null

    interface DialogCallback {
        fun onClicked()
    }

    override fun iniListener() {
        mDataBinding?.ivDialogImg?.setOnClickListener {
            callback?.onClicked()
        }
    }

    companion object {
        /**
         * 传入的参数，按需扩展，
         */
        fun newInstance(data: Parcelable? = null): RoomGiftDialog {
            val fragment = RoomGiftDialog()
            val bundle = Bundle()
            bundle.putParcelable(ParamKey.PARAM_KEY_DIALOG_DATA, data)
            fragment.arguments = bundle
            return fragment
        }
    }
}