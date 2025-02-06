package com.example.coolweather.UI.cityManagement

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.recyclerview.widget.RecyclerView
import com.example.coolweather.R

class CityManagementAdapter(val context:Context,val dataList:List<CityManagementCls>,val onItemClickListener:OnItemClickListener)
    : RecyclerView.Adapter<CityManagementAdapter.ViewHolder>() {
    class ViewHolder(itemView:View): RecyclerView.ViewHolder(itemView) {
        val relaout_citychild=itemView.findViewById<RelativeLayout>(R.id.relaout_citychild)
        val citychild_cityname = itemView.findViewById<android.widget.TextView?>(R.id.citychild_cityname)
        val citychild_aqi = itemView.findViewById<android.widget.TextView?>(R.id.citychild_aqi)
        val citychild_nowtemp = itemView.findViewById<android.widget.TextView?>(R.id.citychild_nowtemp)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.citymanegement_itme, null, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val cityManagementCls = dataList.get(position)
        holder.citychild_cityname.setText(cityManagementCls.cityName)
        holder.citychild_aqi.setText(cityManagementCls.aqi)
        holder.citychild_nowtemp.setText(cityManagementCls.nowtemp)

        //单点跳转到天气页面
        holder.relaout_citychild.setOnClickListener {
            val city_name = holder.citychild_cityname.text.toString()
            //回调函数传入城市名
            if (onItemClickListener != null) {
                onItemClickListener.onItemClicked(city_name)
            }
            /*String request_str="https://geoapi.qweather.com/v2/city/lookup?location="+city_name
                    +"&range=cn&number=20&key=50f12b7a8c974cdd91b2ba876abf19f4";
            search_city(request_str);*/
        }

        //长按删除
        holder.relaout_citychild.setOnLongClickListener { true }

    }
}