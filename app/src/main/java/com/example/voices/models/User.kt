package com.example.voices.models

data class User(var id:String, var email: String, var first_name:String?, var rating: Double,var city:String?, var phone:String?)
data class Users(var users: MutableList<User>?)
data class Friends(var friends:MutableList<User>)

