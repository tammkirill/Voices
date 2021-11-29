package com.example.voices.models

data class Post(
    var id: String,
    var main_text: String,
    var email: String,
    var topic: String,
    var img_link: String? = null,
    var user_like: Boolean? = null,
    var created_at: String,
    var vote_for: Int,
    var vote_against: Int
)