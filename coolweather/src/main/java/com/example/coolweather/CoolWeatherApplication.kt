package com.example.coolweather

import android.annotation.SuppressLint
import android.app.Application
import android.content.Context


class CoolWeatherApplication: Application() {

    companion object{
        //全局Context
        @SuppressLint("StaticFieldLeak")
        lateinit var context:Context
        //全局令牌
        const val TOKEN="50f12b7a8c974cdd91b2ba876abf19f4"

    }

    override fun onCreate() {
        super.onCreate()
        context=applicationContext


    }
}