package com.example.voices.models

data class Point (var id: String, var loc_lat: String, val loc_long: String, var text: String)

data class Points(var points: MutableList<Point>)
