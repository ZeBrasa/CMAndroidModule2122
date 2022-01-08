package com.example.homework2.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface IpmaEndPoint {
    @GET("open-data/distrits-islands.json")
    fun getCityParent(): Call<CityGroup?>?

    @GET("open-data/forecast/meteorology/cities/daily/{localId}.json")
    fun getWeatherParent(@Path("localId") localId: Int): Call<WeatherGroup?>?

    @GET("open-data/weather-type-classe.json")
    fun getWeatherTypes(): Call<WeatherTypeGroup?>?
}