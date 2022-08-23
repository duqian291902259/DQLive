package com.duqian.app.live.viewmodel

import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.duqian.app.base.BaseApplication

/**
 * Description:App级别的ViewModel
 * 直播间数据暴露给直播间外部，使用Application级别的ViewModel
 * Created by 杜乾 on 2022/8/11 - 14:02.
 * E-mail: duqian2010@gmail.com
 */
class GlobalViewModel : AndroidViewModel(BaseApplication.instance) {
    //App级别的数据定义在这里，如：
    val isLogin = MutableLiveData<Boolean>()
}
