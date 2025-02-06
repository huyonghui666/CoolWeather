package com.example.demo.Util

import com.example.demo.MyResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.coroutines.suspendCoroutine

object Helper {

   /* fun String.lettersCount():Int{
        var count=0
        for (char in this){
            if (char.isLetter()){
                count++
            }
        }
        return count
    }


    fun num1AndNum2(num1:Int,num2:Int,operation:(Int,Int)->Int):Int{
        val result=operation(num1,num2)
        return result
    }

    fun StringBuilder.build(block:StringBuilder.() ->Unit):StringBuilder{
        block()
        return this
    }

    fun <T> method(param:T):T{
        return param
    }

    fun <T> T.build(block:T.()->Unit):T{
        block()
        return this
    }*/

    private const val BASE_URL = "https://devapi.qweather.com/v7/weather/"
    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    fun <T> create(weatherService:Class<T>):T= retrofit.create(weatherService)



    suspend fun <T> Call<T>.await():T{
        return suspendCoroutine {comtinuation->
            enqueue(object :Callback<T>{
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

}





