package com.duqian.app.live.services

import android.app.Application
import java.util.*

/**
 * Description:AutoService的load工具方法,todo-dq 处理有多种接口实现的get
 *
 * Created by 杜乾 on 2022/8/14 - 00:28.
 * E-mail: duqian2010@gmail.com
 */
object AutoServiceHelper {

    fun <S> load(clazz: Class<S>): S? {
        val service = ServiceLoader.load(clazz).iterator()
        try {
            if (service.hasNext()) {
                return service.next()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return null
    }

    fun <S> load(app: Application, service: Class<S>): ServiceLoader<S> {
        return ServiceLoader.load(service, app.classLoader)
    }

    fun <S> loadFirst(app: Application, service: Class<S>): S {
        return load(app, service).iterator().next()
    }
}