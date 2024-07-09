package com.michasoft.stationdistance.model

import android.location.Location

data class Station(
    val id: Int,
    val name: String,
    val location: LatLng,
    val hits: Int
)