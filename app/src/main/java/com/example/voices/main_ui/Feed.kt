package com.example.voices.main_ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voices.R
import kotlinx.coroutines.ObsoleteCoroutinesApi

@SuppressLint("CoroutineCreationDuringComposition")
@ObsoleteCoroutinesApi
@Composable()
fun Feed(onCreatePost:()->Unit){


    val feed = feedViewModel.feed
    Scaffold(topBar = {
        Surface (elevation = 10.dp, modifier = Modifier.wrapContentSize()) {
            Row(horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).padding(top = 10.dp, bottom = 5.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text = "Новости", style = TextStyle(fontSize = 30.sp, fontWeight = FontWeight.Bold))
            IconButton(onClick = {  }) {
                Icon(painter = painterResource(id = R.drawable.ic_search_blue), contentDescription = "")
            }
        }}
    }) {
        Column(modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .background(color = Color.White)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally) {
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp))
            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable(onClick = onCreatePost)
                    .padding(horizontal = 15.dp)){
                Text(text = "Создать запись", style = TextStyle(color = Color.Gray,
                    fontSize = 20.sp))
                Icon(painter = painterResource(id = R.drawable.ic_create_news), contentDescription = "")
            }
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 5.dp))

            FeedList(feed = feed)
        }
    }
}