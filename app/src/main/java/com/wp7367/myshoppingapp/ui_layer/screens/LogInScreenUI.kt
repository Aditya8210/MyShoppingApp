package com.wp7367.myshoppingapp.ui_layer.screens

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
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes


@Composable
fun  LoginScreen(viewModel: MyViewModel = hiltViewModel(),navController: NavController){


//  ~ Here State is Collect ~

    val lgState = viewModel.loginUser.collectAsState()



    val context = LocalContext.current


//     ~ Here State is Manage ~
    when{
        lgState.value.isLoading ->{
            Box(modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center){
                CircularProgressIndicator()

            }

        }
        lgState.value.error != null ->{
            Toast.makeText(context,lgState.value.error,Toast.LENGTH_SHORT).show()

        }
        lgState.value.data != null ->{
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
                    .size(235.dp)
                    .offset(x = 180.dp, y = (-60).dp)
            ) {

                Image(
                    painter = painterResource(id = com.wp7367.myshoppingapp.R.drawable.circle332212),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize())

            }

        }


        Column(
            modifier = Modifier.fillMaxWidth(),

            ) {

            Text(
                text = "LOGIN", modifier = Modifier.padding(start = 41.dp),
                fontSize = 33.sp,
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
            )

            Spacer(modifier = Modifier.height(4.dp))


            OutlinedTextField(value  =password.value ,
                onValueChange = {password.value = it},
                label = {Text(text = "Password")},
                singleLine = true,

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
            Button(onClick = {viewModel.loginUser(email.value,password.value)},)
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
                Box(modifier = Modifier.size(215.dp)
                    .offset(x = 0.dp, y = (+29).dp)
                ){


                    Image(painter = painterResource(id = com.wp7367.myshoppingapp.R.drawable.circle33221),
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