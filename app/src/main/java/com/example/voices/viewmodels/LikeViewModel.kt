package com.example.voices.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.voices.backend.HttpServices
import com.example.voices.main_ui.oneMoreThread
import com.example.voices.models.Post
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class LikeViewModel(post: Post): ViewModel(){
    var post = post
    var isLiked = if(post.user_like!=null) MutableStateFlow(post.user_like!!) else MutableStateFlow(false)
    var isDisliked = if(post.user_like!=null) MutableStateFlow(!post.user_like!!) else MutableStateFlow(false)
    fun updateLike(new_value:Boolean?, context: Context){
        val httpServices = HttpServices(context)
        if(new_value!=null){
            isLiked.value = new_value
            isDisliked.value = !new_value
        }
        else{
            isLiked.value = false
            isDisliked.value = false
        }
        runBlocking { async(){
            withContext(oneMoreThread){
                post.user_like = new_value

                httpServices.votePost(post.id,post.user_like)
            }
        }}
    }
}