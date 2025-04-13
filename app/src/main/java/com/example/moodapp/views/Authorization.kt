package com.example.moodapp.views

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
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
fun Authorization(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: AuthorizationViewModel = viewModel()
) {
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    val coroutineScope = rememberCoroutineScope()

    // Визначення розмірів на основі windowSizeClass
    val imageSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 130.dp
        WindowWidthSizeClass.Medium -> 180.dp
        WindowWidthSizeClass.Expanded -> 220.dp
        else -> 130.dp
    }

    val textFieldWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 260.dp
        WindowWidthSizeClass.Medium -> 360.dp
        WindowWidthSizeClass.Expanded -> 420.dp
        else -> 260.dp
    }

    val buttonWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 150.dp
        WindowWidthSizeClass.Medium -> 200.dp
        WindowWidthSizeClass.Expanded -> 250.dp
        else -> 150.dp
    }

    val buttonHeight = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 50.dp
        WindowWidthSizeClass.Medium -> 60.dp
        WindowWidthSizeClass.Expanded -> 70.dp
        else -> 50.dp
    }

    val fontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 20.sp
        WindowWidthSizeClass.Medium -> 22.sp
        WindowWidthSizeClass.Expanded -> 24.sp
        else -> 20.sp
    }

    val titleFontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MaterialTheme.typography.headlineMedium
        WindowWidthSizeClass.Medium -> MaterialTheme.typography.headlineLarge
        WindowWidthSizeClass.Expanded -> MaterialTheme.typography.displaySmall
        else -> MaterialTheme.typography.headlineMedium
    }

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
            .padding(
                horizontal = when (windowSizeClass.widthSizeClass) {
                    WindowWidthSizeClass.Compact -> 16.dp
                    WindowWidthSizeClass.Medium -> 32.dp
                    WindowWidthSizeClass.Expanded -> 64.dp
                    else -> 16.dp
                },
                vertical = 16.dp
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Image(
            painter = painterResource(id = R.drawable.logo),
            contentDescription = "Login Image",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .height(imageSize)
                .width(imageSize)
                .padding(bottom = 16.dp)
        )

        Text(
            text = "Authorization",
            style = titleFontSize,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = login,
            onValueChange = { viewModel.onLoginChanged(it) },
            label = { Text("Login") },
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 10.dp),
            shape = RoundedCornerShape(20.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = fontSize * 0.8f)
        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text("Password") },
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            textStyle = LocalTextStyle.current.copy(fontSize = fontSize * 0.8f)
        )

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.loginUser()
                }
            },
            enabled = login.isNotEmpty() && password.isNotEmpty(),
            modifier = Modifier
                .width(buttonWidth)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text("Sign In", fontSize = fontSize)
        }

        Row(
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 16.dp)
        ) {
            Text(
                text = "Create Account",
                fontSize = fontSize * 0.7f,
            )

            TextButton(onClick = { navController.navigate("signup_screen") }) {
                Text("Sign up", fontSize = fontSize * 0.75f)
            }
        }
    }
}