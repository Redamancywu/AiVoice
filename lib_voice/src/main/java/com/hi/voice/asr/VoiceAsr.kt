package com.hi.voice.asr

import android.content.Context
import com.baidu.speech.EventListener
import com.baidu.speech.EventManager
import com.baidu.speech.EventManagerFactory
import com.baidu.speech.asr.SpeechConstant
import com.hi.voice.manager.VoiceManager
import com.hi.voice.wakeup.VoiceWakeUp
import org.json.JSONObject

object VoiceAsr {
    private lateinit var asr:EventManager
    private lateinit var AsrJson: String
    fun initVoiceAsr(mContext: Context,listener: EventListener){
        val map=LinkedHashMap<Any,Any>()
        map[SpeechConstant.APP_ID] = VoiceManager.APP_ID
        map[SpeechConstant.APP_KEY] = VoiceManager.API_KEY
        map[SpeechConstant.SECRET] = VoiceManager.SECRET_KEY
        map[SpeechConstant.PID]=15373
        map[SpeechConstant.DECODER]=2 //离在线策略
        map[SpeechConstant.ACCEPT_AUDIO_DATA]=false
        map[SpeechConstant.DISABLE_PUNCTUATION]=false
        //是否获取音量
        map[SpeechConstant.ACCEPT_AUDIO_VOLUME] = true
        AsrJson = JSONObject(map as Map<Any, Any>).toString()
        asr=EventManagerFactory.create(mContext,"asr")
        asr.registerListener(listener)
    }

    //开始识别
    fun startAsr(){
        asr.send(SpeechConstant.ASR_START,AsrJson,null,0,0)

    }

    //停止识别

    fun stopAsr(){
        asr.send(SpeechConstant.ASR_STOP,null,null,0,0)
    }
    //取消识别

    fun cancelAsr(){
        asr.send(SpeechConstant.ASR_CANCEL,null,null,0,0)
    }
    //销毁
    fun releaseAsr(listener: EventListener){
        asr.unregisterListener(listener)
    }

}