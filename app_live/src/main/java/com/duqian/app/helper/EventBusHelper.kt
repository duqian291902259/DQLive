package com.duqian.app.helper

import com.duqian.app.live.base_comm.BaseEvent
import org.greenrobot.eventbus.EventBus

@Target(AnnotationTarget.ANNOTATION_CLASS, AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class RegisterEvent

object EventBusHelper {
    private val eventBus = EventBus.getDefault()

    /**
     * 新版，发送事件
     */
    fun sendEvent(event: BaseEvent) {
        eventBus.post(event)
    }

    fun notifyEvent(obj: Any?) {
        eventBus.post(obj)
    }

    fun notifyStickyEvent(obj: Any?) {
        eventBus.postSticky(obj)
    }

    fun bind(obj: Any): EventBusHelper {
        val clazz: Class<*> = obj.javaClass
        if (clazz.isAnnotationPresent(RegisterEvent::class.java)) {
            if (!eventBus.isRegistered(obj)) {
                eventBus.register(obj)
            }
        }
        return this
    }

    fun unbind(obj: Any): EventBusHelper {
        if (eventBus.isRegistered(obj)) {
            eventBus.unregister(obj)
        }
        return this
    }
}