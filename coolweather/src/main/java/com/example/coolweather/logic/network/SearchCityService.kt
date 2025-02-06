package com.example.coolweather.logic.network

import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.logic.networkModel.SearchCityResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query


interface SearchCityService {

    @GET("lookup?range=cn&number=20&key=${CoolWeatherApplication.TOKEN}")
    fun searchCity(@Query("location")location:String):Call<SearchCityResponse>

}