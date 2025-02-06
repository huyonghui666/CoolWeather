package com.example.coolweather.logic.networkModel

data class ShowWeatherThreeDayResponse(val code:String,val daily:List<Daily>)

/**
 * sunrise:日出时间，sunset：日落时间，tempMax：最高温度，tempMin：最低温度，uvIndex：紫外线，iconDay：白天天气图标，textDay：天气文本描述
 */
data class Daily(val sunrise:String,val sunset:String,val tempMax:String,val tempMin:String,
                 val uvIndex:String,val iconDay:String,val textDay:String)