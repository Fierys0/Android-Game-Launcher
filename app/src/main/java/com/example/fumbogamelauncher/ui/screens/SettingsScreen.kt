package com.example.fumbogamelauncher.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fumbogamelauncher.ui.theme.*
import com.example.fumbogamelauncher.util.ThemeHelper

@Composable
fun SettingsScreen(onThemeToggle: () -> Unit) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text = "General",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            item {
                SettingsToggleItem(
                    SettingItem("Dark Mode", "Toggle light or dark theme", Icons.Default.DarkMode, ThemeHelper.isDarkMode(context)),
                    onToggle = { enabled ->
                        ThemeHelper.setDarkMode(context, enabled)
                        onThemeToggle()
                    }
                )
            }

            items(generalSettings.filter { it.title != "Dark Mode" }) { setting ->
                SettingsToggleItem(setting, onToggle = {})
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Notifications",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(notificationSettings) { setting ->
                SettingsToggleItem(setting, onToggle = {})
            }
            
            item {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Support",
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
            
            items(otherSettings) { setting ->
                SettingsClickItem(setting)
            }
        }
    }
}

@Composable
fun SettingsToggleItem(setting: SettingItem, onToggle: (Boolean) -> Unit) {
    var checked by remember { mutableStateOf(setting.initialValue as Boolean) }
    
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = setting.icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = setting.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
                if (setting.subtitle != null) {
                    Text(text = setting.subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 12.sp)
                }
            }
        }
        Switch(
            checked = checked,
            onCheckedChange = { 
                checked = it
                onToggle(it)
            },
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = MaterialTheme.colorScheme.primary,
                uncheckedThumbColor = Color.Gray,
                uncheckedTrackColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.8f)
            )
        )
    }
}

@Composable
fun SettingsClickItem(setting: SettingItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable { /* Handle click */ }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .clip(RoundedCornerShape(10.dp))
                .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = setting.icon,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = setting.title, color = MaterialTheme.colorScheme.onSurface, fontSize = 16.sp)
            if (setting.subtitle != null) {
                Text(text = setting.subtitle, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f), fontSize = 12.sp)
            }
        }
    }
}

data class SettingItem(
    val title: String,
    val subtitle: String? = null,
    val icon: ImageVector,
    val initialValue: Any? = null
)

val generalSettings = listOf(
    SettingItem("Auto-update games", "Update games automatically over Wi-Fi", Icons.Default.Update, true),
    SettingItem("Dark Mode", "Toggle light or dark theme", Icons.Default.DarkMode, true),
    SettingItem("Hardware Acceleration", "Improve performance in games", Icons.Default.Speed, false)
)

val notificationSettings = listOf(
    SettingItem("Game Alerts", "Get notified about new games", Icons.Default.Notifications, true),
    SettingItem("Friend Activity", "See what your friends are playing", Icons.Default.People, false),
    SettingItem("Promotions", "Special offers and discounts", Icons.Default.LocalOffer, true)
)

val otherSettings = listOf(
    SettingItem("Clear Cache", "Free up space by removing temporary files", Icons.Default.Delete),
    SettingItem("Language", "English (United States)", Icons.Default.Language),
    SettingItem("Help & Support", "Visit our help center", Icons.AutoMirrored.Filled.Help)
)
