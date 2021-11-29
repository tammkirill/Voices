package com.example.voices.sign_in_sign_up

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.voices.ui.theme.MyApplicationTheme
import com.example.voices.R


class ConfirmActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toChangePwd = Intent(this, ChangePwdActivity::class.java)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        ConfirmPage(
                            toChangePwd = {startActivity(toChangePwd)},
                            onBack = {this.finish()}
                        )
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable
fun ConfirmPage(toChangePwd: ()->Unit, onBack:()->Unit){
    val scrollState = rememberScrollState()
    val code = remember { mutableStateOf("") }
    Column() {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxWidth()
                .background(color = Color.White)
        ) {
            val (back_btn, step) = createRefs()

            IconButton(onClick = onBack, Modifier.constrainAs(back_btn) {
                start.linkTo(parent.start, margin = 10.dp)
                top.linkTo(parent.top, margin = 10.dp)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back),
                    contentDescription = ""
                )
            }
            Text(text = "Шаг 2/2",
                style = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp),
                modifier = Modifier.constrainAs(step) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(back_btn)
                })
        }
        Column(
            modifier = Modifier
                .verticalScroll(state = scrollState)
                .background(color = Color.White)
                .padding(all = 50.dp)
        ) {

            VoicesLogo(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(30.dp))
            GreetingText(
                b_text = "Подтвержение",
                d_text = "Введите 4-значный код, который мы отправили на адрес ",
                Modifier
                    .fillMaxWidth()
            )
            Text(text = "Ivanov@gmail.com", style = TextStyle(color = Color(R.color.voices_blue),
                fontSize = 15.sp),textAlign = TextAlign.Center ,modifier = Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(20.dp))
            VerifyCodeRow(code, onComplete = toChangePwd,Modifier.fillMaxWidth())
        }
    }
}