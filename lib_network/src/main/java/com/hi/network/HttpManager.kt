package com.hi.network

import HttpInterceptor
import com.hi.network.http.HttpKey
import com.hi.network.http.HttpUrl
import com.hi.network.itf.HttpImplService
import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 对外的网络管理类
 */
object HttpManager {
    //网络请求对象
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpInterceptor()).build()
    }

    //天气对象
  private val retrofitWeather by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URI)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //天气接口对象
    private val apiWeather by lazy {
        retrofitWeather.create(HttpImplService::class.java)
    }
    //查询天气
    fun queryWeather(location: String): Call<ResponseBody> {
        return apiWeather.getWeather(HttpUrl.WEATHER_ACTION, HttpKey.WEATHER_KEY, location)
    }
}