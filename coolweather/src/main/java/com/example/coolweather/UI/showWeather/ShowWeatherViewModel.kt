package com.example.coolweather.UI.showWeather

import android.app.Activity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.coolweather.UI.searchCity.SearchCityCallback
import com.example.coolweather.logic.Repository
import com.example.coolweather.logic.network.Request

class ShowWeatherViewModel:ViewModel() {
    //当地址改变
    private val locationLiveData=MutableLiveData<String>()


    val weatherLiveData=locationLiveData.switchMap {location->
        Repository.refreshWeather(location)
    }


    //修改地址
    fun setLocation(_location:String){
        locationLiveData.value=_location
    }

    //获取当前的城区的经纬度
    fun getLocation(): String? {
        return locationLiveData.value
    }

    //定位获取经纬度和区县名
    fun getLatLon(activity : Activity, searchCityCallback: SearchCityCallback){
        Repository.initLocation(activity,searchCityCallback)
    }
}