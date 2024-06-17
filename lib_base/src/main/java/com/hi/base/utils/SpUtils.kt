package com.hi.base.utils

import android.content.Context
import android.content.SharedPreferences

/**
 * 封装SharedPreferences
 */
object SpUtils {
    private const val SP_NAME = "Voice_config"
    private lateinit var sp: SharedPreferences
    private lateinit var editor: SharedPreferences.Editor
    fun intSpUtils(mContext: Context) {
        sp = mContext.getSharedPreferences(SP_NAME, Context.MODE_PRIVATE)
        editor = sp.edit()
        editor.apply()

    }

    fun putString(key: String, value: String) {
        editor.putString(key, value)
        editor.apply()
    }

    fun getString(key: String, defValue: String): String? {
        return sp.getString(key, defValue)!!
    }

    fun putInt(key: String, value: Int) {
        editor.putInt(key, value)
        editor.apply()
    }

    fun getInt(key: String, defValue: Int): Int {
        return sp.getInt(key, defValue)
    }

    fun putBoolean(key: String, value: Boolean) {
        editor.putBoolean(key, value)
        editor.apply()
    }

    fun getBoolean(key: String, defValue: Boolean): Boolean {
        return sp.getBoolean(key, defValue)
    }


}