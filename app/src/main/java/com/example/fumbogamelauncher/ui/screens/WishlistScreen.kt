package com.example.fumbogamelauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory
import com.example.fumbogamelauncher.ui.theme.*

@Composable
fun WishlistScreen(onGameClick: (Int) -> Unit) {
    val context = LocalContext.current
    // In a real app, this would come from a ViewModel
    val wishlistGames = remember {
        listOf(
            Game(1, context.getString(R.string.game_1_title), "", GameCategory.RPG, "cyberpunk"),
            Game(2, context.getString(R.string.game_2_title), "", GameCategory.ACTION, "elden_ring"),
            Game(4, "The Witcher 3", "", GameCategory.RPG, null)
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 24.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "Your Wishlist",
            color = TextWhite,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold
        )
        Text(
            text = "Games you're tracking",
            color = PlayStoreGreen,
            style = MaterialTheme.typography.bodyMedium
        )
        
        Spacer(modifier = Modifier.height(24.dp))
        
        if (wishlistGames.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = "Your wishlist is empty", color = Color.Gray)
            }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                items(wishlistGames) { game ->
                    WishlistItem(game, onGameClick)
                }
            }
        }
    }
}

@Composable
fun WishlistItem(game: Game, onGameClick: (Int) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceColor)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(50.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(PlayStoreGreen.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(Icons.Default.Favorite, contentDescription = null, tint = PlayStoreGreen)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = game.title, color = TextWhite, fontWeight = FontWeight.SemiBold, fontSize = 16.sp)
                Text(text = game.category.name, color = TextWhite.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
        
        Button(
            onClick = { onGameClick(game.id) },
            colors = ButtonDefaults.buttonColors(containerColor = PlayStoreGreen),
            shape = RoundedCornerShape(8.dp),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp)
        ) {
            Text("View", color = Color.White, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}
