package com.example.voices.sign_in_sign_up

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.LocalTextStyle
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.voices.R

@Composable()
fun VoicesLogo(modifier: Modifier = Modifier) {
    Image(painter = painterResource(id = R.drawable.ic_voices_blue),  contentDescription = "", modifier = modifier)
}


@Composable()
fun GreetingText(b_text: String, d_text: String, modifier: Modifier){
    Column(modifier = modifier) {
        Text(text = b_text,
            style = LocalTextStyle.current.copy(color = Color(0.0f, 0.0f, 0.0f, 1.0f),
                textAlign = TextAlign.Center, fontSize = 30.0.sp, fontWeight = FontWeight.Bold),modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 10.dp))


        Text(text = d_text,
            style = LocalTextStyle.current.copy(color = Color(0.0f, 0.0f, 0.0f, 1.0f),
                textAlign = TextAlign.Center, fontSize = 15.0.sp), modifier = Modifier.fillMaxWidth())
    }
}


@ExperimentalComposeUiApi
@Composable()
fun DataField(label: String, text: String, onTextChange: (String) -> Unit, modifier: Modifier = Modifier){
    val keyboardController = LocalSoftwareKeyboardController.current
    OutlinedTextField(
        value = text,
        onValueChange = onTextChange,
        label = { Text(label) },
        modifier = modifier,

        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
        ),
        keyboardActions = KeyboardActions(
            onDone = {keyboardController?.hide()}
        ),
    )
}


@Composable()
fun BlueBtn(text: String, onClick: () -> Unit, modifier: Modifier){
    Button(onClick = onClick,
        modifier.background(color = Color(0xff0069CC))
    ){
        Text(text = text,
            style = LocalTextStyle.current.copy(color = Color(1.0f, 1.0f, 1.0f, 1.0f),
                textAlign = TextAlign.Center, fontSize = 30.0.sp, fontWeight = FontWeight.Bold))
    }
}


@ExperimentalComposeUiApi
@Composable()
fun VerifyCodeRow(code:MutableState<String>,onComplete:()->Unit, modifier: Modifier = Modifier){

    val first = remember { mutableStateOf("") }
    val second = remember { mutableStateOf("") }
    val third = remember { mutableStateOf("") }
    val fourth = remember { mutableStateOf("") }
    val controller = remember { mutableStateOf(1) }
    val requester = remember{FocusRequester()}
    val focusManager = LocalFocusManager.current
    Row(modifier = modifier.fillMaxWidth()){
        val mod = Modifier
            .fillMaxWidth()
            .weight(1f)
        VerifyInputCell( first, controller.value == 1,mod,controller, requester, code,onComplete)
        VerifyInputCell(second, controller.value == 2,mod,controller, requester, code,onComplete)
        VerifyInputCell( third, controller.value == 3,mod,controller, requester, code,onComplete)
        VerifyInputCell(fourth, controller.value == 4,mod,controller, requester, code,onComplete)
    }
}


@ExperimentalComposeUiApi
@Composable()
fun VerifyInputCell(text: MutableState<String>, isChoose: Boolean, modifier: Modifier = Modifier,
                    controller: MutableState<Int>, requester: FocusRequester, code: MutableState<String>,onComplete:()->Unit
){
    val keyboardController = LocalSoftwareKeyboardController.current
    Box(modifier = modifier
        .padding(all = 5.dp)
        .wrapContentSize()
        .clip(RoundedCornerShape(20.dp))
        .background(color = if (isChoose) Color(R.color.voices_blue) else Color.White)) {
        val mod = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(color = if (isChoose) Color(R.color.voices_blue) else Color.White)
        OutlinedTextField(
            value = text.value,
            onValueChange = {
                text.value = it
                code.value += it
                if (controller.value<4){
                    controller.value+=1
                }
                else{
                    keyboardController?.hide()
                    onComplete()
                }
            },
            modifier = mod.focusRequester(requester) ,
            textStyle = TextStyle(color = if (isChoose) Color.White else Color(R.color.voices_blue), textAlign = TextAlign.Center,
                fontSize = 30.sp),
            singleLine = true,
            enabled = isChoose,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        )
        SideEffect {
            if (isChoose and (controller.value>1)){
                requester.requestFocus()
            }
        }
    }

}