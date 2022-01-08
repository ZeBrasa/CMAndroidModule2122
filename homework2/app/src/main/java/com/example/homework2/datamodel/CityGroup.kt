package com.example.homework2.datamodel

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CityGroup {
    @Expose
    @SerializedName("owner")
    private var owner: String? = null

    @Expose
    @SerializedName("country")
    private var country: String? = null

    @Expose
    @SerializedName("data")
    private var cities: List<City>? = null

    fun CityGroup(owner: String?, country: String?, cities: List<City>?) {
        this.owner = owner
        this.country = country
        this.cities = cities
    }

    fun getOwner(): String? {
        return owner
    }

    fun setOwner(owner: String?) {
        this.owner = owner
    }

    fun getCountry(): String? {
        return country
    }

    fun setCountry(country: String?) {
        this.country = country
    }

    fun getCities(): List<City>? {
        return cities
    }

    fun setCities(cities: List<City>?) {
        this.cities = cities
    }
}