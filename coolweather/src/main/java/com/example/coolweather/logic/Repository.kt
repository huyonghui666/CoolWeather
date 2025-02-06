package com.example.coolweather.logic

import android.app.Activity
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.coolweather.UI.searchCity.SearchCityCallback
import com.example.coolweather.logic.network.Request
import com.example.coolweather.logic.networkModel.ShowWeatherResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

object Repository {

    //查询地址
    fun searchCity(cityName:String) = liveData(Dispatchers.IO) {
        try {
            val searchCityResponse = Request.searchCity(cityName)
            emit(searchCityResponse)
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

    //定位获取经纬度和区县名
    fun initLocation(activity: Activity,searchCityCallback: SearchCityCallback){
        Request.initLocation(activity,searchCityCallback)
    }

    //刷新天气
    fun refreshWeather(location: String?)= liveData(Dispatchers.IO) {
        try {
            if (location==null){
                emit(null)
            }else{
                var showWeatherResponse: ShowWeatherResponse? =null
                coroutineScope {
                    val showWeatherNowResponse = async {
                        Request.getNow(location)
                    }
                    val showWeatherThreeDayResponse = async {
                        Request.getThreeDay(location)
                    }
                    val showWeatherAqiResponse = async {
                        Request.getAqi(location)
                    }
                    val showWeatherWarningResponse = async {
                        Request.getWarning(location)
                    }
                    val showWeatherSuggestionResponse = async {
                        Request.getSuggestion(location)
                    }
                    val weatherNowResponse = showWeatherNowResponse.await()
                    val weatherThreeDayResponse = showWeatherThreeDayResponse.await()
                    val weatherAqiResponse = showWeatherAqiResponse.await()
                    val weatherWarningResponse = showWeatherWarningResponse.await()
                    val weatherSuggestionResponse = showWeatherSuggestionResponse.await()
                    if (weatherNowResponse.code == "200" &&
                        weatherThreeDayResponse.code == "200" &&
                        weatherAqiResponse.code == "200" &&
                        weatherWarningResponse.code == "200" &&
                        weatherSuggestionResponse.code == "200"
                    ) {
                        showWeatherResponse = ShowWeatherResponse(
                            weatherNowResponse,
                            weatherThreeDayResponse,
                            weatherAqiResponse,
                            weatherWarningResponse,
                            weatherSuggestionResponse
                        )
                    }
                    emit(showWeatherResponse)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }


}