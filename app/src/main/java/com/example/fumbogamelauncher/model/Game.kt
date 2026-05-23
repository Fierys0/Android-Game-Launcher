package com.example.fumbogamelauncher.model

import java.io.Serializable

enum class GameCategory {
    ACTION, ADVENTURE, RPG, STRATEGY, SPORTS
}

data class Game(
    val id: Int,
    val title: String,
    val description: String,
    val category: GameCategory,
    val imageResName: String? = null,
    val downloadUrl: String? = null,
    val packageName: String? = null
) : Serializable