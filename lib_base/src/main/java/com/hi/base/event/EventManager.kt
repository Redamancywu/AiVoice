package com.hi.base.event

import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode




/**
 * EventBus事件管理类
 */
object EventManager {
    /**
     * 注册
     */
    fun register(event: Any) {
        EventBus.getDefault().register(event)
    }

    /**
     * 取消注册
     */
    fun unregister(event: Any) {
        EventBus.getDefault().unregister(event)
    }
    /**
     * 发送事件类
     */
   private fun post(event: MessageEvent) {
        EventBus.getDefault().post(event)
    }
    /**
     * 发送事件类型
     */
    fun post(eventType: Int) {
        post(MessageEvent(eventType))
    }

    /**
     * 发送类型 携带string
     */
    fun post(eventType: Int, str: String) {
        val event=MessageEvent(eventType)
        event.stringValue=str
        post(event)
    }
    /**
     * 发送类型 携带int
     */
    fun post(eventType: Int, intValue: Int) {
        val event=MessageEvent(eventType)
        event.intValue=intValue
        post(event)
    }
    /**
     * 发送类型 携带boolean
     */
    fun post(eventType: Int, boolValue: Boolean) {
        val  event=MessageEvent(eventType)
        event.booleanValue=boolValue
        post(event)
    }


}