package com.alizaidi.busroute.api

import com.alizaidi.busroute.models.BusInfo
import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface BusApi {

    @Headers("Content-Type: application/json")
   @POST("/nearby-buses")
    suspend fun getBus(@Body requestBody: String): Response<List<BusInfo>>


}