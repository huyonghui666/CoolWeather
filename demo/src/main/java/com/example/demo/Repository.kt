package com.example.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers

object Repository {

  /*  fun getUser(userId:String):LiveData<User>{
        val liveData = MutableLiveData<User>()
        liveData.value=User(userId,userId,0)
        return liveData
    }*/

    // 假设有一个获取数据的函数
    fun fetchDataFromNetwork(): String {
        // 模拟网络请求
        Thread.sleep(2000)  // 模拟网络延时
        return "Fetched Data"
    }

    // 创建 LiveData
    fun fetchData() = liveData(Dispatchers.IO) {
        // 在后台线程执行异步操作
        val data = fetchDataFromNetwork()  // 这是一个耗时操作
        emit(data)  // 发射结果到 LiveData
    }
}