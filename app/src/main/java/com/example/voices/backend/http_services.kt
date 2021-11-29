package com.example.voices.backend

import android.content.Context
import com.example.voices.models.*
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.github.kittinunf.fuel.core.extensions.jsonBody
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.fuel.httpPost

class HttpServices(context: Context) {
    private val url = "https://hack-club-back.herokuapp.com"
    private val context = context
    val sharedPreferences = SharedPreferences(context)

    suspend fun login(email:String, pwd:String):Boolean{

        val(_,response,result) = "$url/login".httpPost()
            .jsonBody("{\"email\":\"$email\", \"user_password\":\"$pwd\"}")
            .responseString()
        val(code,error) = result

        if (error!=null){
            println(error)
        }

        if(code!=null){
            sharedPreferences.save("access_token", code)
            sharedPreferences.save("email", email)

        }
        return response.statusCode==200
    }


    suspend fun registration(name:String, email:String, pwd:String):Boolean{

        val(_,response,result) = "$url/registration".httpPost()
            .jsonBody("{\"first_name\":\"$name\",\"email\":\"$email\", \"user_password\":\"$pwd\"}")
            .responseString()
        val(code,error) = result

        if (error!=null){
            println(error)
        }

        if(code!=null){
            sharedPreferences.save("access_token", code)
            sharedPreferences.save("email", email)

        }
        return response.statusCode==200
    }


    suspend fun getPoints():List<Point> {

        val points = ArrayList<Point>()
        val mapper = jacksonObjectMapper()

        val (_,_,result) = "$url/points".httpGet().responseString()
        val pointsFromJson = mapper.readValue<Points>(result.get())
        val(_,error) = result
        if (error!=null){
            println(error)
            return listOf()
        }
        for (point in pointsFromJson.points) {
                points.add(point)
            }
        return points
    }


    suspend fun getFeed(): Feed {

        val access_token = sharedPreferences.getValueString("access_token")
        val mapper = jacksonObjectMapper()

        val (request, response, result) = "$url/feed".httpGet()
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(request)
        val(_,error) = result
        if (error!=null){
            println(error)
            return Feed(mutableListOf())
        }
        println(response)
        return mapper.readValue(result.get())
    }

    suspend fun addPost(main_text: String, topic: String,img_link:String = "string"):Boolean{
        val access_token = sharedPreferences.getValueString("access_token")
        println(access_token)
        val(req,response,result) = "$url/feed".httpPost()
            //.header(mapOf("Authorization" to "Bearer $access_token"))
            .jsonBody("{\"main_text\":\"$main_text\",\"topic\":\"$topic\",\"img_link\":\"$img_link\"}")
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(req)
        val(_,error) = result

        if (error!=null){
            println(error)
        }

        return response.statusCode==200
    }


    suspend fun votePost(id:String, like:Boolean?):Boolean{
        val access_token = sharedPreferences.getValueString("access_token")
        val(req,response,result) = "$url/feed/like".httpPost()
            .jsonBody("{\"news_id\":\"$id\",\"like\":$like}")
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(req)
        val(_,error) = result
        if (error!=null){
            println(error)
        }

        return response.statusCode==200
    }


    suspend fun getUsers(): Users {
        val access_token = sharedPreferences.getValueString("access_token")

        val mapper = jacksonObjectMapper()

        val (request, response, result) = "$url/users".httpGet()
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(request)
        val(_,error) = result
        if (error!=null){
            println(error)
            return Users(mutableListOf())
        }
        println(response)
        return mapper.readValue(result.get())
    }


    suspend fun getFriends(): Friends {
        val access_token = sharedPreferences.getValueString("access_token")

        val mapper = jacksonObjectMapper()

        val (_, response, result) = "$url/friends".httpGet()
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(response)
        val(_,error) = result
        if (error!=null){
            println(error)
            return Friends(mutableListOf())
        }
        return mapper.readValue(result.get())
    }


    suspend fun getProfile(): User {
        val access_token = sharedPreferences.getValueString("access_token")

        val mapper = jacksonObjectMapper()

        val (request, response, result) = "$url/profile".httpGet()
            .header("Authorization", "Bearer " + access_token!!.replace("\"",""))
            .responseString()
        println(request)
        val(_,error) = result
        if (error!=null){
            println(error)
        }
        println(response)
        return mapper.readValue(result.get())
    }
}