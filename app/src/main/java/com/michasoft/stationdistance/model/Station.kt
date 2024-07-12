package com.michasoft.stationdistance.model


data class Station(
    val id: Int,
    val name: String,
    val location: LatLng,
    val hits: Int
)