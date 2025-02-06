package com.example.coolweather

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.coolweather.logic.network.SearchCityCallback

class Test : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

       /* initLocationOption(this,this)*/
    }


   /* //初始化定位
    fun initLocationOption(activity: Activity,searchCityCallback: SearchCityCallback){
        if (ContextCompat.checkSelfPermission(CoolWeatherApplication.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }

        var mLocationClient: LocationClient
        //定位服务的客户端。宿主程序在客户端声明此类，并调用，目前只支持在主线程中启动
        try {
            mLocationClient = LocationClient(CoolWeatherApplication.context)
        } catch (e: Exception) {
            throw RuntimeException(e)
        }
        val option = LocationClientOption()
        option.locationMode = LocationClientOption.LocationMode.Hight_Accuracy // 设置定位模式
        option.setCoorType("bd09ll") // 设置坐标系为bd09ll
        option.setScanSpan(2000) // 设置扫描间隔，单位为毫秒
        option.isOpenGnss = true // 打开卫星定位
        option.setIsNeedAddress(true) // 返回地址信息
        option.setIsNeedLocationDescribe(true) // 返回位置描述信息
        mLocationClient.locOption = option
        //开始定位
        mLocationClient.start()

        //注册监听函数
        val myLocationListener = MyLocationListener(searchCityCallback)
        mLocationClient.registerLocationListener(myLocationListener)

    }


    //实现定位回调
    private class MyLocationListener(private val searchCityCallback: SearchCityCallback) : BDAbstractLocationListener() {
        override fun onReceiveLocation(location: BDLocation?) {

            //此处的BDLocation为定位结果信息类，通过它的各种get方法可获取定位相关的全部结果
            //以下只列举部分获取经纬度相关（常用）的结果信息
            //更多结果信息获取说明，请参照类参考中BDLocation类中的说明
            if (location != null) {
                //获取纬度信息
                val latitude: Double = location.getLatitude()
                //获取经度信息
                val longitude: Double = location.getLongitude()
                searchCityCallback.getLatLon(latitude.toString(),longitude.toString(),location.district)

            }
        }
    }

    override fun getLatLon(latitude: String, longitude: String, district: String) {
        Log.d("TAG", latitude)
        Log.d("TAG", district)
    }*/


}