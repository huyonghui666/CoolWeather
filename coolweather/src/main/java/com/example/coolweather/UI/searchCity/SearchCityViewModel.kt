package com.example.coolweather.UI.searchCity

import android.annotation.SuppressLint
import android.app.Activity
import android.util.Log
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.example.coolweather.logic.Repository
import com.example.coolweather.logic.networkModel.Location
import com.example.coolweather.logic.networkModel.ShowWeatherResponse
import java.util.UUID
import java.util.concurrent.atomic.AtomicBoolean

class SearchCityViewModel:ViewModel() {

    //对输入的城市进行观察
    private val searchCityLiveData=MutableLiveData<String>()

    //创建一个数组对搜索到城市进行缓存，并且保证不会因为屏幕旋转而丢失
    val location =ArrayList<Location>()

    //已添加的观察
    private val btnListData=MutableLiveData<String?>()

    //点击的城市名
    var cityName:String=""

    val locationLiveData=searchCityLiveData.switchMap {searchCity->
        Repository.searchCity(searchCity)
    }

    val cityInfoLiveData: LiveData<ShowWeatherResponse?> =btnListData.switchMap { location->
        Repository.refreshWeather(location)
    }

    //修改searchCity设置
    fun searchCity(_searchCityLiveData:String){
        searchCityLiveData.value=_searchCityLiveData
    }

    //定位获取经纬度和区县名
    fun getLatLon(activity : Activity, searchCityCallback: SearchCityCallback){
        Repository.initLocation(activity,searchCityCallback)
    }

    //设置点击添加
    fun setBtn(location:String){
        btnListData.value = location
    }

    //获得button的经纬度
    fun getBtn():String{
        return btnListData.value.toString()
    }

}