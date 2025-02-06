package com.example.test

interface SearchCityCallback {

    //得到经纬度,区县名
    fun getLatLon(latitude:String,longitude:String,district:String){
    }

}