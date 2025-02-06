package com.example.coolweather.logic.network


import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.amap.api.location.AMapLocationClient
import com.amap.api.location.AMapLocationClientOption
import com.amap.api.location.AMapLocationListener
import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.UI.searchCity.SearchCityCallback
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Request {

    //这个是搜索城市的
    private const val SEARCHCITY_BASEURL = "https://geoapi.qweather.com/v2/city/"
    private val searchCity_retrofit = Retrofit.Builder()
        .baseUrl(SEARCHCITY_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private fun <T> searchCityCreate(weatherService:Class<T>):T= searchCity_retrofit.create(weatherService)
    private val searchCityCreator= searchCityCreate(SearchCityService::class.java)
    //返回一个SearchCityResponse的数据
    suspend fun searchCity(cityName:String)= searchCityCreator.searchCity(cityName).await()


    //这个是ShowWeather的
    private const val SHOWWEATHER_BASEURL = "https://devapi.qweather.com/v7/"
    private val showWeather_retrofit = Retrofit.Builder()
        .baseUrl(SHOWWEATHER_BASEURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    //这里填入自己创建的网络接口
    private fun <T> showWeatherCreate(weatherService:Class<T>):T= showWeather_retrofit.create(weatherService)
    private val showWeatherCreator= showWeatherCreate(ShowWeatherService::class.java)
    suspend fun getNow(location: String)= showWeatherCreator.getNow(location).await()
    suspend fun getThreeDay(location: String)= showWeatherCreator.getThreeDay(location).await()
    suspend fun getAqi(location: String)= showWeatherCreator.getAqi(location).await()
    suspend fun getWarning(location: String)= showWeatherCreator.getWarning(location).await()
    suspend fun getSuggestion(location: String)= showWeatherCreator.getSuggestion(location).await()



    //如下是对enqueue（）后面部分进行封装
    private suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {comtinuation->
            enqueue(object : Callback<T> {
                override fun onResponse(call: Call<T>, response: Response<T>) {
                    val body=response.body()
                    if (body!=null) comtinuation.resume(body)
                    else comtinuation.resumeWithException(Throwable("response body is null"))
                }

                override fun onFailure(call: Call<T>, t: Throwable) {
                    comtinuation.resumeWithException(t)
                }
            })
        }
    }

    fun initLocation(activity: Activity,searchCityCallback: SearchCityCallback) {
        if (ContextCompat.checkSelfPermission(CoolWeatherApplication.context,
                android.Manifest.permission.ACCESS_FINE_LOCATION)!= PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 1)
        }
        //声明AMapLocationClient类对象
        var mLocationClient: AMapLocationClient? = null

        //声明定位回调监听器
        val mLocationListener = AMapLocationListener{amapLocation->
            if (amapLocation != null) {
                if (amapLocation.errorCode == 0) {
                    //可在其中解析amapLocation获取相应内容。
                    searchCityCallback.getLatLon(amapLocation.latitude.toString(),
                        amapLocation.longitude.toString(),
                        amapLocation.district)
                }else {
                    //定位失败时，可通过ErrCode（错误码）信息来确定失败的原因，errInfo是错误信息，详见错误码表。
                    searchCityCallback.getFailure("location Error, ErrCode:"
                            + amapLocation.errorCode + ", errInfo:"
                            + amapLocation.errorInfo
                    )
                }
            }
        }

        //初始化定位
        mLocationClient = AMapLocationClient(CoolWeatherApplication.context)
        //声明AMapLocationClientOption对象
        var mLocationOption: AMapLocationClientOption? = null
        //初始化AMapLocationClientOption对象
        mLocationOption = AMapLocationClientOption()
        //设置定位模式为AMapLocationMode.Hight_Accuracy，高精度模式。
        mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
        //获取一次定位结果：
        //该方法默认为false。
        mLocationOption.setOnceLocation(true);
        //获取最近3s内精度最高的一次定位结果：
        //设置setOnceLocationLatest(boolean b)接口为true，启动定位时SDK会返回最近3s内精度最高的一次定位结果。如果设置其为true，setOnceLocation(boolean b)接口也会被设置为true，反之不会，默认为false。
        mLocationOption.setOnceLocationLatest(true);
        //设置定位间隔,单位毫秒,默认为2000ms，最低1000ms。
        mLocationOption.setInterval(1000);
        //设置是否返回地址信息（默认返回地址信息）
        mLocationOption.setNeedAddress(true);
        //给定位客户端对象设置定位参数
        mLocationClient.setLocationOption(mLocationOption);
        //启动定位
        mLocationClient.startLocation();
        //设置定位回调监听
        mLocationClient.setLocationListener(mLocationListener)

    }


}