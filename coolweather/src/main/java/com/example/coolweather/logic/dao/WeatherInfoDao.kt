package com.example.coolweather.logic.dao

import android.content.SharedPreferences
import androidx.core.content.edit
import com.example.coolweather.logic.networkModel.ShowWeatherResponse

object WeatherInfoDao {

    //传入一个SharedPreferences缓存和天气信息数据
    fun saveWeatherInfo(weatherSharePreferences: SharedPreferences, showWeatherResponse:ShowWeatherResponse){
        weatherSharePreferences.edit {
            showWeatherResponse.showWeatherNowResponse.now.let {
                putString("现在温度", it.temp)
                putString("描述", it.text)
                putString("风向", it.windDir)
                putString("风力等级", it.windScale)
                putString("湿度", it.humidity)
                putString("气压", it.pressure)
                putString("体感温度", it.feelsLike)
            }
            showWeatherResponse.showWeatherThreeDayResponse.daily.get(0).let {
                putString("日出时间", it.sunrise)
                putString("日落时间", it.sunset)
                putString("最高温度", it.tempMax)
                putString("最低温度", it.tempMin)
                putString("紫外线", it.uvIndex)
            }
            //三天的天气
            showWeatherResponse.showWeatherThreeDayResponse.daily.let {
                val three_day_list=ArrayList<String>()
                for (i in it){
                    three_day_list.add(i.iconDay)
                    three_day_list.add(i.textDay)
                    three_day_list.add(i.tempMax)
                    three_day_list.add(i.tempMin)
                }

                // 使用逗号拼接字符串
                val concatenatedString = java.lang.String.join(",", three_day_list)
                putString("three_day", concatenatedString)
            }
            showWeatherResponse.showWeatherAqiResponse.daily.let {
                putString("aqi指数", it.get(0).aqi)
                putString("污染程度文本", it.get(0).category)
                val dataList=ArrayList<String>()
                //三天的污染文本
                for (i in 0..3){
                    var level=it.get(i).category.split("污")[0]
                    if (level==""){
                        //说明是良、优等
                        level = it.get(i).category
                    }
                    dataList.add(level)
                }
                // 使用逗号拼接字符串
                val level = java.lang.String.join(",", dataList)
                putString("污染程度", level)
            }
            showWeatherResponse.showWeatherWarningResponse.warning.let {
                var warning_forecast_set= HashSet<String>()
                if (it.size!=0){
                    for (i in it){
                        val warning_forecast=i.title+":"+i.text
                        warning_forecast_set.add(warning_forecast)
                    }
                }
                putStringSet("预警",warning_forecast_set)
            }
            showWeatherResponse.showWeatherSuggestionResponse.daily.let {
                if (it.size!=0){
                    val dataList=ArrayList<String>()
                    for (i in it){
                        dataList.add(i.level)
                        // 使用逗号拼接字符串
                        val level = java.lang.String.join(",", dataList)
                        putString("建议程度",level)
                    }
                }
            }
        }
    }

}