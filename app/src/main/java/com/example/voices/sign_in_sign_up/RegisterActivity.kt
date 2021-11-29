package com.example.voices.sign_in_sign_up

import android.content.Context
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
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.voices.R
import com.example.voices.backend.HttpServices
import com.example.voices.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class RegisterActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toCongrats = Intent(this, CongratsActivity::class.java)
        //sharedPref = getSharedPreferences("com.example.voices", Context.MODE_PRIVATE)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        RegistrationPage(
                            onContinue = {startActivity(toCongrats)},
                            onBack = {this.finish()},
                            context = this
                        )
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable()
fun RegistrationPage(onContinue: ()-> Unit, onBack: ()-> Unit, context: Context) {
    val (name, setName) = remember { mutableStateOf("") }
    val (mail, setMail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val httpServices = HttpServices(context)
    Column() {

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)) {
            val(back_btn, step) = createRefs()

            IconButton(onClick = onBack, Modifier.constrainAs(back_btn) {
                start.linkTo(parent.start, margin = 10.dp)
                top.linkTo(parent.top, margin = 10.dp)
            }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_baseline_arrow_back),
                    contentDescription = ""
                )
            }
            Text(text = "Шаг 1/2",
                style = LocalTextStyle.current.copy(color = Color.Black, fontSize = 18.sp),
                modifier = Modifier.constrainAs(step) {
                    top.linkTo(parent.top)
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(back_btn)
                })
        }
        Column(modifier = Modifier
            .verticalScroll(state = scrollState)
            .background(color = Color.White)
            .padding(all = 50.dp)
        ) {

            VoicesLogo(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(30.dp))
            GreetingText(b_text = "Регистрация",
                d_text = "Пожалуйста, укажите следующие детали для вашей новой учетной записи",
                Modifier
                    .fillMaxWidth())
            Spacer(modifier = Modifier.padding(20.dp))
            DataField(label = "Имя", text = name, onTextChange = setName,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(5.dp))
            DataField(label = "почта", text = mail, onTextChange = setMail,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(5.dp))
            DataField(label = "пароль", text = password, onTextChange = setPassword,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(20.dp))
            BlueBtn(text = "Продолжить", onClick = {
                runBlocking{
                val success = async {
                    withContext(Dispatchers.IO){
                        httpServices.registration(name=name,email = mail,pwd = password)
                    }
                }

                if(success.await()) {
                    onContinue()
                }
                                                   }
            },
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth())
        }
    }
}