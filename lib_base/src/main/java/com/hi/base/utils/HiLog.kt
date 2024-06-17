package com.hi.base.utils

import android.util.Log
import com.hi.base.BuildConfig

/**
 * 日志
 */
object HiLog {
   private const val TAG="AiVoiceApp"
    fun i(msg:String){
        if (BuildConfig.DEBUG){
            msg.let {
                Log.i(TAG,it)
            }
        }
    }
    fun d(msg:String){
        if (BuildConfig.DEBUG){
            msg.let {
                Log.d(TAG,it)
            }
        }
    }

    fun e(msg:String){
        if (BuildConfig.DEBUG){
            msg.let {
                Log.e(TAG,it)
            }
        }
    }
    fun w(msg:String){
        if (BuildConfig.DEBUG){
            msg.let {
                Log.w(TAG,it)
            }
        }
    }
    fun v(msg:String){
        if (BuildConfig.DEBUG){
            msg.let {
                Log.v(TAG,it)
            }
        }
    }
}