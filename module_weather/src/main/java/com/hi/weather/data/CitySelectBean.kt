package com.hi.weather.data


/**
 * FileName: CitySelectBean
 * Founder: LiuGuiLin
 * Profile:
 */
data class CitySelectBean(
    val type: Int,
    val title: String,
    val content: String,
    val city: String,
    val province: String
)
data class WeatherWid(val day: String, val night: String)
data class FutureWeather(val date: String, val temperature: String, var weather: String, val wid: WeatherWid, val direct: String)
data class RealtimeWeather(val temperature: String, val humidity: String, val info: String, val wid: String, val direct: String, val power: String, val aqi: String)
data class WeatherResult(val city: String, val realtime: RealtimeWeather, val future: List<FutureWeather>)
data class WeatherResponse(val reason: String, val result: WeatherResult, val error_code: Int)