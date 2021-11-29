package com.example.voices.sign_in_sign_up

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voices.backend.HttpServices
import com.example.voices.main_ui.VoicesActivity
import com.example.voices.ui.theme.MyApplicationTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext


class LoginActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val toProfile = Intent(this, VoicesActivity::class.java)
        val toCreateAcc = Intent(this, RegisterActivity::class.java)

        setContent {
            MyApplicationTheme {
                Surface(color = MaterialTheme.colors.background) {
                    LoginPage (
                        onLogin = {
                            startActivity(toProfile)
                            this.finish()
                        },
                        onCreateAcc = { startActivity(toCreateAcc) },
                        context = this
                    )
                }
            }
        }
    }
}

@ExperimentalComposeUiApi
@Composable()
fun LoginPage(onLogin:()->Unit, onCreateAcc: ()-> Unit, context: Context){
    val (email, setEmail) = remember { mutableStateOf("") }
    val (password, setPassword) = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    val httpServices = HttpServices(context)
    Column(modifier = Modifier
        .verticalScroll(state = scrollState)
        .background(color = Color.White)
        .padding(all = 50.dp)
    ){

        val forgot_pwd_text = buildAnnotatedString {
            append("Нет аккаунта? ")
            withStyle(SpanStyle(color = Color(0xff0069CC))) {
                append("Создать аккаунт")
            }
        }
        VoicesLogo(Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(30.dp))
        GreetingText(b_text = "Добро пожаловать", d_text = "Войдите, чтобы продолжить",
            Modifier.fillMaxWidth())
        Spacer(modifier = Modifier.padding(20.dp))
        DataField(label = "почта", text = email, onTextChange = setEmail,
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
        )
        Spacer(modifier = Modifier.padding(5.dp))
        DataField(label = "пароль", text = password, onTextChange = setPassword,
            Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp)))
        Spacer(modifier = Modifier.padding(20.dp))
        BlueBtn(text = "Войти", onClick = {runBlocking{
            val success = async(){
                withContext(Dispatchers.IO){
                    httpServices.login(email = email, pwd=password)
                }
            }
            println(success.await())
            if(success.await())
                onLogin()
        }},
            Modifier
                .clip(RoundedCornerShape(10.dp))
                .fillMaxWidth())
        Spacer(modifier = Modifier.padding(8.dp))
        Text(text = forgot_pwd_text,
            Modifier
                .clickable(onClick = onCreateAcc)
                .fillMaxWidth(),
            style = LocalTextStyle.current.copy(fontSize = 18.0.sp,textAlign = TextAlign.Center ))
    }
}