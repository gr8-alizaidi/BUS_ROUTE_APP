package com.alizaidi.busroute.utils

import java.util.concurrent.TimeUnit

class TimeConverter {

    fun convert(milliseconds: Long): Long {


        // long minutes = (milliseconds / 1000) / 60;
        val minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds)


        return minutes
    }
}