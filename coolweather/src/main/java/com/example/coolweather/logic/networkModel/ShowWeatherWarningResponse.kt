package com.example.coolweather.logic.networkModel

data class ShowWeatherWarningResponse(val code:String,val warning:List<Warning>)

/**
 *text:预警文本，title:预警标题
 */
data class Warning(val text:String,val title:String)