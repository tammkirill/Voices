package com.example.voices.main_ui

import android.content.res.Resources
import android.os.Bundle
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.LinearProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.example.voices.R
import com.example.voices.models.Post
import com.example.voices.models.User
import com.example.voices.viewmodels.LikeViewModel
import com.google.android.libraries.maps.MapView
import kotlinx.coroutines.flow.MutableStateFlow
import java.lang.Float.max


@Composable
fun CircularProgressBar(
    modifier: Modifier = Modifier,
    progress: Float = 0f,
    progressMax: Float = 100f,
    progressBarColor: Color = Color.Black,
    progressBarWidth: Dp = 10.dp,
    backgroundProgressBarColor: Color = Color.Gray,
    backgroundProgressBarWidth: Dp = 3.dp,
    roundBorder: Boolean = false,
    startAngle: Float = 0f
) {
    Box(modifier = Modifier.wrapContentSize(),contentAlignment = Alignment.Center){
        Canvas(modifier = modifier.fillMaxSize()) {

            val canvasSize = size.minDimension

            val radius =
                canvasSize / 2 - maxOf(backgroundProgressBarWidth, progressBarWidth).toPx() / 2

            drawCircle(
                color = backgroundProgressBarColor,
                radius = radius,
                center = size.center,
                style = Stroke(width = backgroundProgressBarWidth.toPx())
            )

            drawArc(
                color = progressBarColor,
                startAngle = 270f + startAngle,
                sweepAngle = (progress / progressMax) * 360f,
                useCenter = false,
                topLeft = size.center - Offset(radius, radius),
                size = Size(radius * 2, radius * 2),
                style = Stroke(
                    width = progressBarWidth.toPx(),
                    cap = if (roundBorder) StrokeCap.Round else StrokeCap.Butt
                )
            )
        }
        Text(text = "${progress.toInt()}%", style = TextStyle(fontWeight = FontWeight.Bold))
    }
}

@Composable
fun ProfileImage(modifier: Modifier = Modifier) {
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    val context = LocalContext.current
    val image = remember {
        ContextCompat.getDrawable(context, R.mipmap.ic_user)?.toBitmap(w/6, w/6)?.asImageBitmap()!!
    }
    val shape = CircleShape
            Image(
                image,
                modifier = modifier.clip(shape),
                contentDescription = ""
            )
}

fun sum(a:Float, b:Float):Float{
    return max(a+b, 1f)
}



