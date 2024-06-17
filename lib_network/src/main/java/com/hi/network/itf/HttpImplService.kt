package com.hi.network.itf

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
}
