package com.example.coolweather.UI.showWeather

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.lifecycle.ViewModelProvider
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.amap.api.location.AMapLocationClient
import com.bumptech.glide.Glide
import com.example.coolweather.CoolWeatherApplication
import com.example.coolweather.R
import com.example.coolweather.UI.cityManagement.CityManagement
import com.example.coolweather.UI.searchCity.SearchCityCallback
import com.example.coolweather.UI.warning.Warning
import com.example.coolweather.logic.dao.WeatherInfoDao

class ShowWeatherActivity : AppCompatActivity(), SearchCityCallback, View.OnClickListener {


    private var title_city_name: TextView? = null
    private var degree_text: TextView? = null
    private var weather_info_text: TextView? = null
    private var wind: TextView? = null
    private var humidity: TextView? = null
    private var feelsLike: TextView? = null
    private var pressure: TextView? = null
    private var sunrise_sunset_time: TextView? = null
    private var uvIndex: TextView? = null
    private var aqi_text: TextView? = null
    private var rain_forecast: TextView? = null
    private var forecast_svg1: ImageView? = null
    private var forecast_svg2: ImageView? = null
    private var forecast_svg3: ImageView? = null
    private var info_text1: TextView? = null
    private var info_text2: TextView? = null
    private var info_text3: TextView? = null
    private var aqi1: TextView? = null
    private var aqi2: TextView? = null
    private var aqi3: TextView? = null
    private var temp_text1: TextView? = null
    private var temp_text2: TextView? = null
    private var temp_text3: TextView? = null
    private var suggestion_dress_index: TextView? = null
    private var suggestion_sunscreen: TextView? = null
    private var suggestion_sports: TextView? = null
    private var suggestion_car: TextView? = null
    private var suggestion_umbrella: TextView? = null
    private var suggestion_medicine: TextView? = null
    private var tv_forecast_warning: SwitchTextView? = null
    private var iv_warning: ImageView? = null
    private var relaout_forecast: RelativeLayout? = null
    private var sharedPreferences_added_city: SharedPreferences? = null
    
    //污染程度文本
    private lateinit var category: String
    //最高温度
    private lateinit var tempMax: String
    //最高温度
    private lateinit var tempMin: String
    //现在温度
    private lateinit var temp: String

    // 创建 ActivityResultLauncher
    private var activityResultLauncher: ActivityResultLauncher<Intent>? = null
    private lateinit var whetherFristStartSharedPreferences: SharedPreferences
    private lateinit var weather_backgroundimg: ImageView
    private var weather_swipeRefreshLayout: SwipeRefreshLayout? = null
    private val viewModel:ShowWeatherViewModel by lazy {
        ViewModelProvider(this).get(ShowWeatherViewModel::class.java)
    }
    private val weatherSharePreferences:SharedPreferences by lazy {
        getSharedPreferences("weather", MODE_PRIVATE)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        AMapLocationClient.updatePrivacyShow(CoolWeatherApplication.context,true,true);
        AMapLocationClient.updatePrivacyAgree(CoolWeatherApplication.context,true);
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_weather)

        findViewById<ImageView>(R.id.title_add).setOnClickListener(this)
        title_city_name = findViewById(R.id.title_city_name)
        weather_backgroundimg = findViewById(R.id.weather_backgroundimg)
        degree_text = findViewById<TextView>(R.id.degree_text)
        weather_info_text = findViewById<TextView>(R.id.weather_info_text)
        wind = findViewById<TextView>(R.id.wind)
        humidity = findViewById<TextView>(R.id.humidity)
        feelsLike = findViewById<TextView>(R.id.feelsLike)
        pressure = findViewById<TextView>(R.id.pressure)
        sunrise_sunset_time = findViewById<TextView>(R.id.sunrise_sunset_time)
        uvIndex = findViewById<TextView>(R.id.uvIndex)
        aqi_text = findViewById<TextView>(R.id.aqi_text)
        rain_forecast = findViewById<TextView>(R.id.rain_forecast)
        tv_forecast_warning = findViewById(R.id.tv_forecast_warning)
        tv_forecast_warning!!.setOnClickListener(this)
        iv_warning = findViewById<ImageView>(R.id.iv_warning)
        relaout_forecast = findViewById<RelativeLayout>(R.id.relaout_forecast)
        weather_backgroundimg=findViewById(R.id.weather_backgroundimg)

