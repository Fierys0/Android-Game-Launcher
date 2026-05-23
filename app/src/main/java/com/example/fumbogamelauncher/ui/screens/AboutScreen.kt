package com.example.fumbogamelauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fumbogamelauncher.ui.theme.*

@Composable
fun AboutScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        // App Logo Card Style
        Box(
            modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(25.dp))
                .background(PlayStoreGreen),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "F",
                color = Color.White,
                fontSize = 72.sp,
                fontWeight = FontWeight.Bold
            )
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            text = "Fumbo Launcher",
            color = TextWhite,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = "Version 1.0.0",
            color = TextWhite.copy(alpha = 0.5f),
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(32.dp))
        
        // Description Card
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(20.dp))
                .background(SurfaceColor)
                .padding(20.dp)
        ) {
            Text(
                text = "Fumbo Game Launcher is your ultimate destination for discovering and playing the best mobile games. Organise your library, track your achievements, and connect with other gamers.",
                color = TextWhite.copy(alpha = 0.8f),
                textAlign = TextAlign.Center,
                lineHeight = 24.sp
            )
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            AboutInfoRow(label = "Developer", value = "Fumbo Studios", color = PlayStoreGreen)
            AboutInfoRow(label = "Website", value = "fumbo.example.com", color = PlayStoreGreen)
            AboutInfoRow(label = "Privacy", value = "View Policy", color = PlayStoreGreen)
        }
        
        Spacer(modifier = Modifier.height(48.dp))
        
        Text(
            text = "© 2023 Fumbo Studios",
            color = TextWhite.copy(alpha = 0.3f),
            fontSize = 12.sp
        )
    }
}

@Composable
fun AboutInfoRow(label: String, value: String, color: Color) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(SurfaceColor)
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, color = TextWhite.copy(alpha = 0.6f))
        Text(text = value, color = color, fontWeight = FontWeight.SemiBold)
    }
}
