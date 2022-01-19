package com.alizaidi.busroute.models


data class BusInfo (
var ac:String="",
var agency:String="",
var id:String="",
var lat:Double=0.0,
var lng:Double=0.0,
var location:Location,
var orientation:Double=0.0,
var route:String="",
var timestamp: Long=0
        )