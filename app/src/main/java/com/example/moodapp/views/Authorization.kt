package com.example.moodapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.R
import com.example.moodapp.viewModel.AuthorizationViewModel
import kotlinx.coroutines.launch


@Composable
fun Authorization(navController: NavController, viewModel: AuthorizationViewModel = viewModel()) {
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // перехід на екран вибору настрою при успішному вході
    LaunchedEffect(isLoggedIn) {
        if (isLoggedIn) {
            Log.d("Authorization", "Успішний вхід, переходимо на choose_mood")
            navController.navigate("choose_mood")
        }
    }

    Log.d("Authorization", "Екран входу відкритий")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Login Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(130.dp)
                .width(130.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Authorization",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = login,
            onValueChange = { viewModel.onLoginChanged(it) },
            label = { Text("Login") },
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(20.dp),

        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.loginUser()
                }
            },
            enabled = login.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text("Sign In", fontSize = 20.sp)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Text(
                text = "Create Account",
                fontSize = 14.sp,
            )

            TextButton(onClick = { navController.navigate("signup_screen") }) {
                Text("Sign up", fontSize = 15.sp)
            }
        }
    }
}
