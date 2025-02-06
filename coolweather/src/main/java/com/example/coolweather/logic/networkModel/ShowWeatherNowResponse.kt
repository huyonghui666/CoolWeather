package com.example.coolweather.logic.networkModel

data class ShowWeatherNowResponse(val code:String,val now:Now)

/**
 * temp：现在温度，text:天气描述，windDir:风向,windScale:风力等级,humidity:湿度,pressure:气压,feelsLike:体感温度
 */
data class Now(val temp:String,val text:String,val windDir:String,val windScale:String,
               val humidity:String,val pressure:String,val feelsLike:String)

