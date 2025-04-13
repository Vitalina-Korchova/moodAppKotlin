package com.example.moodapp.views

import android.content.res.Configuration
import androidx.compose.foundation.layout.Row
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalConfiguration

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.ClickableText
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.material3.windowsizeclass.*
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.DpSize

@OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
@Composable
fun MoodTips() {
    val configuration = LocalConfiguration.current
    val density = LocalDensity.current

    // Всі обчислення - ПОЗА remember
    val screenWidthDp = configuration.screenWidthDp.dp
    val screenHeightDp = configuration.screenHeightDp.dp

    val windowSizeClass = WindowSizeClass.calculateFromSize(
        with(density) { DpSize(screenWidthDp, screenHeightDp) }
    )

    val tips = listOf(
        "Try meditation",
        "Take a walk outside",
        "Talk to a friend",
        "Listen to your favorite music",
        "Write down your thoughts"
    )

    var selectedTip by remember { mutableStateOf<String?>(null) }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (selectedTip == null) {
                TipsList(tips = tips, onTipSelected = { selectedTip = it })
            } else {
                TipDetail(tip = selectedTip, onBack = { selectedTip = null })
            }
        }

        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
            Row(Modifier.fillMaxSize()) {
                TipsList(
                    tips = tips,
                    onTipSelected = { selectedTip = it },
                    modifier = Modifier.weight(1f)
                )
                TipDetail(
                    tip = selectedTip,
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}


@Composable
fun TipsList(
    tips: List<String>,
    onTipSelected: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxHeight()
            .padding(16.dp)
    ) {
        Text("Mood Tips", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))
        tips.forEach { tip ->
            Card(
                onClick = { onTipSelected(tip) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
            ) {
                Text(tip, modifier = Modifier.padding(16.dp))
            }
        }
    }
}

@Composable
fun TipDetail(
    tip: String?,
    modifier: Modifier = Modifier,
    onBack: (() -> Unit)? = null
) {
    if (tip == null) {
        Box(modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Select a tip from the list")
        }
    } else {
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            if (onBack != null) {
                IconButton(onClick = onBack) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            }
            Text("Details:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(tip, style = MaterialTheme.typography.bodyLarge)
        }
    }
}




