package com.example.voices.main_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.example.voices.R
import com.example.voices.backend.HttpServices
import com.google.android.libraries.maps.CameraUpdateFactory
import com.google.android.libraries.maps.model.LatLng
import com.google.android.libraries.maps.model.MarkerOptions
import com.google.maps.android.ktx.awaitMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable()
fun MapUi(){
    val httpServices = HttpServices(LocalContext.current)
    val mapView = rememberMapViewWithLifecycle()
//    val points = runBlocking { async() { httpServices.getPoints() }.await()}

    Scaffold(topBar = {
        Surface (elevation = 10.dp, modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = {  }, modifier = Modifier.padding(end = 10.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_back_left), contentDescription = "")
                }
                Text(text = "Карта", style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold))
            }
        }
    }) {
        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth()
                .background(Color.White)
        ) {
            AndroidView({ mapView}) {mapView->
                CoroutineScope(Dispatchers.Main).launch {

                    val map = mapView.awaitMap()

                    map.uiSettings.isZoomControlsEnabled = true
                    val yaroslavl = LatLng(57.6299, 39.8737)
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(yaroslavl, 12f))
                    val points =
                        withContext(Dispatchers.Default) { httpServices.getPoints() }
                    for(point in points){
                        val latlng = LatLng(point.loc_lat.toDouble(), point.loc_long.toDouble())
                        map.addMarker(MarkerOptions().position(latlng))
                    }

//                    val markerOptions = MarkerOptions().position(pickUp)
//                    map.addMarker(markerOptions)
//
//                    val markerOptionsDestination = MarkerOptions().position(destination)
//                    map.addMarker(markerOptionsDestination)


                }



            }
        }
    }
}