package com.alizaidi.busroute.models

data class StopsResult(
    var status:String="",
    var stops:ArrayList<Stops>
)

data class Stops(

    var id:String="",
    var lat:Double=0.0,
    var lng:Double=0.0,
    var name:String="",
    var next_stop:String=""
) {
}