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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.R
import com.example.moodapp.ui.theme.customPurple
import com.example.moodapp.viewModel.RegistrationViewModel
import kotlinx.coroutines.launch

@Composable
fun Registration(navController: NavController, viewModel: RegistrationViewModel = viewModel()) {
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val dateOfBirth by viewModel.dateOfBirth.collectAsState()
    val isPrivacyAccepted by viewModel.isPrivacyAccepted.collectAsState()
    val isRegistered by viewModel.isRegistered.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // перехід на екран входу при успішній реєстрації
    LaunchedEffect(isRegistered) {
        if (isRegistered) {
            Log.d("Registration", "Переходимо на екран входу")
            navController.navigate("signin_screen")
        }
    }

    Log.d("Registration", "Екран реєстрації відкритий")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(androidx.compose.ui.graphics.Color.White)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Login Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(100.dp)
                .width(100.dp)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Registration",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = login,
            onValueChange = { viewModel.onLoginChanged(it) },
            label = { Text("Login", fontSize = 16.sp) },
            textStyle = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 6.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password", fontSize = 16.sp) },
            textStyle = TextStyle(fontSize = 14.sp),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 6.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { viewModel.onDateOfBirthChanged(it) },
            label = { Text("Date of Birth", fontSize = 16.sp) },
            textStyle = TextStyle(fontSize = 14.sp),
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .width(260.dp)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isPrivacyAccepted,
                    onCheckedChange = { viewModel.onPrivacyAcceptedChanged(it) }
                )
                Text(
                    text = "I accept the Privacy Policy",
                    fontSize = 14.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }
        }

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.registerUser()
                }
            },
            enabled = login.isNotEmpty() && password.isNotEmpty() && dateOfBirth.isNotEmpty() && isPrivacyAccepted,
            modifier = Modifier
                .width(150.dp)
                .height(50.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = customPurple,
                contentColor = androidx.compose.ui.graphics.Color.White
            ),
        ) {
            Text("Sign Up", fontSize = 20.sp)
        }
    }
}
