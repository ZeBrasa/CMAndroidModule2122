package com.example.homework2.api

import android.util.Log
import com.example.homework2.datamodel.City
import com.example.homework2.datamodel.CityGroup
import pt.ua.nextweather.datamodel.Weather
import pt.ua.nextweather.datamodel.WeatherGroup
import pt.ua.nextweather.datamodel.WeatherType
import pt.ua.nextweather.datamodel.WeatherTypeGroup
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import java.util.*


/**
 * issues the requests to the remote IPMA API
 * consummers should provide a lister to get the results
 */
class WeatherClient {
    private val apiService: IpmaEndPoint

    /**
     * get a list of cities (districts)
     * @param listener a listener object to callback with the results
     */
    fun retrieveCitiesList(listener: CityResultsObserver) {
        val cities = HashMap<String?, City>()
        val call: Call<CityGroup> = apiService.getCityParent()
        call.enqueue(object : Callback<CityGroup?> {
            override fun onResponse(call: Call<CityGroup?>, response: Response<CityGroup?>) {
                val statusCode = response.code()
                val citiesGroup = response.body()
                for (city in citiesGroup!!.getCities()!!) {
                    cities[city.getLocal()] = city
                }
                listener.receiveCitiesList(cities)
            }

            override fun onFailure(call: Call<CityGroup?>, t: Throwable) {
                Log.e("main", "errog calling remote api: " + t.localizedMessage)
                listener.onFailure(t)
            }
        })
    }

    /**
     * get the dictionary for the weather states
     * @param listener a listener object to callback with the results
     */
    fun retrieveWeatherConditionsDescriptions(listener: WeatherTypesResultsObserver) {
        val weatherDescriptions: HashMap<Int, WeatherType> = HashMap<Int, WeatherType>()
        val call: Call<WeatherTypeGroup> = apiService.getWeatherTypes()
        call.enqueue(object : Callback<WeatherTypeGroup?> {
            override fun onResponse(
                call: Call<WeatherTypeGroup?>,
                response: Response<WeatherTypeGroup?>
            ) {
                val statusCode = response.code()
                val weatherTypesGroup: WeatherTypeGroup? = response.body()
                for (weather in weatherTypesGroup.getTypes()) {
                    weatherDescriptions[weather.getIdWeatherType()] = weather
                }
                listener.receiveWeatherTypesList(weatherDescriptions)
            }

            override fun onFailure(call: Call<WeatherTypeGroup?>, t: Throwable) {
                Log.e("main", "errog calling remote api: " + t.localizedMessage)
                listener.onFailure(t)
            }
        })
    }

    /**
     * get the 5-day forecast for a city
     * @param  localId the global identifier of the location
     * @param listener a listener object to callback with the results
     */
    fun retrieveForecastForCity(localId: Int, listener: ForecastForACityResultsObserver) {
        val forecast: MutableList<Weather> = ArrayList<Weather>()
        val call: Call<WeatherGroup> = apiService.getWeatherParent(localId)
        call.enqueue(object : Callback<WeatherGroup?> {
            override fun onResponse(call: Call<WeatherGroup?>, response: Response<WeatherGroup?>) {
                val statusCode = response.code()
                val weatherTypesGroup: WeatherGroup? = response.body()
                forecast.addAll(weatherTypesGroup.getForecasts())
                listener.receiveForecastList(forecast)
            }

            override fun onFailure(call: Call<WeatherGroup?>, t: Throwable) {
                Log.e("main", "errog calling remote api: " + t.localizedMessage)
                listener.onFailure(t)
            }
        })
    }

    init {
        val retrofitInstance: Retrofit = RetrofitInstance.getRetrofitInstance()
        apiService = retrofitInstance.create(IpmaApiEndpoints::class.java)
    }
}

