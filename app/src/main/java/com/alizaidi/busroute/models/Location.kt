package com.alizaidi.busroute.models

import com.google.gson.annotations.SerializedName

data class Location (
   @SerializedName("\$reql_type$")
    var reql_type : String="",
   var coordinates:ArrayList<Double>,
   var type:String=""
        ){
}