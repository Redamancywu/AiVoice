package com.hi.network.itf

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
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

/**
 * 接口服务
 *
 */
interface HttpImplService {
    @GET("{action}/{key}/{location}/realtime")
    fun getWeather(
        @Path("action") action: String,
        @Path("key") key: String,
        @Path("location") location: String
    ): Call<ResponseBody>
    //==============================天气=============================//
    /**
     * 城市搜索
     *
     * @param location 要搜索的城市
     * @return 城市信息网络请求回调
     */
//    @GET("v7/weather/now?key=${HttpKey.WEATHER_TOKEN}")
//    fun searchLocations(@Query("location") location: String): Call<WeatherData>
    @GET("v2.6/token${HttpKey.WEATHER_TOKEN}/realtime")
    fun searchLocations(@Query("location") location: String): Call<WeatherData>
    /**
     * 实时天气查询
     *
     * @param locationID 要查询的城市ID
     * @return 实时天气信息请求回调
     */
    @GET("v7/weather/now?key=${HttpKey.WEATHER_TOKEN}")
    fun getNowWeather(@Query("location") locationID: String): Call<WeatherData>

    /**
     * 未来一周天气查询
     *
     * @param locationID 要查询的城市ID
     * @return 未来一周天气信息请求回调
     */
    @GET("v7/weather/7d?key=${HttpKey.WEATHER_TOKEN}")
    fun getDailyWeather(@Query("location") locationID: String): Call<WeatherData>

    /**
     * 当天空气生活指数查询
     *
     * @param locationID 要查询的城市ID
     * @return 当天空气生活指数信息请求回调
     */
    @GET("v7/indices/1d?type=${HttpKey.INDICES_TYPE}&key=${HttpKey.WEATHER_TOKEN}")
    fun getIndicesWeather(@Query("location") locationID: String): Call<WeatherData>

    @GET(HttpUrl.WEATHER_ACTION)
    fun getWeather(@Query("city") city: String, @Query("key") key: String): Call<WeatherData>


    @GET(HttpUrl.WEATHER_ACTION)
    fun getWeekWeather(@Query("city") city: String, @Query("key") key: String):Call<Future>

    //==============================天气=============================//


    //==============================笑话=============================

    @GET(HttpUrl.JOKE_ONE_ACTION)
    fun queryJoke(@Query("key") key: String): Call<JokeOneData>

    @GET(HttpUrl.JOKE_LIST_ACTION)
    fun queryJokeList(
        @Query("key") key: String,
        @Query("page") page: Int,
        @Query("pagesize") pageSize: Int
    ): Call<JokeListData>

    //==============================星座=============================

    @GET(HttpUrl.CONS_TELL_ACTION)
    fun queryTodayConsTellInfo(
        @Query("consName") consName: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): Call<TodayData>

    @GET(HttpUrl.CONS_TELL_ACTION)
    fun queryWeekConsTellInfo(
        @Query("consName") consName: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): Call<WeekData>

    @GET(HttpUrl.CONS_TELL_ACTION)
    fun queryMonthConsTellInfo(
        @Query("consName") consName: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): Call<MonthData>

    @GET(HttpUrl.CONS_TELL_ACTION)
    fun queryYearConsTellInfo(
        @Query("consName") consName: String,
        @Query("type") type: String,
        @Query("key") key: String
    ): Call<YearData>

}
