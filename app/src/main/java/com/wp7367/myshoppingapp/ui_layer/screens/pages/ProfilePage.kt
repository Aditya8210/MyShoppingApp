package com.wp7367.myshoppingapp.ui_layer.screens.pages

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.Green
import androidx.compose.ui.graphics.Color.Companion.Red
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.wp7367.myshoppingapp.domain_layer.models.userData
import com.wp7367.myshoppingapp.ui_layer.screens.MyViewModel
import com.wp7367.myshoppingapp.ui_layer.screens.navigation.Routes


@Composable
fun ProfilePage(viewModels: MyViewModel = hiltViewModel(), navController: NavController, firebaseAuth: FirebaseAuth) {

    val coroutineScope = rememberCoroutineScope()

    LaunchedEffect(key1 = Unit) {
        viewModels.getUserData(firebaseAuth.currentUser?.uid.toString())
    }

    val profileSt = viewModels.getUserData.collectAsStateWithLifecycle()




    val context = LocalContext.current




    val firstname = remember { mutableStateOf("") }
    val lastname  = remember { mutableStateOf("") }
    val email     = remember { mutableStateOf("") }
    val phone     = remember { mutableStateOf("") }
    val address   = remember { mutableStateOf("") }

    //  To Know The User Click Edit or Not
    val isEditing = remember { mutableStateOf(false) }

//   To Show UserData on Screen while Profile Section Open
    LaunchedEffect(profileSt.value.data) {
        profileSt.value.data.let {
            firstname.value = it?.firstName.toString()
            lastname.value = it?.lastName.toString()
            email.value = it?.email.toString()
            phone.value = it?.phoneNumber.toString()
            address.value = it?.address.toString()
        }
    }




    if(profileSt.value.isLoading){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }
    else if(profileSt.value.error != null){
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            Text(text = profileSt.value.error!!)
        }
    }
    else if(profileSt.value.data != null){

        Column (modifier = Modifier.fillMaxSize().padding(10.dp)
            .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.Center,

        )
        {

            Row (modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp))
            {


                OutlinedTextField(value = firstname.value,
                    modifier = Modifier.weight(1f),
                    readOnly = if (isEditing.value) false else true,

                    onValueChange = { firstname.value = it },
                    label = { Text(text = "FirstName") },


                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

                OutlinedTextField(value = lastname.value,
                    modifier = Modifier.weight(1f),
                    readOnly = if (isEditing.value) false else true,
                    onValueChange = {lastname.value = it},
                    label = { Text(text = "LastName") },

                    singleLine = true,
                    keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Number)
                )

            }
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(value = email.value,
                onValueChange = {email.value = it},
                modifier = Modifier.fillMaxWidth(),
                readOnly = if (isEditing.value) false else true,

                label = { Text(text = "Email") },

            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(value =phone.value,
                modifier = Modifier.fillMaxWidth(),
                readOnly = if (isEditing.value) false else true,
                onValueChange = {phone.value = it},
                label = { Text(text = "Phone") },

            )
            Spacer(modifier = Modifier.height(5.dp))

            OutlinedTextField(value = address.value,
                modifier = Modifier.fillMaxWidth(),
                readOnly = if (isEditing.value) false else true,
                onValueChange = {address.value = it},
                label = { Text(text = "Address") },

            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp))
            {
                if (isEditing.value == false) {
                    OutlinedButton(
                        onClick = { isEditing.value = !isEditing.value },
                        modifier = Modifier.size(width = 200.dp, height = 50.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) {
                        Text(text = "Edit Profile", color = Color.Blue)
                    }
                }else {

                    OutlinedButton(
                        onClick = {
                            val updatedata = userData(
                                firstName = firstname.value,
                                lastName = lastname.value,
                                email = email.value,
                                phoneNumber = phone.value,
                                address = address.value
                            )

                            viewModels.updateUser(updatedata)

                             isEditing.value = !isEditing.value



                            Toast.makeText(context, "Profile Updated", Toast.LENGTH_SHORT).show()
                        },

                        modifier = Modifier.size(width = 200.dp, height = 50.dp),

                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Green)
                    ) {
                        Text(text = "Save Profile", color = Color.Blue)
                    }
                }

                OutlinedButton(
                    onClick = {
                        firebaseAuth.signOut()
                        navController.navigate(Routes.LogInScreen)
                    },
                    modifier = Modifier.size(width = 200.dp, height = 50.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Red)
                ) {
                    Text(text = "LogOut", color = Color.Black)
                }

            }


        }

    }
    else { // This new block handles the case where data is null, not loading, and no error
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center){
            CircularProgressIndicator()
        }
    }




// ----------------- ~ Old UI ~ --------------------------------------------------------------------


//    Column (modifier = Modifier.fillMaxSize(),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ){
//
//        OutlinedButton(onClick = {
//
//            firebaseAuth.signOut()
//            navController.navigate(Routes.LogInScreen)
//        },
//
//            modifier = Modifier.size(width = 200.dp, height = 50.dp),
//            shape = RoundedCornerShape(10.dp),
//            colors = ButtonDefaults.buttonColors(),
//
//
//
//            ) {
//            Text(text = "LogOut")
//        }
//    }


}
