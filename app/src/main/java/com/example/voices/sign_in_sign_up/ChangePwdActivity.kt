package com.example.voices.sign_in_sign_up

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

class ChangePwdActivity : ComponentActivity() {
    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                        ChangePwdPage()
                }
            }
        }
    }
}


@ExperimentalComposeUiApi
@Composable()
fun ChangePwdPage(){
    val (new_pwd, setNewPwd) = remember { mutableStateOf("") }
    val (pwd_check, setNewPwdCheck) = remember { mutableStateOf("") }
    val scrollState = rememberScrollState()

    Column() {

        ConstraintLayout(modifier = Modifier
            .fillMaxWidth()
            .background(color = Color.White)) {
            val(back_btn, step) = createRefs()

            IconButton(onClick = { /*TODO*/ }, Modifier.constrainAs(back_btn) {
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
            .padding(all = 50.dp)
        ) {

            VoicesLogo(Modifier.fillMaxWidth())
            Spacer(modifier = Modifier.padding(30.dp))
            GreetingText(b_text = "Смена пароля",
                d_text = "Придумайте новый пароль",
                Modifier
                    .fillMaxWidth())
            Spacer(modifier = Modifier.padding(20.dp))
            DataField(label = "Новый пароль", text = new_pwd, onTextChange = setNewPwd,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(5.dp))
            DataField(label = "Новый пароль", text = pwd_check, onTextChange = setNewPwdCheck,
                Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)))
            Spacer(modifier = Modifier.padding(20.dp))
            BlueBtn(text = "Изменить", onClick = { /*TODO*/ },
                Modifier
                    .clip(RoundedCornerShape(10.dp))
                    .fillMaxWidth())
        }
    }
}