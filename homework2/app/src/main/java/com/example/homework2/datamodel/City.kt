package com.example.homework2.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import java.util.*


class City {

    @Expose
    @SerializedName("local")
    private var local: String? = null

    @Expose
    @SerializedName("globalIdLocal")
    private var globalIdLocal = 0

    @Expose
    @SerializedName("latitude")
    private var latitude = 0.0

    @Expose
    @SerializedName("longitude")
    private var longitude = 0.0

    private var lastRefresh: Date? = null

    private var forecasts: List<Weather?>? = null


    fun City(
        local: String?,
        globalIdLocal: Int,
        latitude: Double,
        longitude: Double,
        lastRefresh: Date?
    ) {
        this.local = local
        this.globalIdLocal = globalIdLocal
        this.latitude = latitude
        this.longitude = longitude
        this.lastRefresh = lastRefresh
    }


    /* ---- Getters & Setters---- */

    /* ---- Getters & Setters---- */
    fun getForecasts(): List<Weather?>? {
        return forecasts
    }

    fun setForecasts(forecasts: List<Weather?>?) {
        this.forecasts = forecasts
    }

    fun getLocal(): String? {
        return local
    }

    fun setLocal(local: String?) {
        this.local = local
    }

    fun getGlobalIdLocal(): Int {
        return globalIdLocal
    }

    fun setGlobalIdLocal(globalIdLocal: Int) {
        this.globalIdLocal = globalIdLocal
    }

    fun getLatitude(): Double {
        return latitude
    }

    fun setLatitude(latitude: Double) {
        this.latitude = latitude
    }

    fun getLongitude(): Double {
        return longitude
    }

    fun setLongitude(longitude: Double) {
        this.longitude = longitude
    }

    fun setLastRefresh(lastRefresh: Date?) {
        this.lastRefresh = lastRefresh
    }

    fun getLastRefresh(): Date? {
        return lastRefresh
    }


    override fun toString(): String {
        return "City{" +
                "local='" + local + '\'' +
                ", globalIdLocal=" + globalIdLocal +
                ", forecasts=" + forecasts +
                ", latitude=" + latitude +
                ", longitude=" + longitude +
                '}'
    }
}
