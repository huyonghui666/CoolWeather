package com.example.coolweather.UI.searchCity

interface SearchCityCallback {

    //点击城市按钮触发
    fun callbackBtnClick(location:String)

    //得到经纬度,区县名
    fun getLatLon(latitude:String,longitude:String,district:String)
    fun getFailure(code:String)
}