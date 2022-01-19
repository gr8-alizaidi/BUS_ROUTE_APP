package com.alizaidi.busroute.api

import com.alizaidi.busroute.models.BusInfo
import com.alizaidi.busroute.models.RoutesInfo
import com.alizaidi.busroute.models.Stops
import com.alizaidi.busroute.models.StopsResult
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface OtherApi {

    @GET("get_stops")
    suspend fun getStops(): Response<StopsResult>

    @GET("get_routes")
    suspend fun getRoutes(): Response<RoutesInfo>
}