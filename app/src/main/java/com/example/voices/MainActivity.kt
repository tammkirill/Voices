package com.example.voices


import android.content.Intent
import android.os.Bundle
import android.os.CountDownTimer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.voices.backend.SharedPreferences
import com.example.voices.main_ui.VoicesActivity
import com.example.voices.sign_in_sign_up.LoginActivity
import com.example.voices.ui.theme.MyApplicationTheme


class MainActivity() : ComponentActivity() {
    var counter = 0

    @ExperimentalComposeUiApi
    override fun onCreate(savedInstanceState: Bundle?) {
//        val sharedPreferences = SharedPreferences(this@MainActivity)
//        sharedPreferences.clearSharedPreference()
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    onBoarding()
                }
            }
        }
        onBoardingTimer()
    }

    fun onBoardingTimer(){
        object: CountDownTimer(500,1){
            val sharedPreferences = SharedPreferences(this@MainActivity)

            override fun onTick(millisUntilFinished: Long) {
                counter++
            }

            override fun onFinish() {
                if (sharedPreferences.getValueString("access_token") !=null){
                    val intent = Intent(this@MainActivity,VoicesActivity::class.java)
                    startActivity(intent)
                }
                else{
                    val intent = Intent(this@MainActivity, LoginActivity::class.java)
                    startActivity(intent)

                }

                this@MainActivity.finish()
            }

        }.start()
    }
}



@Composable()
@Preview()
fun AndroidPreview_onBoarding() {
        onBoarding()

}

@Composable()
fun onBoarding() {
    ConstraintLayout(modifier = Modifier
        .background(
            Brush.linearGradient(
                colors = listOf(
                    Color(0xff00A7CC),
                    Color(0xff0089CC),
                    Color(0xff0069CC)
                )
            )
        )
        .fillMaxSize()) {
        val (Voices, mdi_map_marker_check) = createRefs()
        ConstraintLayout(modifier = Modifier.constrainAs(mdi_map_marker_check) {
            centerHorizontallyTo(parent)
            bottom.linkTo(Voices.top)

        }) {
            val (Vector) = createRefs()
            Image(painter = painterResource(id = R.drawable.ic_mdi_map_marker_check), contentDescription = "")

            /* raw vector Vector should have an export setting */
        }
        Text("Voices",
            Modifier
                .wrapContentHeight(Alignment.Top)
                .constrainAs(Voices) {
                    centerHorizontallyTo(parent)
                    centerVerticallyTo(parent)

                }, style = LocalTextStyle.current.copy(color = Color(1.0f, 1.0f, 1.0f, 1.0f), textAlign = TextAlign.Left, fontSize = 48.0.sp))


    }
}

