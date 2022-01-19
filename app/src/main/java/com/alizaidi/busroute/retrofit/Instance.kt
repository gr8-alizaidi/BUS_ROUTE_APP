package com.alizaidi.busroute.retrofit

import com.alizaidi.busroute.api.BusApi
import com.alizaidi.busroute.api.OtherApi
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object instance {

    // Create Retrofit
    val busapiInstance: BusApi = Retrofit.Builder()
        .baseUrl("https://live.chartr.in/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(BusApi::class.java)

    val otherapiInstance:OtherApi= Retrofit.Builder()
        .baseUrl("https://routesapi.chartr.in/transit/dimts/")
        .addConverterFactory(ScalarsConverterFactory.create())
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OtherApi::class.java)!!
}