        forecast_svg1 = findViewById<ImageView>(R.id.forecast_svg1)
        forecast_svg2 = findViewById<ImageView>(R.id.forecast_svg2)
        forecast_svg3 = findViewById<ImageView>(R.id.forecast_svg3)
        info_text1 = findViewById<TextView>(R.id.info_text1)
        info_text2 = findViewById<TextView>(R.id.info_text2)
        info_text3 = findViewById<TextView>(R.id.info_text3)
        aqi1 = findViewById<TextView>(R.id.aqi1)
        aqi2 = findViewById<TextView>(R.id.aqi2)
        aqi3 = findViewById<TextView>(R.id.aqi3)
        temp_text1 = findViewById<TextView>(R.id.temp_text1)
        temp_text2 = findViewById<TextView>(R.id.temp_text2)
        temp_text3 = findViewById<TextView>(R.id.temp_text3)

        suggestion_dress_index = findViewById<TextView>(R.id.suggestion_dress_index)
        suggestion_sunscreen = findViewById<TextView>(R.id.suggestion_sunscreen)
        suggestion_sports = findViewById<TextView>(R.id.suggestion_sports)
        suggestion_car = findViewById<TextView>(R.id.suggestion_car)
        suggestion_umbrella = findViewById<TextView>(R.id.suggestion_umbrella)
        suggestion_medicine = findViewById<TextView>(R.id.suggestion_medicine)
        weather_swipeRefreshLayout=findViewById<SwipeRefreshLayout>(R.id.weather_swipeRefreshLayout)

