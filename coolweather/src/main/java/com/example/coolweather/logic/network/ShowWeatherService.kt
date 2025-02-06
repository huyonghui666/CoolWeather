package com.example.coolweather.logic.network

import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.logic.networkModel.ShowWeatherAqiResponse
import com.example.coolweather.logic.networkModel.ShowWeatherNowResponse
import com.example.coolweather.logic.networkModel.ShowWeatherSuggestionResponse
import com.example.coolweather.logic.networkModel.ShowWeatherThreeDayResponse
import com.example.coolweather.logic.networkModel.ShowWeatherWarningResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ShowWeatherService {

    @GET("weather/now?key=${CoolWeatherApplication.TOKEN}")
    fun getNow(@Query("location")location:String):Call<ShowWeatherNowResponse>

    @GET("weather/3d?key=${CoolWeatherApplication.TOKEN}")
    fun getThreeDay(@Query("location")location:String):Call<ShowWeatherThreeDayResponse>

    @GET("air/5d?key=${CoolWeatherApplication.TOKEN}")
    fun getAqi(@Query("location")location:String):Call<ShowWeatherAqiResponse>

    @GET("warning/now?lang=en&key=${CoolWeatherApplication.TOKEN}")
    fun getWarning(@Query("location")location:String):Call<ShowWeatherWarningResponse>

    @GET("indices/1d?type=3,16,1,2,9&key=${CoolWeatherApplication.TOKEN}")
    fun getSuggestion(@Query("location")location:String):Call<ShowWeatherSuggestionResponse>

}