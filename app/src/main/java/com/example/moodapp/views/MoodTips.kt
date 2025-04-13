package com.example.moodapp.views

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.windowsizeclass.*

@Composable
fun MoodTips(windowSizeClass: WindowSizeClass) {

    val moodTipsMap = mapOf(
        "Try meditation" to "Meditation is a simple yet powerful practice " +
                "to calm the mind. Start by finding a comfortable seated " +
                "position—either on a chair or on the floor with your " +
                "legs crossed. Close your eyes gently or lower your gaze " +
                "to minimize distractions. Bring your attention to your " +
                "breath and notice how the air feels as it enters your " +
                "nose and fills your lungs. Let your breath flow naturally " +
                "without trying to change it. Feel the rise and fall of your chest or the sensation of air passing through your nostrils. If your mind starts to wander, gently bring your focus back to your breath without judgment. You can count your breaths from one to ten and start over if needed. This helps anchor your awareness and keeps your attention steady. Even just a few minutes of this each day can make a noticeable difference. Over time, meditation helps reduce anxiety and creates more emotional space. It teaches you to observe your thoughts without getting caught up in them. You don’t need to be an expert to benefit—consistency matters more than perfection. Try using a timer or a guided meditation app if that helps you stay on track. The key is to make it a regular habit, even if it’s just five minutes in the morning or before bed. With patience and practice, meditation becomes a supportive tool for emotional and mental well-being.",
        "Take a walk outside" to "Taking a walk outside is one of the simplest ways to improve your mood and mental clarity. Fresh air has a remarkable ability to clear your mind and provide a refreshing change of scenery. The physical act of walking releases endorphins, the body’s natural mood elevators, which help reduce stress and anxiety. A short walk, even just 10–15 minutes, can help break up your day and give you a new perspective. Walking outdoors exposes you to natural sunlight, which is vital for regulating your circadian rhythm and boosting your mood through the production of serotonin. If possible, try walking in nature, such as a park, garden, or forest. The sights and sounds of nature can have a soothing effect on your mind and help you feel more connected to the world around you. As you walk, focus on the rhythm of your steps and the sensations in your body. Allow your mind to wander and let go of any worries or distractions. If you’re feeling particularly stressed, you might want to leave your phone behind so you can fully immerse yourself in the experience. Use this time to clear your thoughts and focus on being present in the moment. Walking also provides a great opportunity to reflect on any challenges you might be facing or gain insight into a problem. The movement itself can help stimulate creative thoughts and boost problem-solving abilities. Additionally, walking outside is an easy way to practice mindfulness, which can reduce mental clutter and increase overall well-being. Next time you're feeling overwhelmed, step outside, take a deep breath, and enjoy the simple act of walking.",
        "Talk to a friend" to "Talking to a friend can be one of the most powerful ways to release built-up emotions and ease feelings of isolation. Sometimes, just having someone listen to you without judgment can bring incredible relief. Friends are often able to offer a fresh perspective or even advice, which might help you navigate difficult situations. Opening up about how you’re feeling allows you to process emotions that might otherwise remain bottled up. It’s important to remember that you don’t have to carry your burdens alone—many people are willing to listen and support you. A good friend will offer empathy and understanding, helping you feel heard and validated. When you talk to a friend, try to be honest about what’s bothering you and what kind of support you need. You don’t always have to have solutions right away; sometimes, just expressing yourself can bring clarity. If you're feeling vulnerable, reach out to someone you trust deeply, as their presence will make the conversation feel safe. Social connections are essential for mental health, and maintaining close relationships with friends fosters a sense of belonging and emotional well-being. Talking can also be cathartic, providing an emotional release that allows you to move forward more easily. Additionally, sharing your thoughts might strengthen the bond between you and your friend, deepening your connection. Even if the conversation is brief, knowing that someone cares can significantly boost your mood. If you’re hesitant to reach out, remember that true friendships are built on mutual support, and asking for help is a sign of strength, not weakness. Next time you feel overwhelmed, consider calling a friend and taking the opportunity to talk things through.\n" +
                "\n",
        "Listen to your favorite music" to "Listening to music is a quick and effective way to change your emotional state. The power of music to influence your mood is well-documented, and different genres can have distinct effects on how you feel. If you’re feeling down or stressed, playing some upbeat, energetic songs can lift your spirits and help energize you. Conversely, if you're feeling anxious or need to unwind, slower, calming tunes can help soothe your mind and reduce tension. Music taps into your brain's emotional centers, evoking feelings of joy, nostalgia, or comfort. It can be a form of emotional expression, allowing you to connect with feelings that might be difficult to verbalize. Sometimes, a favorite song can take you back to a happy memory, which can improve your mood and remind you of positive experiences. Creating a playlist of songs that boost your mood can be a great tool to turn to when you need a pick-me-up. If you’re feeling stressed, try listening to nature sounds or instrumental music, as these can have a calming and meditative effect. Singing along or dancing to the beat can also help release any built-up tension and bring joy. Music has a therapeutic quality that is often used in art therapy to aid emotional healing. It helps you to tune into your emotions and release any pent-up feelings in a healthy way. Next time you're feeling overwhelmed, consider playing your favorite music and letting the rhythm change your state of mind. Whether you're alone or with others, music can be a universal connector that brightens your day.",
        "Write down your thoughts" to "Journaling is an incredibly effective tool for processing your thoughts and emotions. Writing things down allows you to externalize your inner world and make sense of complex feelings. When you put your thoughts on paper, it often feels like a mental release, giving you a sense of relief and clarity. It doesn’t matter if your writing is neat or organized; what’s important is that you express yourself honestly. You might start by jotting down how you’re feeling in the moment or reflecting on what’s been happening in your life. Sometimes, simply asking yourself questions like, “What’s bothering me today?” or “What am I grateful for?” can help unlock deeper insights. Journaling also gives you the chance to track your emotions over time, allowing you to notice patterns and triggers that affect your mental state. This process of self-reflection can increase your emotional intelligence and awareness. You don’t need to write a lot—5–10 minutes can be enough to make a difference. In addition, the act of writing can help you organize your thoughts and prioritize what's important. If you're stuck, try writing about your day, your goals, or even a recent challenge you’ve faced. Writing can help you work through problems or gain new perspectives. You might be surprised by the clarity that emerges once you’ve written things down. If you feel overwhelmed by your thoughts, journaling provides a safe space to unload without fear of judgment. As a bonus, revisiting your journals later can reveal how much progress you’ve made. Regular journaling can also be a form of self-care that brings mental calm and emotional grounding."
    )

    val tips = moodTipsMap.keys.toList()
    var selectedTipKey by remember { mutableStateOf<String?>(null) }

    when (windowSizeClass.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            if (selectedTipKey == null) {
                MoodTipsList(
                    tips = tips,
                    onTipSelected = { selectedTipKey = it }
                )
            } else {
                MoodTipDetail(
                    tip = moodTipsMap[selectedTipKey],
                    onBack = { selectedTipKey = null }
                )
            }
        }

        WindowWidthSizeClass.Medium, WindowWidthSizeClass.Expanded -> {
            Row(Modifier.fillMaxSize()) {
                MoodTipsList(
                    tips = tips,
                    onTipSelected = { selectedTipKey = it },
                    modifier = Modifier.weight(1f)
                )
                MoodTipDetail(
                    tip = moodTipsMap[selectedTipKey],
                    modifier = Modifier.weight(2f)
                )
            }
        }
    }
}

@Composable
fun MoodTipsList(
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
fun MoodTipDetail(
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
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp)
                    .background(
                        color = MaterialTheme.colorScheme.onPrimary,
                        shape = RoundedCornerShape(16.dp)
                    )
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                Column {
                    Text(
                        text = "Details:",
                        style = MaterialTheme.typography.titleLarge.copy(
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                    Spacer(Modifier.height(12.dp))
                    Text(
                        text = tip,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                            color = MaterialTheme.colorScheme.primary
                        )
                    )
                }
            }
        }
    }
}
