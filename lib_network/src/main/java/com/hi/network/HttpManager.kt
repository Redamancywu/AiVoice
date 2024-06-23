package com.hi.network

import HttpInterceptor
import com.hi.network.bean.Future
import com.hi.network.bean.FutureWeather
import com.hi.network.bean.JokeListData
import com.hi.network.bean.JokeOneData
import com.hi.network.bean.MonthData
import com.hi.network.bean.TodayData
import com.hi.network.bean.WeatherData
import com.hi.network.bean.WeekData
import com.hi.network.bean.YearData
import com.hi.network.http.HttpKey
import com.hi.network.http.HttpUrl
import com.hi.network.itf.HttpImplService
import okhttp3.OkHttpClient
import retrofit2.Callback
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * 对外的网络管理类
 */
object HttpManager {
    private const val PAGE_SIZE = 20

    const val JSON = "Content-type:application/json;charset=UTF-8"

    //网络请求对象
    private fun getOkHttpClient(): OkHttpClient {
        return OkHttpClient.Builder().addInterceptor(HttpInterceptor()).build()
    }

    //查询城市天气
    private val queryLocalWeather by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(HttpUrl.WEATHER_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    //天气
//    private val retrofitWeather by lazy {
//        Retrofit.Builder()
//            .client(getOkHttpClient())
//            .baseUrl(HttpUrl.BASE_WEATHER_URL)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
    //天气接口对象
    private val apiWeather by lazy {
        queryLocalWeather.create(HttpImplService::class.java)
    }

    //查询天气
    fun queryWeather(city: String, callback: Callback<WeatherData>) {
        apiWeather.getWeather(city, HttpKey.WEATHER_KEY).enqueue(callback)
    }
    //未来七天
    fun queryWeekWeather(city: String, callback: Callback<Future>) {
        apiWeather.getWeekWeather(city, HttpKey.WEATHER_KEY).enqueue(callback)
    }




    //============================笑话============================

    //笑话对象
    private val retrofitJoke by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(HttpUrl.JOKE_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //笑话接口对象
    private val apiJoke by lazy {
        retrofitJoke.create(HttpImplService::class.java)
    }

    //查询笑话
    fun queryJoke(callback: Callback<JokeOneData>) {
        apiJoke.queryJoke(HttpKey.JOKE_KEY).enqueue(callback)
    }

    //查询笑话列表
    fun queryJokeList(page: Int, callback: Callback<JokeListData>) {
        apiJoke.queryJokeList(HttpKey.JOKE_KEY, page, PAGE_SIZE).enqueue(callback)
    }

    //============================笑话============================
    //============================星座============================

    //星座对象
    private val retrofitConsTell by lazy {
        Retrofit.Builder()
            .client(getOkHttpClient())
            .baseUrl(HttpUrl.CONS_TELL_BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    //星座接口对象
    private val apiConsTell by lazy {
        retrofitConsTell.create(HttpImplService::class.java)
    }

    //查询今天星座详情
    fun queryTodayConsTellInfo(name: String, callback: Callback<TodayData>) {
        apiConsTell.queryTodayConsTellInfo(name, "today", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    //查询明天星座详情
    fun queryTomorrowConsTellInfo(name: String, callback: Callback<TodayData>) {
        apiConsTell.queryTodayConsTellInfo(name, "tomorrow", HttpKey.CONS_TELL_KEY)
            .enqueue(callback)
    }

    //查询本周星座详情
    fun queryWeekConsTellInfo(name: String, callback: Callback<WeekData>) {
        apiConsTell.queryWeekConsTellInfo(name, "week", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    //查询本月星座详情
    fun queryMonthConsTellInfo(name: String, callback: Callback<MonthData>) {
        apiConsTell.queryMonthConsTellInfo(name, "month", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    //查询今年星座详情
    fun queryYearConsTellInfo(name: String, callback: Callback<YearData>) {
        apiConsTell.queryYearConsTellInfo(name, "year", HttpKey.CONS_TELL_KEY).enqueue(callback)
    }

    //============================星座============================
}