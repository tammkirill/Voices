package com.example.voices.viewmodels

import android.content.Context
import androidx.lifecycle.ViewModel
import com.example.voices.backend.HttpServices
import com.example.voices.main_ui.customThread
import com.example.voices.models.User
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map

@ObsoleteCoroutinesApi
@OptIn(ExperimentalCoroutinesApi::class)
class UsersViewModel: ViewModel() {

    val users = MutableStateFlow<MutableList<User>>(mutableListOf())

    val friends = MutableStateFlow<MutableList<User>>(mutableListOf())

    lateinit var profile: User

    @ObsoleteCoroutinesApi
    fun getProfile(context: Context, thread: ExecutorCoroutineDispatcher = customThread): User {
        val u = runBlocking {

            withContext(thread) {
                HttpServices(context).getProfile()

            }
        }
        profile = User(u.id,u.email,u.first_name,u.rating,u.city,u.phone)
        return u
    }

    @ObsoleteCoroutinesApi
    fun getUsers(context: Context, thread: ExecutorCoroutineDispatcher = customThread){
        //val customThread = newSingleThreadContext("users")
        CoroutineScope(thread).launch {

            val u = HttpServices(context).getUsers()
            for (i in u.users!!) {
                users.value.add(i)
            }
        }
        // customThread.close()
    }
    @ObsoleteCoroutinesApi
    fun getFriends(context: Context, thread: ExecutorCoroutineDispatcher = customThread){

        CoroutineScope(thread).launch {

            val u = HttpServices(context).getFriends()
            for (i in u.friends) {
                friends.value.add(i)
            }

        }

    }

    fun searchUsers(query: String,users: MutableStateFlow<MutableList<User>>) =
        users.map {  users ->
            return@map if(query.isNotBlank())
                users.filter { user ->
                    user.first_name!!.contains(
                        query, ignoreCase = true
                    )
                }
            else listOf()
        }
}