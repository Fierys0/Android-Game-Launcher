package com.example.fumbogamelauncher.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory
import com.example.fumbogamelauncher.util.DownloadHelper

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GameDetailScreen(gameId: Int?, onBack: () -> Unit) {
    val context = LocalContext.current
    val game = remember(gameId) {
        listOf(
            Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom"),
            Game(1, context.getString(R.string.game_1_title), context.getString(R.string.game_1_desc), GameCategory.RPG, "cyberpunk"),
            Game(2, context.getString(R.string.game_2_title), context.getString(R.string.game_2_desc), GameCategory.ACTION, "elden_ring"),
            Game(3, context.getString(R.string.game_3_title), context.getString(R.string.game_3_desc), GameCategory.ACTION, "valorant")
        ).find { it.id == gameId }
    }

    if (game == null) {
        Box(modifier = Modifier.fillMaxSize().background(MaterialTheme.colorScheme.background), contentAlignment = Alignment.Center) {
            Text(text = "Game not found", color = MaterialTheme.colorScheme.onBackground)
        }
        return
    }

    val resId = if (game.imageResName != null) {
        context.resources.getIdentifier(game.imageResName, "drawable", context.packageName)
    } else 0

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                actions = {
                    IconButton(onClick = { /* Search */ }) {
                        Icon(Icons.Default.MoreVert, contentDescription = "More", tint = MaterialTheme.colorScheme.onBackground)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background)
            )
        },
        containerColor = MaterialTheme.colorScheme.background
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
        ) {
            // Header with Icon and Title
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .clip(RoundedCornerShape(8.dp)),
                    color = MaterialTheme.colorScheme.surface
                ) {
                    if (resId != 0) {
                        Image(
                            painter = painterResource(resId),
                            contentDescription = null,
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(40.dp))
                        }
                    }
                }
                
                Spacer(modifier = Modifier.width(16.dp))
                
                Column {
                    Text(
                        text = game.title,
                        color = MaterialTheme.colorScheme.onBackground,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "FUMBO STUDIOS",
                        color = MaterialTheme.colorScheme.primary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                }
            }

            // Game Stats Row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                DetailStat(label = "4.8 ★", subLabel = "Reviews")
                VerticalDivider(modifier = Modifier.height(30.dp).align(Alignment.CenterVertically))
                DetailStat(label = "500MB", subLabel = "Size")
                VerticalDivider(modifier = Modifier.height(30.dp).align(Alignment.CenterVertically))
                DetailStat(label = "12+", subLabel = "Rating")
            }

            // Action Buttons
            val isInstalled = game.packageName?.let { DownloadHelper.isAppInstalled(context, it) } ?: false
            
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Button(
                    onClick = {
                        if (isInstalled) {
                            DownloadHelper.launchApp(context, game.packageName!!)
                        } else if (game.downloadUrl != null) {
                            DownloadHelper.downloadAndInstallApk(context, game.downloadUrl, "${game.title.replace(" ", "_")}.apk")
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                    shape = RoundedCornerShape(4.dp)
                ) {
                    Text(
                        text = if (isInstalled) "LAUNCH" else "INSTALL",
                        color = Color.White,
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        letterSpacing = 1.sp
                    )
                }
            }

            // About this game
            Text(
                text = "ABOUT THIS GAME",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 16.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(16.dp),
                letterSpacing = 1.sp
            )
            
            Text(
                text = game.description,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.7f),
                fontSize = 14.sp,
                lineHeight = 22.sp,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            // Genre Tags
            Row(modifier = Modifier.padding(horizontal = 16.dp)) {
                AssistChip(
                    onClick = { },
                    label = { Text(game.category.name, color = MaterialTheme.colorScheme.onSurface) },
                    colors = AssistChipDefaults.assistChipColors(containerColor = MaterialTheme.colorScheme.surface, labelColor = MaterialTheme.colorScheme.onSurface),
                    border = null,
                    shape = RoundedCornerShape(2.dp)
                )
            }

            Spacer(modifier = Modifier.height(48.dp))
        }
    }
}

@Composable
fun DetailStat(label: String, subLabel: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = label, color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Black, fontSize = 16.sp)
        Text(text = subLabel, color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontSize = 12.sp)
    }
}
