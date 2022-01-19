package com.alizaidi.busroute.models

data class RoutesInfo
    (
    var routes:ArrayList<Route>
    )
data class Route(
    var agency:String="",
    var direction:Int=0,
    var end:String="",
    var id:Int=0,
    var long_name:String="",
    var route:String="",
    var short_name:String="",
    var start:String="",
    var trips_count:String=""
)
