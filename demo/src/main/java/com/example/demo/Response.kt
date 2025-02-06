package com.example.demo

data class MyResponse(val daily:List<Daily>)

data class Daily(val tempMax:String,val tempMin:String)