        //将状态栏设置透明
        if (Build.VERSION.SDK_INT >= 21) {
            val decorView = window.decorView
            decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    or View.SYSTEM_UI_FLAG_LAYOUT_STABLE)
            window.statusBarColor = Color.TRANSPARENT
            // 设置底部的导航栏为透明
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
            )
            window.setFlags(
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION,
                WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION
            )
        }

        //设置背景图片
        Glide.with(this)
            .load(R.drawable.cloudpic)
            .into(weather_backgroundimg)

        //如果是第一次安装
        whetherFristStartSharedPreferences = getSharedPreferences("whetherFristStart", MODE_PRIVATE)
        val isFristStart = whetherFristStartSharedPreferences.getBoolean("isFristStart", true)
        if (isFristStart == true) {
            //（第一次安装软件时进行）获取定位的城市或者区县名，并且改变location使其进行weatherLiveData的观察操作
            viewModel.getLatLon(this,this)
            whetherFristStartSharedPreferences.edit {
                putBoolean("isFristStart",false)
            }
        } else {
            initWeather()
        }

        sharedPreferences_added_city = getSharedPreferences("added_city", MODE_PRIVATE)

        //通过观察locationLiveData，当天气数据改变进行
        viewModel.weatherLiveData.observe(this){weatherInfo->
            if (weatherInfo != null) {
                //保存数据
                WeatherInfoDao.saveWeatherInfo(weatherSharePreferences,weatherInfo)
            }
            initWeather()
        }


        // 注册回调
        activityResultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult())
        { result: ActivityResult ->
            if (result.resultCode == RESULT_OK && result.data != null) {
                //经纬度
                val lat = result.data!!.extras!!.getString("lat")
                val lon = result.data!!.extras!!.getString("lon")
                val city_name = result.data!!.extras!!.getString("city_name")
                title_city_name!!.text=city_name
                weatherSharePreferences.edit {
                    putString("城市名", city_name)
                    putString("纬度", lat)
                    putString("经度", lon)
                }
                viewModel.setLocation(lon+","+lat)
            }
        }

        //刷新
        weather_swipeRefreshLayout!!.setOnRefreshListener {
            val latitude= weatherSharePreferences.getString("纬度", null)
            val longitude= weatherSharePreferences.getString("经度", null)
            viewModel.setLocation(longitude+","+latitude)
            Toast.makeText(this, "更新完毕", Toast.LENGTH_SHORT).show()
        }

    }


    @SuppressLint("SetTextI18n")
    private fun initWeather() {
        title_city_name!!.text=weatherSharePreferences.getString("城市名", null)
        temp= weatherSharePreferences.getString("现在温度",null).toString()
        degree_text!!.text = "${temp}℃"
        tempMax= weatherSharePreferences.getString("最高温度", null).toString()
        tempMin= weatherSharePreferences.getString("最低温度", null).toString()
        weather_info_text!!.text=weatherSharePreferences.getString("描述", null) + " " +
                tempMax + "°" + "/" + tempMin+ "°"
        category= weatherSharePreferences.getString("污染程度文本", null).toString()
        aqi_text!!.text = category+" "+
                weatherSharePreferences.getString("aqi指数", null);
        if (weatherSharePreferences.getString("描述", null)?.contains("雨") == true) {
            rain_forecast!!.text = "正在降雨"
        } else {
            rain_forecast!!.text = "2小时内无降雨"
        }

        val warning_forecast_set = weatherSharePreferences.getStringSet("预警", null)
        val data: MutableList<String> = ArrayList()
        if (warning_forecast_set != null) {
            for (warning_forecast in warning_forecast_set) {
                data.add(warning_forecast)
            }
        }
        if (data.size != 0) {
            relaout_forecast!!.visibility = View.VISIBLE
            tv_forecast_warning!!.startPlay(data)
        } else {
            relaout_forecast!!.visibility = View.GONE
        }

        wind?.text=weatherSharePreferences.getString("风向", null)+"\n"+
                weatherSharePreferences.getString("风力等级", null)+"级"
        sunrise_sunset_time?.text= weatherSharePreferences.getString("日出时间", null) +
                "日出" + "\n" + weatherSharePreferences.getString("日落时间", null) + "日落"
        humidity?.text= weatherSharePreferences.getString("湿度", null) + "%"
        feelsLike?.text= weatherSharePreferences.getString("体感温度", null) + "℃"
        uvIndex?.text=weatherSharePreferences.getString("紫外线", null)
        pressure?.text= weatherSharePreferences.getString("气压", null) + "hPa"

        // 读取数据并还原为列表
        var icon: Int
        val three_day = weatherSharePreferences.getString("three_day", null)
        val level = weatherSharePreferences.getString("污染程度", null)
        if (three_day!=null&&level!=null){
            val retrievedList = three_day.split(",")
            val levelList = level.split(",")
            icon=this.resources.getIdentifier("_"+ retrievedList[0],"drawable",this.packageName)
            forecast_svg1!!.setImageResource(icon)
            info_text1!!.text= retrievedList[1]
            aqi1!!.text= levelList[0]
            temp_text1!!.text= (retrievedList[2] + "°/" + retrievedList[3] + "°")

            icon = this.resources.getIdentifier("_" + retrievedList[4], "drawable", this.packageName)
            forecast_svg2!!.setImageResource(icon)
            info_text2!!.text=retrievedList[5]
            aqi2!!.text=levelList[1]
            temp_text2!!.text= (retrievedList[6] + "°/" + retrievedList[7] + "°")

            icon = this.resources.getIdentifier("_" + retrievedList[8], "drawable", this.packageName)
            forecast_svg3!!.setImageResource(icon)
            info_text3!!.text=retrievedList[9]
            aqi3!!.text=levelList[2]
            temp_text3!!.text= (retrievedList[10] + "°/" + retrievedList[11] + "°")
        }

        // 读取数据并还原为列表
        val suggestion_level_list = weatherSharePreferences.getString("建议程度", null)
        if (suggestion_level_list != null) {
            val list = suggestion_level_list.split(",")
            //运动指数程度
            if (list[0].toInt() > 2) {
                suggestion_sports!!.text = "宜室内运动"
            } else {
                suggestion_sports!!.text = "宜室外运动"
            }//洗车指数程度
            if (suggestion_level_list[1].toInt() > 2) {
                suggestion_car!!.text = "不宜洗车"
            } else {
                suggestion_car!!.text = "宜洗车"
            }
            //穿衣指数程度
            if (suggestion_level_list[2].toInt() <= 3) {
                suggestion_dress_index!!.text = "适宜羽绒服"
            } else if (suggestion_level_list[0].toInt() > 3 &&
                suggestion_level_list[0].toInt() < 6
            ) {
                suggestion_dress_index!!.text = "宜室长袖"
            } else {
                suggestion_dress_index!!.text = "宜室短袖"
            }
            //感冒指数程度
            if (suggestion_level_list[3].toInt() > 2) {
                suggestion_medicine!!.text = "易感冒"
            } else {
                suggestion_medicine!!.text = "不易感冒"
            }
            //防晒指数程度
            if (suggestion_level_list[4].toInt() > 2) {
                suggestion_sunscreen!!.text = "注意防晒"
            } else {
                suggestion_sunscreen!!.text = "无需防晒"
            }
            //降雨
            if (weatherSharePreferences.getString("描述", null)?.contains("雨") == true) {
                suggestion_umbrella!!.text = "有雨带伞"
            } else {
                suggestion_umbrella!!.text = "无需带伞"
            }
        }
        //刷新结束，并且隐藏刷新进度条
        weather_swipeRefreshLayout!!.isRefreshing=false

    }

    override fun callbackBtnClick(location: String) {
        TODO("Not yet implemented")
    }

    override fun getLatLon(latitude: String, longitude: String, district: String) {
        Log.d("TAG", district)
        var _district=district
        if (_district.contains("区")) {
            _district=district.split("区")[0]
            title_city_name!!.text = _district
        } else if (district.contains("县")) {
            _district = district.split("县")[0]
            title_city_name!!.text = _district
        } else {
            title_city_name!!.text = _district
        }
        weatherSharePreferences?.edit {

            putString("城市名",_district)
            putString("纬度",latitude)
            putString("经度",longitude)
        }
        //改变location进行观察操作
        viewModel.setLocation(longitude+","+latitude)
    }

    override fun getFailure(code: String) {
        Log.i("TAG", code)
    }

    override fun onClick(v: View?) {
        if (v!!.id==R.id.tv_forecast_warning){
            val intent: Intent = Intent(this, Warning::class.java)
            val warning_forecast_set= weatherSharePreferences.getStringSet("预警", null)
            var i = 0
            val bundle = Bundle()
            if (warning_forecast_set != null) {
                for (warning in warning_forecast_set) {
                    bundle.putString("预警$i", warning)
                    i++
                }
            }
            intent.putExtras(bundle)
            activityResultLauncher!!.launch(intent)
        }

        if (v.id == R.id.title_add) {
            sharedPreferences_added_city?.edit {
                val dataList= ArrayList<String>()
                dataList.apply {
                    add(title_city_name!!.getText().toString())
                    add(category)
                    add(tempMax)
                    add(tempMin)
                    add(temp)
                    val location=viewModel.getLocation()
                    if (location != null) {
                        add(location)
                    }
                }
                val join=java.lang.String.join(",",dataList)
                putString(title_city_name?.getText().toString(),join)
            }
            val intent= Intent(this, CityManagement::class.java)
            activityResultLauncher!!.launch(intent)
        }
    }

}