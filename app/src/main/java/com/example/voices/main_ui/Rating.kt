package com.example.voices.main_ui

import android.content.res.Resources
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.voices.R
import com.example.voices.models.User
import kotlinx.coroutines.ObsoleteCoroutinesApi

@ObsoleteCoroutinesApi
@Composable()
fun Rating(navController: NavHostController){
    navController.enableOnBackPressed(true)
    val metrics = Resources.getSystem().displayMetrics
    val w = metrics.widthPixels
    val selectedTadIndex = remember{ mutableStateOf(0) }
    val (query, setQuery) = remember{ mutableStateOf("")}

    val users = usersViewModel.users
    val results by usersViewModel.searchUsers(query,users).collectAsState(mutableListOf<User>())
    Scaffold(topBar = {
        Surface (elevation = 10.dp, modifier = Modifier.wrapContentSize()) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp)
                    .padding(top = 10.dp, bottom = 5.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { navController.popBackStack() }, modifier = Modifier.padding(end = 10.dp)) {
                    Icon(painter = painterResource(id = R.drawable.ic_back_left), contentDescription = "")
                }

                Text(text = "Рейтинг", style = TextStyle(fontSize = 28.sp, fontWeight = FontWeight.Bold))

            }
        }
    }) {
        Column(modifier = Modifier
            .verticalScroll(state = rememberScrollState())
            .background(color = Color.White)
            .fillMaxWidth(),
            horizontalAlignment = Alignment.Start) {

            Row(verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 10.dp, vertical = 10.dp)){
                TabRow(
                    selectedTabIndex = selectedTadIndex.value,
                    modifier = Modifier.width((w/3).dp),
                    backgroundColor = Color.White
                ) {
                    Tab(selected = selectedTadIndex.value == 0,
                        onClick = { selectedTadIndex.value = 0 },
                    ) {
                        Text("Все пользователи")
                    }
                    Tab(selected = selectedTadIndex.value == 1,
                        onClick = { selectedTadIndex.value = 1 },

                        ) {
                        Text("Мои друзья")
                    }
                }

            }
            Divider(color = Color.Gray, thickness = 1.dp, modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 5.dp))

            TextField(
                value = query,
                onValueChange = setQuery,
                textStyle = TextStyle(fontSize = 17.sp),
                leadingIcon = ({Icon(Icons.Filled.Search, null, tint = Color.Gray)}),
                modifier = Modifier
                    .padding(horizontal = 15.dp, vertical = 5.dp)
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(24.dp))
                    .background(Color(0xFFE7F1F1)),
                placeholder = ({ Text(text = "Поиск") }),
                colors = TextFieldDefaults.textFieldColors(
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    backgroundColor = Color(0xFFE7F1F1),
                    cursorColor = Color.DarkGray
                )
            )

            if(query.isBlank()) UserList(type = "rating", users.value.sortedByDescending { it.rating })
            else UserList(type = "rating", users = results)
        }
    }
}