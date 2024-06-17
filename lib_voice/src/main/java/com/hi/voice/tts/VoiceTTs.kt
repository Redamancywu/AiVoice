package com.hi.voice.tts

import android.content.Context
import android.util.Log
import com.baidu.tts.chainofresponsibility.logger.LoggerProxy
import com.baidu.tts.client.SpeechError
import com.baidu.tts.client.SpeechSynthesizer
import com.baidu.tts.client.SpeechSynthesizerListener
import com.baidu.tts.client.TtsMode
import com.hi.voice.manager.VoiceManager

/**
 * 百度AiTTs语音
 * 1.实现其他参数
 * 2.实现监听 播放结束
 */
object VoiceTTs : SpeechSynthesizerListener {
    private var TAG=VoiceTTs::class.java.simpleName

    /**
     * TTs对象
     */
    private lateinit var mSpeechSynthesizer:SpeechSynthesizer
    /**
     * 监听接口对象
     */
    private var mOnTTsResultListener:OnTTsResultListener?=null
    /**
     * 初始化
     */
    fun initTTs(mContext: Context) {
        LoggerProxy.printable(true); // 日志打印在logcat中
        mSpeechSynthesizer = SpeechSynthesizer.getInstance()
        mSpeechSynthesizer.setContext(mContext)
        mSpeechSynthesizer.setAppId(VoiceManager.APP_ID)
        mSpeechSynthesizer.setApiKey(VoiceManager.API_KEY, VoiceManager.SECRET_KEY)
        //设置监听
        mSpeechSynthesizer.setSpeechSynthesizerListener(this)
        //其他参数
        //发声人
       // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, "5118")
        setVoicePeople("5118")
        //语速
      //  mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED,"6")
        setVoiceVolume("5")
        //音调
       // mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME,"5")
        setVoiceSpeed("5")
        //初始化
        mSpeechSynthesizer.initTts(TtsMode.ONLINE)

    }
    //设置发音人
    fun setVoicePeople(people: String){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEAKER, people)
    }
    //设置语速
    fun setVoiceSpeed(speed: String){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_SPEED,speed)
    }
    //设置音量
    fun setVoiceVolume(volume: String){
        mSpeechSynthesizer.setParam(SpeechSynthesizer.PARAM_VOLUME,volume)
    }


    override fun onSynthesizeStart(p0: String?) {
        Log.i(TAG, "onSynthesizeStart: 合成开始")
    }

    override fun onSynthesizeDataArrived(p0: String?, p1: ByteArray?, p2: Int, p3: Int) {
        Log.i(TAG, "onSynthesizeDataArrived: 合成数据到达")
    }

    override fun onSynthesizeFinish(p0: String?) {
            Log.i(TAG, "onSynthesizeFinish: 合成结束")
    }

    override fun onSpeechStart(p0: String?) {
        Log.i(TAG, "onSpeechStart: 开始播放")
    }

    override fun onSpeechProgressChanged(p0: String?, p1: Int) {
        Log.i(TAG, "onSpeechProgressChanged: 语音合成进度改变")
    }

    override fun onSpeechFinish(p0: String?) {
        Log.i(TAG, "onSpeechFinish: 语音合成结束")
        mOnTTsResultListener?.onTTsEnd()
    }

    override fun onError(p0: String?, p1: SpeechError?) {
        Log.e(TAG, "onError: 语音合成错误:"+p1.toString())
    }
    //播放
    fun start(text:String,mOnTTsResultListener: OnTTsResultListener?){
        this.mOnTTsResultListener=mOnTTsResultListener
        mSpeechSynthesizer.speak(text)
    }
    //停止
    fun stop(){
        mSpeechSynthesizer.stop()
    }
    //暂停
    fun pause(){
        mSpeechSynthesizer.pause()
    }
    //继续播放
    fun resume(){
        mSpeechSynthesizer.resume()
    }
    //释放资源
    fun release(){
        mSpeechSynthesizer.release()
    }

    /**
     * 监听接口
     */
    interface OnTTsResultListener{
        //播放结束
        fun onTTsEnd()
    }

}