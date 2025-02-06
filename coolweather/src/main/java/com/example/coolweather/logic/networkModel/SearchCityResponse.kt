package com.example.coolweather.logic.networkModel

data class SearchCityResponse(val code:String,val location:ArrayList<Location>)

data class Location(val name:String,val adm2:String,val adm1:String,val country:String,val lat:String,val lon:String)


