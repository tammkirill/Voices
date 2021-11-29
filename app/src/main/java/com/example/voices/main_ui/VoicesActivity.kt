package com.example.voices.main_ui


import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.voices.R
import com.example.voices.kotlin.helloar.ArActivity
import com.example.voices.main_ui.HomeSection.*
import com.example.voices.main_ui.HomeSection.Map
import com.example.voices.sign_in_sign_up.LoginActivity
import com.example.voices.ui.theme.MyApplicationTheme
import com.example.voices.viewmodels.FeedViewModel
import com.example.voices.viewmodels.UsersViewModel
import kotlinx.coroutines.ObsoleteCoroutinesApi
import kotlinx.coroutines.newSingleThreadContext

val customThread = newSingleThreadContext("good thread")
val oneMoreThread = newSingleThreadContext("for news")
val feedViewModel = FeedViewModel()
val usersViewModel = UsersViewModel()

class VoicesActivity : ComponentActivity() {
    @ObsoleteCoroutinesApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toCreatePost = Intent(this, CreatePostActivity::class.java)
        val logout = Intent(this, LoginActivity::class.java)
        val toAr = Intent(this, ArActivity::class.java)
        feedViewModel.getFeed(this)

        usersViewModel.getUsers(this)

        usersViewModel.getProfile(this)

        usersViewModel.getFriends(this)
//        val sharedPreferences = SharedPreferences(this)
//        sharedPreferences.clearSharedPreference()
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    val sectionState = remember { mutableStateOf(Profile) }

                    val navItems = HomeSection.values()
                        .toList()
                    Scaffold(
                        bottomBar = {
                            BottomBar(
                                items = navItems,
                                currentSection = sectionState.value,
                                onSectionSelected = { sectionState.value = it},
                            )
                        }) { innerPadding ->
                        val modifier = Modifier.padding(innerPadding)
                        Crossfade(
                            modifier = modifier,
                            targetState = sectionState.value)
                        { section ->
                            when (section) {
                                Map -> MapUi()
                                Feed -> Feed {
                                    startActivity(toCreatePost)
                                    this.finish()
                                }
                                Ar -> AR { startActivity(toAr) }
                                Messenger -> MessengerUi()
                                Profile -> Profile ({
                                    startActivity(toCreatePost)
                                    this.finish()
                                },
                                    {
                                        startActivity(logout)
                                        this.finish()
                                    })
                            }
                        }
                    }
                }
            }
        }
    }
}


@Composable
private fun BottomBar(
    items: List<HomeSection>,
    currentSection: HomeSection,
    onSectionSelected: (HomeSection) -> Unit,
) {
    BottomNavigation(
        modifier = Modifier.height(bottomBarHeight),
        backgroundColor = MaterialTheme.colors.background,
        contentColor = contentColorFor(MaterialTheme.colors.background)
    ) {
        items.forEach { section ->

            val selected = section == currentSection

            val iconRes = if (selected) section.selectedIcon else section.icon

            BottomNavigationItem(
                icon = {
                    Image(
                        painterResource(id = iconRes),
                        modifier = Modifier.icon(),
                        contentDescription = ""
                    )
                },
                selected = selected,
                onClick = { onSectionSelected(section) },
                alwaysShowLabel = false
            )
        }
    }
}


private enum class HomeSection(
    val icon: Int,
    val selectedIcon: Int
) {
    Map(R.drawable.ic_outlined_map, R.drawable.ic_filled_map),
    Feed(R.drawable.ic_outlined_feed, R.drawable.ic_filled_feed),
    Ar(R.drawable.ic_outlined_ar, R.drawable.ic_filled_ar),
    Messenger(R.drawable.ic_outlined_messenger, R.drawable.ic_filled_messenger),
    Profile(R.drawable.ic_outlined_profile, R.drawable.ic_filled_profile)
}


fun Modifier.icon() = this.size(24.dp)


fun Modifier.defaultPadding() = this.padding(
    horizontal = horizontalPadding,
    vertical = verticalPadding
)

val verticalPadding = 12.dp
val horizontalPadding = 10.dp
val bottomBarHeight = 50.dp