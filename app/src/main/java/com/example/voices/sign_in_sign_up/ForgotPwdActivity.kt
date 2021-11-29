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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.voices.R
import com.example.voices.ui.theme.MyApplicationTheme

class ForgotPwdActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toConfirm = Intent(this, ConfirmActivity::class.java)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        ForgotPwdPage(
                            onBack = {this.finish()},
                            onSend = {startActivity(toConfirm)}
                        )
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable()
fun ForgotPwdPage(onBack: ()->Unit, onSend: ()->Unit){
    val (mail, setMail) = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()
    Column() {

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)) {
            val(back_btn) = createRefs()

            IconButton(onClick = onBack, Modifier.constrainAs(back_btn) {
                start.linkTo(parent.start, margin = 10.dp)
                top.linkTo(parent.top, margin = 10.dp)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back),
                    contentDescription = ""
                )
            }
        }
        Column(modifier = Modifier
            .verticalScroll(state = scrollState)
            .background(color = Color.White)
            .padding(horizontal = 50.dp, vertical = 30.dp)

        ) {

            VoicesLogo(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(30.dp))
            GreetingText(b_text = "Забыли пароль?",
                d_text = "Пожалуйста, введите Вашу почту, мы отправим Вам 4-значный код для подтверждения Вашей учетной записи",
                Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(15.dp))
            DataField(label = "Почта", text = mail, onTextChange = setMail,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(20.dp))
            BlueBtn(text = "Отправить", onClick = onSend,
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth())
        }
    }
}
