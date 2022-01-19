package com.alizaidi.busroute.Repository

import android.util.Log
import com.alizaidi.busroute.models.BusInfo
import com.alizaidi.busroute.models.Route
import com.alizaidi.busroute.models.Stops
import com.alizaidi.busroute.retrofit.instance
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import java.io.IOException

class repo {
    var routes:ArrayList<Route> = ArrayList()
     var businfo:ArrayList<BusInfo> = ArrayList()
      var stops:ArrayList<Stops> = ArrayList()
    var status=0


    suspend fun getStop() {

            val response =try {
                instance.otherapiInstance.getStops()
            }
            catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection $e")
                status = -1;
                return
            } catch (e: HttpException) {
                status = 0;
                Log.e("TAG", "HttpException, unexpected response")
                return
            }
            if (response.isSuccessful && response.body() != null) {
                    status = 1;
                    for(i in response.body()!!.stops)
                    {
                        stops.add(i)
                    }
                }
            }


    suspend fun getBus() {

        val jsonObject = JSONObject()
        jsonObject.put("device_id", "625eb5dcebf9e4b0146878aad75de325bde59f57")
        jsonObject.put("lat", 28.545625199086384)
        jsonObject.put("lon", 77.27303899510929)

        val jsonObjectString = jsonObject.toString()

            val response = try{
                instance.busapiInstance.getBus(jsonObjectString)
            } catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection $e")
                status = -1;
                return
            } catch (e: HttpException) {
                status = 0;
                Log.e("TAG", "HttpException, unexpected response")
                return
            }
        if (response.isSuccessful && response.body() != null) {
            status = 1;
            businfo=response.body() as ArrayList<BusInfo>
            Log.e("RETROFIT_ERROR", businfo.toString())
            } else
            {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
            }

    suspend fun getRouteData() {
            val response = try {
                instance.otherapiInstance.getRoutes()
            } catch (e: IOException) {
                Log.e("TAG", "IOException, you might not have internet connection $e")
                status = -1;
                return
            } catch (e: HttpException) {
                status = 0;
                Log.e("TAG", "HttpException, unexpected response")
                return
            }
        if (response.isSuccessful && response.body() != null) {
            status = 1;
            routes=response.body()!!.routes as ArrayList<Route>
                    Log.e("RETROFIT_ERROR", routes.toString())
                } else {
                    Log.e("RETROFIT_ERROR", response.code().toString())
                }
        }
    }