package com.example.coolweather.UI.searchCity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.edit
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R
import com.example.coolweather.UI.cityManagement.CityManagement

class SearchCityActivity : AppCompatActivity(), SearchCityCallback, View.OnClickListener {

    private lateinit var btn_search_city_back: Button
    private lateinit var tv_search_city_address: TextView
    private lateinit var adapter: SearchCityAdapter
    private lateinit var linearLayout_search_city: LinearLayout
    private lateinit var searchcityintent_linear: LinearLayout
    private lateinit var searchcityintent_RecyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var et_search_city: TextView
    val viewModel:SearchCityViewModel by lazy {
        ViewModelProvider(this).get(SearchCityViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search_city)

        et_search_city=findViewById(R.id.et_search_city)
        searchcityintent_RecyclerView=findViewById(R.id.searchcityintent_RecyclerView)
        searchcityintent_linear=findViewById(R.id.searchcityintent_linear)
        linearLayout_search_city=findViewById(R.id.linearLayout_search_city)
        tv_search_city_address=findViewById(R.id.tv_search_city_address)
        btn_search_city_back=findViewById<Button>(R.id.btn_search_city_back)
        btn_search_city_back.setOnClickListener(this)
        //初始化定位
        viewModel.getLatLon(this,this)

        sharedPreferences=getSharedPreferences("added_city", Context.MODE_PRIVATE)
        //设置适配器
        val linearLayoutManager = LinearLayoutManager(this)
        searchcityintent_RecyclerView.layoutManager=linearLayoutManager
        adapter=SearchCityAdapter(this,viewModel,this,sharedPreferences,viewModel.location)
        searchcityintent_RecyclerView.adapter=adapter

        //设置文本的焦点和输入法
        et_search_city.requestFocus()
        val systemService = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        systemService.showSoftInput(et_search_city,0)
        //设置文本变化监听
        et_search_city.addTextChangedListener { text: Editable? ->
            //输入城市或者区
            var city = text.toString()
            if (!city.equals("")){
                searchcityintent_linear.visibility = View.GONE
                linearLayout_search_city.visibility=View.GONE
                searchcityintent_RecyclerView.visibility = View.VISIBLE
                //观察文本变化并且网络请求返回数据
                viewModel.searchCity(text.toString())
            }else{
                viewModel.location.clear()
                adapter.notifyDataSetChanged()
                searchcityintent_linear.visibility = View.GONE
                searchcityintent_RecyclerView.visibility = View.GONE
                linearLayout_search_city.visibility = View.VISIBLE
            }
        }

        //观察地址列表变化情况
        viewModel.locationLiveData.observe(this){searchCityResponse->
            if (searchCityResponse!=null){
                if (!searchCityResponse.code.equals("200")){
                    //查找不到城市
                    searchcityintent_linear.visibility= View.VISIBLE
                    val view: View = LayoutInflater.from(this)
                        .inflate(R.layout.nomatchcity, null)
                    val params = LinearLayout.LayoutParams(
                            ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.WRAP_CONTENT / 2
                        )
                    searchcityintent_linear.addView(view, params)
                }else{
                    viewModel.location.clear()
                    viewModel.location.addAll(searchCityResponse.location)
                    adapter.notifyDataSetChanged()
                }
            }
        }

        viewModel.cityInfoLiveData.observe(this){cityInfoResponse->
            if (cityInfoResponse != null) {
                var cityInfo = ""
                cityInfo = viewModel.cityName
                val category = cityInfoResponse.showWeatherAqiResponse.daily[0].category
                var level = category.split("污")[0]
                if (level == "") {
                    //说明是良、优等
                    level = category
                }
                cityInfo = cityInfo + "," + level
                val tempMax = cityInfoResponse.showWeatherThreeDayResponse.daily[0].tempMax
                val tempMin = cityInfoResponse.showWeatherThreeDayResponse.daily[0].tempMin
                cityInfo = cityInfo + "," + tempMax + "," + tempMin
                val temp = cityInfoResponse.showWeatherNowResponse.now.temp
                cityInfo = cityInfo + "," + temp + "," + viewModel.getBtn()
                //保存
                sharedPreferences.edit {
                    putString(viewModel.cityName, cityInfo)
                }
            }
        }

    }

    override fun getLatLon(latitude: String, longitude: String, district: String) {
        tv_search_city_address.text = district
    }

    override fun getFailure(code: String) {
        Log.i("TAG", code)
    }

    override fun onClick(v: View?) {
        val intent = Intent(this, CityManagement::class.java)
        setResult(RESULT_OK, intent)
        finish()
    }

    override fun callbackBtnClick(location: String) {
        viewModel.setBtn(location)
    }

}