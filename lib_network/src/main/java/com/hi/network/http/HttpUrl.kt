package com.hi.network.http

/**
 * 网络地址
 */
object HttpUrl {
    //城市查询全局
    //const val WEATHER_BASE_URI = "https://geoapi.qweather.com/"
   // const val WEATHER_BASE_URI = "https://api.caiyunapp.com/v2.6/"
    //天气
    const val WEATHER_BASE_URL = "http://apis.juhe.cn/"

    //天气查询
    const val WEATHER_ACTION = "simpleWeather/query"


    //天气全局URL
    const val BASE_WEATHER_URL = "https://devapi.qweather.com/"

    //随机笑话
    const val JOKE_BASE_URL = "https://v.juhe.cn/"
    const val JOKE_ONE_ACTION = "joke/randJoke.php"

    //笑话列表
    const val JOKE_LIST_ACTION = "joke/content/text.php"

    //星座
    const val CONS_TELL_BASE_URL = "http://web.juhe.cn:8080/"

    //星座详情
    const val CONS_TELL_ACTION = "constellation/getAll"
}