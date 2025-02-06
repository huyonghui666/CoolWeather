package com.example.coolweather.logic.networkModel

data class ShowWeatherAqiResponse(val code:String,val daily:List<Aqi>)

/**
 * aqi:aqi指数，category：污染程度文本
 */
data class Aqi(val aqi:String,val category:String)
