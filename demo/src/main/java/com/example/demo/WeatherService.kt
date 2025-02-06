package com.example.demo

import android.telecom.Call
import retrofit2.http.GET

interface WeatherService {

    @GET("3d?location=101010100&key=50f12b7a8c974cdd91b2ba876abf19f4")
    fun getWeatherData():retrofit2.Call<MyResponse>
}