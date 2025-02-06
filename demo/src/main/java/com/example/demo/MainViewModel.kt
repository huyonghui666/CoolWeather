package com.example.demo

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import androidx.lifecycle.switchMap
import com.example.demo.Repository.fetchData

class MainViewModel:ViewModel() {

    /*private val userIdLiveData=MutableLiveData<String>()

    val user:LiveData<User> =userIdLiveData.switchMap {userId->
        Repository.getUser(userId)
    }

    fun getUser(userId:String){
        userIdLiveData.value=userId
    }*/

    val data = fetchData()  // 调用 liveData 函数

}