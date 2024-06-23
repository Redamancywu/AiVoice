package com.hi.base.utils

import android.content.Context
import android.content.res.AssetManager
import com.google.gson.Gson
import com.hi.base.data.City

import java.nio.charset.Charset


/**
 * FileName: AssetsUtils
 * Founder: LiuGuiLin
 * Profile:
 */
object AssetsUtils {

    private lateinit var assets: AssetManager
    private lateinit var mGson: Gson

    private lateinit var city: City

    fun initUtils(mContext: Context) {
        assets = mContext.assets
        mGson = Gson()
        city = mGson.fromJson(loadAssetsFile("city.json"), City::class.java)
    }

    //获取城市数据
    fun getCity(): City {
        return city
    }

    //读取资源文件
    private fun loadAssetsFile(path: String): String {
        val input = assets.open(path)
        val buffer = ByteArray(input.available())
        input.read(buffer)
        input.close()
        return String(buffer, Charset.forName("utf-8"))
    }
}