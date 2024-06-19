package com.hi.network.http

/**
 * 网络地址
 */
object HttpUrl {
    //天气URI
    const val WEATHER_BASE_URI = "https://api.caiyunapp.com/"
    const val WEATHER_ACTION="v2.6"
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