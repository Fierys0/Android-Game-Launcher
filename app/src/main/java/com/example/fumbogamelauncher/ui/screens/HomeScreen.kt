package com.example.fumbogamelauncher.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory
import com.example.fumbogamelauncher.ui.theme.PlayStoreGreen

@Composable
fun HomeScreen(onGameClick: (Int) -> Unit) {
    val context = LocalContext.current
    val games = remember {
        listOf(
            Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom"),
            Game(1, context.getString(R.string.game_1_title), context.getString(R.string.game_1_desc), GameCategory.RPG, "cyberpunk"),
            Game(2, context.getString(R.string.game_2_title), context.getString(R.string.game_2_desc), GameCategory.ACTION, "elden_ring"),
            Game(3, context.getString(R.string.game_3_title), context.getString(R.string.game_3_desc), GameCategory.ACTION, "valorant"),
            Game(4, "The Witcher 3", "Story-driven open world RPG.", GameCategory.RPG, null),
            Game(5, "Hades", "God-like rogue-like dungeon crawler.", GameCategory.ACTION, null)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
    ) {
        HeaderSection()
        HeroBanner(game = games.first(), onGameClick = onGameClick)
        
        GameRowSection(title = "Featured & Recommended", games = games.drop(1), onGameClick = onGameClick)
        
        GameRowSection(title = "Special Offers", games = games.shuffled(), onGameClick = onGameClick)
        
        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
fun HeaderSection() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                text = "STORE",
                color = MaterialTheme.colorScheme.onBackground,
                fontWeight = FontWeight.Black,
                fontSize = 20.sp,
                letterSpacing = 2.sp
            )
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "LIBRARY", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontSize = 14.sp)
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "COMMUNITY", color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f), fontSize = 14.sp)
        }
        
        Row {
            Icon(Icons.Default.Search, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
            Spacer(modifier = Modifier.width(16.dp))
            Icon(Icons.Default.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.onBackground)
        }
    }
}

@Composable
fun HeroBanner(game: Game, onGameClick: (Int) -> Unit) {
    val context = LocalContext.current
    val resId = if (game.imageResName != null) {
        context.resources.getIdentifier(game.imageResName, "drawable", context.packageName)
    } else 0

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(400.dp)
            .padding(16.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable { onGameClick(game.id) }
            .border(1.dp, Color.White.copy(alpha = 0.1f), RoundedCornerShape(8.dp))
    ) {
        if (resId != 0) {
            Image(
                painter = painterResource(resId),
                contentDescription = null,
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }
        
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, Color.Black.copy(alpha = 0.9f)),
                        startY = 300f
                    )
                )
        )
        
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)
        ) {
            Surface(
                color = PlayStoreGreen,
                shape = RoundedCornerShape(4.dp)
            ) {
                Text(
                    text = "FEATURED",
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = game.title,
                color = Color.White,
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold
            )
            Text(
                text = game.description,
                color = Color.White.copy(alpha = 0.7f),
                fontSize = 14.sp,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onGameClick(game.id) },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White),
                shape = RoundedCornerShape(4.dp)
            ) {
                Icon(Icons.Default.PlayArrow, contentDescription = null, tint = Color.Black)
                Spacer(modifier = Modifier.width(8.dp))
                Text("GET NOW", color = Color.Black, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun GameRowSection(title: String, games: List<Game>, onGameClick: (Int) -> Unit) {
    Column(modifier = Modifier.padding(vertical = 16.dp)) {
        Text(
            text = title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(modifier = Modifier.height(16.dp))
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(games) { game ->
                SteamGameItem(game, onGameClick)
            }
        }
    }
}

@Composable
fun SteamGameItem(game: Game, onGameClick: (Int) -> Unit) {
    val context = LocalContext.current
    val resId = if (game.imageResName != null) {
        context.resources.getIdentifier(game.imageResName, "drawable", context.packageName)
    } else 0

    Column(
        modifier = Modifier
            .width(220.dp)
            .clickable { onGameClick(game.id) }
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(124.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (resId != 0) {
                Image(
                    painter = painterResource(resId),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = game.title,
            color = MaterialTheme.colorScheme.onBackground,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Free",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f),
                fontSize = 12.sp
            )
            Surface(
                color = Color.White.copy(alpha = 0.1f),
                shape = RoundedCornerShape(2.dp)
            ) {
                Text(
                    text = "PC",
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 10.sp,
                    modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                )
            }
        }
    }
}
