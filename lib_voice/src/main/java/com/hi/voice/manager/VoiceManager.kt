package com.hi.voice.manager

import android.content.Context
import android.util.Log
import com.baidu.speech.EventListener
import com.baidu.speech.asr.SpeechConstant
import com.baidu.tts.client.SpeechSynthesizer
import com.hi.voice.asr.VoiceAsr
import com.hi.voice.itf.OnAsrResultListener
import com.hi.voice.tts.VoiceTTs
import com.hi.voice.wakeup.VoiceWakeUp
import org.json.JSONObject

/**
 * 语音管理类
 */
object VoiceManager : EventListener {
    private var TAG = VoiceManager::class.java.simpleName
    const val APP_ID = "78831805"

    const val API_KEY = "vT6ZlQjDvtNXV2zZFYx3qbCV"

    const val SECRET_KEY = "nAAXcG0axOnieAdkhPcGxMNaYKcsjb0n"
    //接口对象
    private lateinit var mOnAsrResultListener: OnAsrResultListener

    //初始化
    fun initVoiceManager(mContext: Context,mOnAsrResultListener: OnAsrResultListener) {
        this.mOnAsrResultListener=mOnAsrResultListener
        VoiceTTs.initTTs(mContext)
        VoiceWakeUp.initWakeUp(mContext, this)
        VoiceAsr.initVoiceAsr(mContext, this)
    }

    //--------------------------------TTS START--------------------------------//
    //播放
    fun ttsStart(text: String) {
        Log.d(TAG, "ttsStart text=$text")
        VoiceTTs.start(text, null)
    }

    fun ttsStart(text: String, mOnTTsResultListener: VoiceTTs.OnTTsResultListener) {
        Log.d(TAG, "ttsStart text=$text")
        VoiceTTs.start(text, mOnTTsResultListener)
    }

    //停止
    fun ttsStop() {
        Log.d(TAG, "ttsStop")
        VoiceTTs.stop()
    }

    //暂停
    fun ttsPause() {
        Log.d(TAG, "ttsPause")
        VoiceTTs.pause()
    }

    //继续播放
    fun ttsResume() {
        Log.d(TAG, "ttsResume")
        VoiceTTs.resume()
    }

    //释放资源
    fun ttsRelease() {
        Log.d(TAG, "ttsRelease")
        VoiceTTs.release()
    }

    //设置发音人
    fun setVoicePeople(people: String) {
        Log.d(TAG, "setVoicePeople people=$people")
        VoiceTTs.setVoicePeople(people)
    }

    //设置语速
    fun setVoiceSpeed(speed: String) {
        Log.d(TAG, "setVoiceSpeed speed=$speed")
        VoiceTTs.setVoiceSpeed(speed)
    }

    //设置音量
    fun setVoiceVolume(volume: String) {
        Log.d(TAG, "setVoiceVolume volume=$volume")
        VoiceTTs.setVoiceVolume(volume)
    }

    //--------------------------------TTS  END--------------------------------//
    //--------------------------------WAKEUP START--------------------------------//
    //启动唤醒
    fun startWakeUp() {
        Log.d(TAG, "startWakeUp")
        VoiceWakeUp.startWakeUp()
    }

    //停止唤醒
    fun stopWakeUp() {
        Log.d(TAG, "stopWakeUp")
        VoiceWakeUp.stopWakeUp()
    }

    override fun onEvent(
        name: String?,
        params: String?,
        data: ByteArray?,
        offset: Int,
        length: Int
    ) {
        Log.e(TAG, "onEvent name:$name,params:$params,data:$data,offset:$offset,length:$length")
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_READY -> {
                //唤醒准备就绪
                mOnAsrResultListener.wakeUpReady()
            }

            SpeechConstant.CALLBACK_EVENT_ASR_BEGIN -> {//开始说话
                mOnAsrResultListener.asrStartSpeak()
            }

            SpeechConstant.CALLBACK_EVENT_ASR_END -> {//结束说话
                mOnAsrResultListener.asrStopSpeak()

            }

        }
        //去除脏数据
        if (params == null) {
            return
        }
        val allJson=JSONObject(params)
        when (name) {
            SpeechConstant.CALLBACK_EVENT_WAKEUP_SUCCESS -> mOnAsrResultListener.wakeUpSuccess(allJson)
            SpeechConstant.CALLBACK_EVENT_WAKEUP_ERROR ->{
                Log.e(TAG, "唤醒失败：$params")
                mOnAsrResultListener.voiceError("唤醒失败：$params")
            }
            SpeechConstant.CALLBACK_EVENT_ASR_READY -> {//准备就绪
                Log.d(TAG, "ASR准备就绪")
            }

            SpeechConstant.CALLBACK_EVENT_ASR_FINISH -> {
                mOnAsrResultListener.asrResult(allJson)
                //识别结束
                Log.d(TAG, "ASR识别结束：$params")
            }

            SpeechConstant.CALLBACK_EVENT_ASR_PARTIAL -> {//识别结果
                mOnAsrResultListener.updateUserText(allJson.optString("best_result"))
                data?.let {
                    val nul = JSONObject(String(data, offset, length))
                    Log.d(TAG, "ASR识别结果：$nul")
                    mOnAsrResultListener.nluResult(nul)
                }
            }

        }
        //--------------------------------WAKEUP END--------------------------------//


    }

    //--------------------------------ASR START--------------------------------//
    fun startAsr() {
        Log.d(TAG, "startAsr")
        VoiceAsr.startAsr()
    }

    fun stopAsr() {
        Log.d(TAG, "stopAsr")
        VoiceAsr.stopAsr()
    }

    fun cancelAsr() {
        Log.d(TAG, "cancelAsr")
        VoiceAsr.cancelAsr()
    }

    fun releaseAsr() {
        Log.d(TAG, "releaseAsr")
        VoiceAsr.releaseAsr(this)
    }
    //--------------------------------ASR END--------------------------------//
}