@Composable
fun NewsCard(modifier: Modifier = Modifier, post: Post){

    val likeViewModel = LikeViewModel(post)
    val c = LocalContext.current
    val isLiked = likeViewModel.isLiked
    val isDisliked = likeViewModel.isDisliked
    val likeTint = remember{ mutableStateOf(Color.LightGray)}
    val dislikeTint = remember{ mutableStateOf(Color.LightGray)}
    val vote_for = remember{ mutableStateOf(post.vote_for)}
    val vote_against = remember{ mutableStateOf(post.vote_against)}

    if (isLiked.value) likeTint.value = Color.Black
    if (isDisliked.value) dislikeTint.value = Color.Black

    Column(modifier = modifier.background(color = Color.White)){
        Text(text = post.topic, style = TextStyle(fontSize = 24.sp,
            fontWeight = FontWeight.Bold, textAlign = TextAlign.Left), modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp))
        Text(text = post.main_text, style = TextStyle(fontSize = 18.sp,
            textAlign = TextAlign.Left), modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp))

        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)) {
            val progress = vote_for.value.toFloat()/sum(vote_against.value.toFloat(),vote_for.value.toFloat())

                Text("${ String.format("%.0f", progress*100)}%",style = TextStyle(fontWeight = FontWeight.Bold),
                    modifier = Modifier.padding(end = 10.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Text("Поддержали", modifier = Modifier.padding(bottom = 10.dp))
                    LinearProgressIndicator(
                        progress = progress,
                        color = Color(R.color.voices_blue),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
        }
        Spacer(Modifier.padding(3.dp))
        Row(verticalAlignment = Alignment.Top, modifier = Modifier.padding(vertical = 5.dp, horizontal = 15.dp)) {
            val progress = vote_against.value.toFloat()/sum(vote_against.value.toFloat(),vote_for.value.toFloat())
            Text("${String.format("%.0f", progress*100)}%",style = TextStyle(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(end = 10.dp))
            Column(modifier = Modifier.fillMaxWidth()) {

                Text("Против", modifier = Modifier.padding(bottom = 10.dp))
                LinearProgressIndicator(
                    progress = progress,
                    color = Color(R.color.voices_blue),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
        Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp))
        
        Row(modifier = Modifier
            .padding(horizontal = 10.dp)
            .wrapContentWidth()
            .height(30.dp)) {
            Row(modifier.clickable {
                val new_value:Boolean? = if (isLiked.value) null else true
                likeTint.value = if(new_value!=null) Color.Black else Color.LightGray
                dislikeTint.value=Color.LightGray
                if(new_value!=null) vote_for.value+=1 else vote_for.value-=1
                if((new_value!=null) and isDisliked.value) vote_against.value -= 1
                likeViewModel.updateLike(new_value,c)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_like), contentDescription = "",
                modifier = Modifier.padding(5.dp),tint = likeTint.value)
                Text(text = "За", style = TextStyle(color = Color.Gray), modifier = Modifier.padding(5.dp))
            }
            Spacer(modifier = Modifier.padding(horizontal = 25.dp))
            Row(modifier.clickable {
                val new_value = if(isDisliked.value) null else false
                dislikeTint.value = if(new_value!=null) Color.Black else Color.LightGray
                likeTint.value=Color.LightGray
                if(new_value!=null) vote_against.value+=1 else vote_against.value-=1
                if((new_value!=null) and isLiked.value) vote_for.value -= 1
                likeViewModel.updateLike(new_value,c)
            }) {
                Icon(painter = painterResource(id = R.drawable.ic_dislike), contentDescription = "",
                    modifier = Modifier.padding(5.dp), tint = dislikeTint.value)
                Text(text = "Против", style = TextStyle(color = Color.Gray), modifier = Modifier.padding(5.dp))
            }
        }
        Divider(color = Color.Gray, thickness = 8.dp, modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp))

    }
}


@Composable()
fun FeedList(modifier: Modifier = Modifier, feed: MutableStateFlow<MutableList<Post>>){
    Column(modifier = modifier) {
        for (post in feed.value) NewsCard(post=post)
    }
}


@Composable()
fun UserList(type:String, users:List<User>, modifier: Modifier = Modifier){

    Column(modifier = modifier.fillMaxWidth()) {
        for(user in users) UserCard(user = user,type = type)
    }
}


@Composable()
fun UserCard(user:User, type: String){
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    Row(horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            ProfileImage(modifier = Modifier.size((w/15).dp))
            Column(
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.Center) {
                Text(text = "${user.first_name}", style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
                if(user.city !=null) Text(text = user.city!!, style = TextStyle(fontSize = 14.sp))
            }
        }
        if (type == "rating") Row(
            modifier = Modifier
                .fillMaxHeight()
                .wrapContentWidth()
                .padding(horizontal = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = user.rating.toString(),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color(R.color.voices_blue)),
                modifier = Modifier.padding(horizontal = 5.dp)
                )
            Icon(painter = painterResource(id = R.drawable.ic_rating), contentDescription = "",tint = Color(R.color.voices_blue))
        }
        else
            Icon(
                painter = painterResource(id = R.drawable.ic_to_message),
                contentDescription = "",
                modifier = Modifier.padding(horizontal = 10.dp)
            )
    }
}


@Composable
fun rememberMapViewWithLifecycle(): MapView {
    val context = LocalContext.current
    val mapView = remember {
        MapView(context).apply {
            id = R.id.map
        }
    }

    // Makes MapView follow the lifecycle of this composable
    val lifecycleObserver = rememberMapLifecycleObserver(mapView)
    val lifecycle = LocalLifecycleOwner.current.lifecycle
    DisposableEffect(lifecycle) {
        lifecycle.addObserver(lifecycleObserver)
        onDispose {
            lifecycle.removeObserver(lifecycleObserver)
        }
    }

    return mapView
}

@Composable
fun rememberMapLifecycleObserver(mapView: MapView): LifecycleEventObserver =
    remember(mapView) {
        LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_CREATE -> mapView.onCreate(Bundle())
                Lifecycle.Event.ON_START -> mapView.onStart()
                Lifecycle.Event.ON_RESUME -> mapView.onResume()
                Lifecycle.Event.ON_PAUSE -> mapView.onPause()
                Lifecycle.Event.ON_STOP -> mapView.onStop()
                Lifecycle.Event.ON_DESTROY -> mapView.onDestroy()
                else -> throw IllegalStateException()
            }
        }
    }


//val points = mutableListOf<LatLng>(
//    LatLng(57.625791, 39.871097),
//    LatLng(57.626671, 39.894116),
//    LatLng(57.614713, 39.865421),
//    LatLng(57.629155, 39.835873),
//    LatLng(57.610094, 39.855119),
//)