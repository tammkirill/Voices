package com.example.voices.main_ui

import android.content.res.Resources
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.voices.R
import com.example.voices.backend.SharedPreferences
import com.example.voices.models.User
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow


//@Preview()
//@Composable()
//fun preview(){
//    ProfilePage({})
//}

@Composable()
fun Profile(onCreatePost:()->Unit, logout:()->Unit){
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = "profile"){
        composable("profile"){ProfilePage(onCreatePost = onCreatePost,navController, logout)}
        composable("rating"){Rating(navController = navController)}
        composable("friends"){ Friends(navController = navController)}
    }

}


@ObsoleteCoroutinesApi
@Composable()
fun ProfilePage(onCreatePost:()->Unit,navController:NavHostController, logout:()->Unit){
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    val sharedPreferences = SharedPreferences(LocalContext.current)

    val feed = feedViewModel.feed

    val friends = usersViewModel.friends
    val selectedTadIndex = remember{ mutableStateOf(0)}
    Column(modifier = Modifier
        .verticalScroll(state = rememberScrollState())
        .background(color = Color.White)
        .fillMaxWidth()
        ,horizontalAlignment = Alignment.CenterHorizontally) {
        Row(modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_notifications), contentDescription = "")
            }
            IconButton(onClick = {
                sharedPreferences.clearSharedPreference()
                logout()
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_baseline_exit), contentDescription = "")
            }
        }
        ProfileInfo(){
            navController.navigate("rating")
        }
        Text(text = "Друзья", style = TextStyle(fontWeight = FontWeight.Bold,
            fontSize = 25.sp, textAlign = TextAlign.Left), modifier = Modifier
            .padding(horizontal = 25.dp)
            .clickable { navController.navigate("friends") }
            .fillMaxWidth())
        Spacer(modifier = Modifier.padding(3.dp))
        FriendsRow(friends)
        Spacer(modifier = Modifier.padding(3.dp))
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
        Row(verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 15.dp)){
            TabRow(
                selectedTabIndex = selectedTadIndex.value,
                modifier = Modifier.width((w/5).dp),
                backgroundColor = Color.White
            ) {
                Tab(selected = selectedTadIndex.value == 0,
                    onClick = { selectedTadIndex.value = 0 },
                ) {
                    Text("Все записи")
                }
                Tab(selected = selectedTadIndex.value == 1,
                    onClick = { selectedTadIndex.value = 1 },

                    ) {
                    Text("Мои записи")
                }
            }
            Icon(painter = painterResource(id = R.drawable.ic_search), contentDescription = "")
        }
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp))

        if(selectedTadIndex.value == 0) FeedList(feed =feed) else FeedList(feed = feed)

    }
}


@ObsoleteCoroutinesApi
@Composable()
fun ProfileInfo(modifier: Modifier = Modifier, toRating:()->Unit) {

    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    val profile = usersViewModel.profile
    val ratingProgress = remember{ mutableStateOf((profile.rating.toFloat()))}
    val context = LocalContext.current
    val image = remember {
        ContextCompat.getDrawable(context, R.mipmap.ic_user)?.toBitmap(w/6, w/6)?.asImageBitmap()!!
    }
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier) {
        Image(image, contentDescription = "",
        //modifier = Modifier.size((w/6).dp)
        )
        Spacer(modifier = Modifier.padding(3.dp))
        Text(text = "${profile.first_name}", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 30.sp))
        Spacer(modifier = Modifier.padding(3.dp))
        if(profile.city !=null )Text(text = profile.city!!, style = TextStyle(fontSize = 15.sp))
        Spacer(modifier = Modifier.padding(3.dp))
        Text(text = profile.email, style = TextStyle(fontSize = 15.sp, color = Color(R.color.voices_blue)))
        Spacer(modifier = Modifier.padding(10.dp))

        Row(modifier = Modifier
            .padding(horizontal = 5.dp)
            .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround){
            ProgressBar(progress = 70f, text = "947 баллов доверия")
            ProgressBar(progress = ratingProgress.value, text = "${profile.rating} баллов рейтинга",
            modifier = Modifier.clickable { toRating() }
                )
            ProgressBar(progress = 22f, text = "947 баллов вовлеченности")
        }
        Spacer(modifier = Modifier.padding(4.dp))

    }
}


@Composable()
fun ProgressBar(progress:Float, text:String, modifier: Modifier = Modifier){
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    Column(
        modifier = modifier
            .wrapContentHeight()
            .width((w / 13).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        CircularProgressBar(
            progress = progress,
            modifier = Modifier.size((w / 16).dp),
            progressBarColor = Color(R.color.voices_blue)
        )
        Spacer(Modifier.padding(3.dp))
        Text(text = text,modifier = Modifier.fillMaxWidth(),style = TextStyle(textAlign = TextAlign.Center, fontSize = 18.sp))
    }

}


@Composable()
fun FriendsRow(friends: MutableStateFlow<MutableList<User>>){
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    LazyRow(modifier = Modifier.padding(start = 15.dp, bottom = 10.dp)) {
        itemsIndexed(friends.value){ _, friend ->
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .width((w / 13).dp)
                    .clickable { }
            ) {
                ProfileImage(modifier = Modifier.size((w/13).dp))
                Text(text = "${friend.first_name}", style = TextStyle(textAlign = TextAlign.Center, fontSize = 15.sp))
            }
            Spacer(modifier = Modifier.padding(4.dp))
        }
    }
}






