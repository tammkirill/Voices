package com.example.voices.main_ui

import android.content.Intent
import android.content.res.Resources
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.TextFieldDefaults.outlinedTextFieldColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import com.example.voices.R
import com.example.voices.backend.HttpServices
import com.example.voices.ui.theme.MyApplicationTheme
import com.example.voices.viewmodels.KeyWordsViewModel
import kotlinx.coroutines.*

class CreatePostActivity : ComponentActivity() {
    @ExperimentalFoundationApi
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, VoicesActivity::class.java)
        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colors.background) {
                        CreatePost {
                            startActivity(intent)
                            this.finish() }
                }
            }
        }
    }
}


@ObsoleteCoroutinesApi
@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable()
fun CreatePost(onExit:()->Unit){
    val text = remember{ mutableStateOf("")}
    val topic = remember { mutableStateOf("") }
    val keyWordsViewModel = KeyWordsViewModel()
    val httpServices = HttpServices(LocalContext.current)
    val context = LocalContext.current
    val onDone = {
        runBlocking { async(){
            withContext(Dispatchers.Default){
                val keywords = keyWordsViewModel.keyWords
                var main_text = text.value
                for(k in keywords){
                    main_text += "#$k"
                }
                val success = httpServices.addPost(
                    main_text = main_text,
                    topic = topic.value
                )
                println(success)
                if(success){
                    onExit()
                }
        }} }

    }
    Scaffold(topBar = {TopBar(onExit, onDone)}, bottomBar = { BottomBar(topic, keyWordsViewModel)}) {
            Surface(color = Color(0xffFDFDFD),modifier = Modifier.wrapContentSize()) {
                OutlinedTextField(
                    value = text.value,
                    onValueChange = {text.value = it},
                    label = { Text("О чем вы хотите рассказать?")},
                    modifier = Modifier.fillMaxSize(),
                    textStyle = TextStyle(fontSize = 30.sp),
                    colors = outlinedTextFieldColors(
                        focusedBorderColor = Color(0xffFDFDFD),
                        unfocusedBorderColor =Color(0xffFDFDFD)
                    )
                )
            }
    }
}


@ObsoleteCoroutinesApi
@Composable()
private fun TopBar(onExit: () -> Unit, onDone: ()-> Deferred<Unit>){
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    val profile = usersViewModel.profile
    val c = LocalContext.current
    val image = remember {
        ContextCompat.getDrawable(c, R.mipmap.ic_user)?.toBitmap(w/6, w/6)?.asImageBitmap()!!
    }
    Surface (elevation = 15.dp, modifier = Modifier.wrapContentSize()) {
        Row(horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(horizontal = 10.dp)
                .padding(top = 10.dp, bottom = 5.dp),
        ) {
            IconButton(onClick = onExit) {
                Icon(painter = painterResource(id = R.drawable.ic_close), contentDescription = "")
            }
            Row(verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.wrapContentWidth()
                ) {
                Image(image, contentDescription = "",
                    modifier = Modifier.size((w/20).dp))
                Spacer(modifier = Modifier.padding(horizontal = 5.dp))
                Text(text = profile.first_name!!, style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp))
            }
            IconButton(onClick =  {runBlocking{onDone().await()}
                feedViewModel.getFeed(c)}) {
                Icon(painter = painterResource(id = R.drawable.ic_check), contentDescription = "")
            }
        }
    }
}


@ExperimentalFoundationApi
@ExperimentalComposeUiApi
@Composable()
private fun BottomBar( topic:MutableState<String>, keyWordsViewModel: KeyWordsViewModel) {
    //val keyWords = remember{ mutableStateOf(mutableListOf("Москва", "Детская площадка", "Дороги")) }
    val text = remember { mutableStateOf("")}
    val keyWords = keyWordsViewModel.keyWords
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(elevation = 15.dp, modifier = Modifier.wrapContentSize()) {
        Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
            Row(
                modifier = Modifier
                    .padding(6.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Заголовок:", style = TextStyle(fontSize = 18.sp),
                    modifier = Modifier
                        .wrapContentWidth()
                        .padding(horizontal = 10.dp)
                )
                OutlinedTextField(
                    value = topic.value,
                    onValueChange = {topic.value = it},
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                        //.height(45.dp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = ImeAction.Done,
                    ),
                    keyboardActions = KeyboardActions(
                        onDone = {
                            keyboardController?.hide()
                        }
                    )
                )
            }
        Row(
            modifier = Modifier
                .padding(horizontal = 6.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Ключевые слова:", style = TextStyle(fontSize = 18.sp),
                modifier = Modifier
                    .wrapContentWidth()
                    .padding(horizontal = 10.dp)
            )
            OutlinedTextField(
                value = text.value,
                onValueChange = {text.value = it},
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    //.height(45.dp)
                ,
                keyboardOptions = KeyboardOptions(
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyWordsViewModel.addWord(text.value)
                        text.value = ""
                        keyboardController?.hide()
                    }
                )
            )
        }
        LazyVerticalGrid(
            cells = GridCells.Fixed(2),
            modifier = Modifier
                .padding(horizontal = 10.dp)
                .wrapContentHeight()
                .fillMaxWidth()
                .align(Alignment.Start)
        ) {
            items(keyWords.size) {
                KeyWord(it,keyWordsViewModel)
            }
        }
            Divider(color = Color(0xffC4C4C4), thickness = 1.dp, modifier = Modifier
                .fillMaxWidth())
        Row(modifier = Modifier.height(bottomBarHeight)) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_create_news),
                    contentDescription = ""
                )
            }
        }
    }
}
}


@Composable()
private fun KeyWord(id: Int, keyWordsViewModel: KeyWordsViewModel, modifier:Modifier = Modifier){
    Surface(elevation = 20.dp, modifier = modifier.wrapContentSize().clip(RoundedCornerShape(5.dp))) {
        Row(
//            Modifier
//                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
                Text(text = keyWordsViewModel.keyWords[id], style = TextStyle(color = Color(R.color.voices_blue), fontSize = 18.sp))
                IconButton(onClick = { keyWordsViewModel.removeWord(id)}) {
                    Icon(painter = painterResource(id = R.drawable.ic_baseline_close), contentDescription = "")
                }
        }
    }
}


