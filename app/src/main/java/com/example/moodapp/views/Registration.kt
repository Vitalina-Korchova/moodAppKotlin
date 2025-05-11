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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.moodapp.R
import com.example.moodapp.viewModel.RegistrationViewModel
import kotlinx.coroutines.launch

@Composable
fun Registration(
    navController: NavController,
    windowSizeClass: WindowSizeClass,
    viewModel: RegistrationViewModel = viewModel()
) {
    val login by viewModel.login.collectAsState()
    val password by viewModel.password.collectAsState()
    val dateOfBirth by viewModel.dateOfBirth.collectAsState()
    val isPrivacyAccepted by viewModel.isPrivacyAccepted.collectAsState()
    val isRegistered by viewModel.isRegistered.collectAsState()

    // Визначення розмірів на основі windowSizeClass
    val imageSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 100.dp
        WindowWidthSizeClass.Medium -> 150.dp
        WindowWidthSizeClass.Expanded -> 180.dp
        else -> 100.dp
    }

    val textFieldWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 260.dp
        WindowWidthSizeClass.Medium -> 360.dp
        WindowWidthSizeClass.Expanded -> 420.dp
        else -> 260.dp
    }

    val buttonWidth = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 240.dp
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

    val buttonFontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 20.sp
        WindowWidthSizeClass.Medium -> 22.sp
        WindowWidthSizeClass.Expanded -> 24.sp
        else -> 20.sp
    }

    val labelFontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 16.sp
        WindowWidthSizeClass.Medium -> 18.sp
        WindowWidthSizeClass.Expanded -> 20.sp
        else -> 16.sp
    }

    val textFontSize = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> 14.sp
        WindowWidthSizeClass.Medium -> 16.sp
        WindowWidthSizeClass.Expanded -> 18.sp
        else -> 14.sp
    }

    val titleStyle = when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> MaterialTheme.typography.headlineMedium
        WindowWidthSizeClass.Medium -> MaterialTheme.typography.headlineLarge
        WindowWidthSizeClass.Expanded -> MaterialTheme.typography.displaySmall
        else -> MaterialTheme.typography.headlineMedium
    }

    //для виклику асинхронної функції viewModel.registerUser(), коли користувач натисне кнопку Sign Up
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
            text = stringResource(R.string.reg_registration),
            style = titleStyle,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = login,
            onValueChange = { viewModel.onLoginChanged(it) },
            label = { Text(stringResource(R.string.reg_login), fontSize = labelFontSize) },
            textStyle = TextStyle(fontSize = textFontSize),
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 6.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChanged(it) },
            label = { Text(stringResource(R.string.reg_password), fontSize = labelFontSize) },
            textStyle = TextStyle(fontSize = textFontSize),
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 6.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        OutlinedTextField(
            value = dateOfBirth,
            onValueChange = { viewModel.onDateOfBirthChanged(it) },
            label = { Text(stringResource(R.string.reg_date_birth), fontSize = labelFontSize) },
            textStyle = TextStyle(fontSize = textFontSize),
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 16.dp),
            shape = RoundedCornerShape(20.dp),
            singleLine = true
        )

        Box(
            modifier = Modifier
                .width(textFieldWidth)
                .padding(bottom = 16.dp),
            contentAlignment = Alignment.Center
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Checkbox(
                    checked = isPrivacyAccepted,
                    onCheckedChange = { viewModel.onPrivacyAcceptedChanged(it) },
                    modifier = Modifier.size(
                        when (windowSizeClass.widthSizeClass) {
                            WindowWidthSizeClass.Compact -> 24.dp
                            WindowWidthSizeClass.Medium -> 30.dp
                            WindowWidthSizeClass.Expanded -> 36.dp
                            else -> 24.dp
                        }
                    )
                )
                Text(
                    text = stringResource(R.string.reg_privacy_policy),
                    fontSize = textFontSize,
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
                .width(buttonWidth)
                .height(buttonHeight),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary
            ),
        ) {
            Text(stringResource(R.string.reg_sign_up), fontSize = buttonFontSize)
        }
    }
}