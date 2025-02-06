package com.example.coolweather.UI.searchCity

import android.content.SharedPreferences
import android.graphics.Color
import android.graphics.Color.BLACK
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.content.edit
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.distinctUntilChanged
import androidx.lifecycle.liveData
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R
import com.example.coolweather.logic.Repository
import com.example.coolweather.logic.network.Request
import com.example.coolweather.logic.networkModel.Location
import com.example.coolweather.logic.networkModel.ShowWeatherResponse
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch
import java.util.Timer
import java.util.TimerTask

class SearchCityAdapter(private val searchCityCallback: SearchCityCallback, private val viewModel: SearchCityViewModel,private val activity: SearchCityActivity,private val sharedPreferences: SharedPreferences,private val dataList:List<Location>)
    : RecyclerView.Adapter<SearchCityAdapter.ViewHolder>(){

    //拼接城市的相关提起信息
    var cityInfo: String = ""


    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val tv_search_city_name=itemView.findViewById<TextView>(R.id.tv_search_city_name)
        val btn_add_city=itemView.findViewById<Button>(R.id.btn_add_city)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.seach_city_item, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (dataList!=null){
            val item = dataList.get(position)
            if (item.adm2.equals(item.name)){
                holder.tv_search_city_name.text=item.name+"-"+item.country
            }else{
                holder.tv_search_city_name.text= item.name+"-"+item.adm1+","+item.country
            }
            val city_name = sharedPreferences.getString(item.name, null)
            //如果获取到了城市名，如果有就说明已经添加了，就将控件设置已经添加
            if (city_name!=null){
                holder.btn_add_city.text = "已添加"
                holder.btn_add_city.textSize = 15f
                holder.btn_add_city.setBackgroundColor(Color.TRANSPARENT) // 设置透明背景
            }else{
                holder.btn_add_city.setOnClickListener{
                    holder.btn_add_city.text = "已添加"
                    holder.btn_add_city.textSize = 15f
                    holder.btn_add_city.setBackgroundColor(Color.TRANSPARENT) // 设置透明背景
                    cityInfo = ""
                    cityInfo = cityInfo + item.name
                    //调用回调方法在SearchCityActivity中实现
                    viewModel.cityName=item.name
                    searchCityCallback.callbackBtnClick(item.lon+","+item.lat)

                }
            }
        }
    }

}