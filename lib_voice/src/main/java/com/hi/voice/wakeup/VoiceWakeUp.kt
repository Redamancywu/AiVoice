package com.hi.voice.wakeup

import android.content.Context
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.hi.voice.manager.VoiceManager
import org.json.JSONObject

/**
 * 语音唤醒
 */
object VoiceWakeUp {
    private lateinit var WakeUpJson: String
    private lateinit var wp: EventManager
    fun initWakeUp(mContext: Context, listener: EventListener) {
        val map = HashMap<Any, Any>()
        // 设置唤醒资源本地路径
        map[SpeechConstant.WP_WORDS_FILE] = "assets:///WakeUp.bin"
        //设置参数
        map[SpeechConstant.APP_ID] = VoiceManager.APP_ID
        map[SpeechConstant.APP_KEY] = VoiceManager.API_KEY
        map[SpeechConstant.SECRET] = VoiceManager.SECRET_KEY
        //是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME] = false
        //map转为json
        WakeUpJson = JSONObject(map as Map<Any, Any>).toString()
        //设置监听器
        wp = EventManagerFactory.create(mContext, "wp")
        wp.registerListener(listener)
        startWakeUp()

    }

    //开始唤醒
    fun startWakeUp() {
        wp.send(SpeechConstant.WAKEUP_START, WakeUpJson, null, 0, 0)

    }

    //停止唤醒
    fun stopWakeUp() {
        wp.send(SpeechConstant.WAKEUP_STOP, null, null, 0, 0)
    }


}