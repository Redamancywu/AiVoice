package com.hi.voice.engine

import android.util.Log
import com.hi.voice.itf.OnNluResultListener
import com.hi.voice.words.NluWords
import org.json.JSONObject

/**
 * 语音引擎分析
 */
object VoiceEngineAnalyze {
    private var TAG = VoiceEngineAnalyze::class.java.simpleName

    private lateinit var mOnNluResultListener: OnNluResultListener
    //分析结果
    fun analyzeNlu(nlu: JSONObject,mOnNluResultListener: OnNluResultListener){
        val rawText = nlu.optString("raw_text")
        Log.i(TAG, "rawText:$rawText")
        //解析results
        val results = nlu.optJSONArray("results") ?: return
        val nluResultLength = results.length()
        when{
            nluResultLength<=0 -> return
            nluResultLength==1 -> analyzeNluSingle(results[0] as JSONObject)
        }
    }

    private fun analyzeNluSingle(results: JSONObject) {
        val domain = results.optString("domain")
        val intent = results.optString("intent")
        val slots = results.optJSONObject("slots")
        when(domain){
            NluWords.NLU_WEATHER->{
                //天气
            }
        }
    }
}