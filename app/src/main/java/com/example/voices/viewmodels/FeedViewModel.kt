package com.example.voices.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.voices.backend.HttpServices
import com.example.voices.main_ui.customThread
import com.example.voices.models.Post
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.ExecutorCoroutineDispatcher
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class FeedViewModel: ViewModel(){
    var feed = MutableStateFlow<MutableList<Post>>(mutableListOf())

    @ObsoleteCoroutinesApi
    fun getFeed(context: Context, thread: ExecutorCoroutineDispatcher = customThread){
        CoroutineScope(thread).launch {
            feed = MutableStateFlow<MutableList<Post>>(mutableListOf())
            val f = HttpServices(context).getFeed()
            for (p in f.news) {
                feed.value.add(p)
            }
        }
    }
}