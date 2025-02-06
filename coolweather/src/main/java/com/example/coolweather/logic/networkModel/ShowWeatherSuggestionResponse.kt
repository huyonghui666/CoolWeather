package com.example.coolweather.logic.networkModel

data class ShowWeatherSuggestionResponse(val code:String,val daily:List<Suggestion>)

/**
 *level:建议程度
 */
data class Suggestion(val level:String)
