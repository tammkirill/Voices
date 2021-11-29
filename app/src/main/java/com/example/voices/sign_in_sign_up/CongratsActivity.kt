package com.example.voices.sign_in_sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.voices.R
import com.example.voices.backend.SharedPreferences
import com.example.voices.main_ui.VoicesActivity
import com.example.voices.ui.theme.MyApplicationTheme


class CongratsActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toProfile = Intent(this, VoicesActivity::class.java)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        CongratsPage(this, onContinue = {
                            startActivity(toProfile)
                            this.finish()
                        }
                        )
                }
            }
        }
    }
}


@Composable()
fun CongratsPage(context: Context, onContinue:()->Unit){
    val sp = SharedPreferences(context)
    println(sp.getValueString("access_token"))
    val scrollState = rememberScrollState()
    Column(
        Modifier
            .wrapContentWidth(Alignment.CenterHorizontally)
            .verticalScroll(
                state = scrollState,
            )
            .background(color = Color.White)
            .padding(horizontal = 50.dp, vertical = 30.dp)
    ) {

        VoicesLogo(Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(30.dp))
        GreetingText(b_text = "Поздравляем!",
            d_text = "Ваша учетная запись была успешно создана. Нажмите 'Продолжить', чтобы начать использовать приложение",
            Modifier
                .fillMaxWidth())
        Image(painter = painterResource(id = R.drawable.ic_ads),
            contentDescription = "",modifier = Modifier.fillMaxWidth())
        BlueBtn(text = "Продолжить", onClick = onContinue,
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth())

    }
}