package com.hi.network.itf

import com.hi.network.bead.JokeListData
import com.hi.network.bead.JokeOneData
import com.hi.network.bead.MonthData
import com.hi.network.bead.TodayData
import com.hi.network.bead.WeekData
import com.hi.network.bead.YearData
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
