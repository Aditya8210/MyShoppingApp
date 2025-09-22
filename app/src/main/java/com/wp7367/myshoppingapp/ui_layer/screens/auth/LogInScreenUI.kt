package com.wp7367.myshoppingapp.ui_layer.screens.auth

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.R
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes
import com.wp7367.myshoppingapp.ui_layer.viewModel.MyViewModel


@Composable
fun  LoginScreen(viewModel: MyViewModel = hiltViewModel(), navController: NavController){


//  ~ Here State is Collect ~

    val lgState by viewModel.loginUser.collectAsStateWithLifecycle()



    val context = LocalContext.current




    // ----- Media Query style breakpoints -----
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val titleSize: Int
    val fieldWidth: Dp
    val buttonWidth: Dp
    val circleBig: Dp
    val circleSmall: Dp
    val circleBigOffsetX: Dp
    val circleBigOffsetY: Dp
    val circleSmallOffsetY: Dp

    when {
        screenWidth < 360.dp -> {
            // Small phone
            titleSize = 24
            fieldWidth = 260.dp
            buttonWidth = 200.dp
            circleBig = 160.dp
            circleSmall = 140.dp
            circleBigOffsetX = 120.dp
            circleBigOffsetY = (-40).dp
            circleSmallOffsetY = 20.dp

        }
        screenWidth < 600.dp -> {
            // Normal phone
            titleSize = 32
            fieldWidth = 320.dp
            buttonWidth = 250.dp
            circleBig = 220.dp
            circleSmall = 180.dp
            circleBigOffsetX = 210.dp     //
            circleBigOffsetY = (-60).dp
            circleSmallOffsetY = 35.dp
        }
        else -> {
            // Tablet / Foldable
            titleSize = 40
            fieldWidth = 400.dp
            buttonWidth = 300.dp
            circleBig = 300.dp
            circleSmall = 260.dp
            circleBigOffsetX = 250.dp
            circleBigOffsetY = (-80).dp
            circleSmallOffsetY = 40.dp
        }
    }


//     ~ Here State is Manage ~
    when{
        lgState.isLoading ->{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator()

            }

        }
        lgState.error != null ->{
            Toast.makeText(context,lgState.error,Toast.LENGTH_SHORT).show()

        }
        lgState.data != null ->{
            Toast.makeText(context, "Login Successfully", Toast.LENGTH_SHORT).show()

            navController.navigate(Routes.HomeScreen)

        }



    }


//    ~ Screen UI ~



    Column (modifier = Modifier
             .fillMaxSize()
             .fillMaxWidth()
             .background(color = Color.White),




    ){



        val email = remember { mutableStateOf("") }
        val password = remember { mutableStateOf("") }



        // New UI



        Row(
            modifier = Modifier.fillMaxWidth())
        {
            Box(
                modifier = Modifier
                    .size(circleBig)
                    .offset(x = circleBigOffsetX, y = circleBigOffsetY)
                    .align(Alignment.Top)
            ) {

                Image(
                    painter = painterResource(id = R.drawable.circle332212),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())

            }

        }


        Column(
            modifier = Modifier.fillMaxWidth(),

            ) {

            Text(
                text = "LOGIN", modifier = Modifier.padding(start = 41.dp),
                fontSize = titleSize.sp,
                fontStyle = FontStyle.Normal,
                fontWeight = FontWeight.ExtraBold,
                color = Color.Blue
            )

        }
        Spacer(modifier = Modifier.padding(10.dp))


        Column(modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally)
        {

            OutlinedTextField(value  = email.value,
                onValueChange = {email.value = it},
                label = { Text(text = "Gmail")
                },
                singleLine = true,
                modifier = Modifier.width(fieldWidth)
            )

            Spacer(modifier = Modifier.height(4.dp))


            OutlinedTextField(value  =password.value ,
                onValueChange = {password.value = it},
                label = {Text(text = "Password")},
                singleLine = true,
                modifier = Modifier.width(fieldWidth)

            )



            Row (modifier = Modifier.fillMaxWidth().
            padding(end = 24.dp),
                horizontalArrangement = Arrangement.End,

                )
            {
                Text(text = "Forgot Password?", modifier = Modifier.
                padding(10.dp).

                clickable(onClick =
                    {
                        Toast.makeText(context,"Forgot Password",Toast.LENGTH_SHORT).show()

                    }))
            }



        }

        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally){
            Button(onClick = {viewModel.loginUser(email.value,password.value)},
                modifier = Modifier.width(buttonWidth))
            {
                Text(text = "Login")

            }


            Spacer(modifier = Modifier.height(19.dp))

            Row {
                Text(text = "Don't have An Account? ")
                Text(
                    text = "Sign Up",
                    color = Color.Blue,
                    modifier = Modifier.clickable {
                        Toast.makeText(context, "Sign Up ", Toast.LENGTH_SHORT).show()
                        navController.navigate(Routes.SignUpScreen)
                    }
                )
            }

        }
        Spacer(modifier = Modifier.height(10.dp))

        Column(modifier = Modifier.fillMaxWidth().fillMaxSize(),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Bottom) {

            Row(modifier = Modifier.fillMaxWidth()) {
                Box(modifier = Modifier
                    .size(circleSmall)
                    .offset(y = circleSmallOffsetY)

                    .align(Alignment.Bottom)
                ){


                    Image(painter = painterResource(id = R.drawable.circle33221),
                        contentDescription = null,
                        modifier = Modifier.fillMaxSize())
                }

            }
        }


//    ----------------------------------------------------------------------------------------

//           Old UI

//        OutlinedTextField(
//            value = email.value,
//            onValueChange = { email.value = it },
//            label = { Text("Gmail") }
//
//        )
//
//        OutlinedTextField(
//            value = password.value,
//            onValueChange = { password.value = it },
//            label = { Text("PASSWORD") }
//
//        )
//
//
//
//        Button(onClick = {
//
//            viewModel.loginUser(email.value,password.value)
//        }) {
//            Text(text = "Login")
//
//        }


    }

}