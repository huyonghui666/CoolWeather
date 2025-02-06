package com.example.coolweather.logic.networkModel

data class ShowWeatherResponse(
    val showWeatherNowResponse:ShowWeatherNowResponse,
    val showWeatherThreeDayResponse:ShowWeatherThreeDayResponse,
    val showWeatherAqiResponse: ShowWeatherAqiResponse,
    val showWeatherWarningResponse:ShowWeatherWarningResponse,
    val showWeatherSuggestionResponse:ShowWeatherSuggestionResponse)
