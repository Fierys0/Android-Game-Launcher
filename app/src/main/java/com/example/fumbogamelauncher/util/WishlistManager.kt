package com.example.fumbogamelauncher.util

import android.content.Context
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory

object WishlistManager {
    private const val PREFS_NAME = "wishlist_prefs"
    private const val KEY_WISHLIST = "wishlist_ids"

    fun addToWishlist(context: Context, gameId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentList = getWishlistIds(context).toMutableSet()
        currentList.add(gameId.toString())
        prefs.edit().putStringSet(KEY_WISHLIST, currentList).apply()
    }

    fun removeFromWishlist(context: Context, gameId: Int) {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        val currentList = getWishlistIds(context).toMutableSet()
        currentList.remove(gameId.toString())
        prefs.edit().putStringSet(KEY_WISHLIST, currentList).apply()
    }

    fun isInWishlist(context: Context, gameId: Int): Boolean {
        return getWishlistIds(context).contains(gameId.toString())
    }

    private fun getWishlistIds(context: Context): Set<String> {
        val prefs = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
        return prefs.getStringSet(KEY_WISHLIST, emptySet()) ?: emptySet()
    }

    /**
     * Helper to get full Game objects from stored IDs.
     * In a real app, this would come from a Repository/DB.
     */
    fun getWishlistGames(context: Context): List<Game> {
        val ids = getWishlistIds(context).map { it.toInt() }
        
        // This is a simplified master list. Ideally, this would be centralized.
        val masterGames = listOf(
            Game(1, "Cyberpunk 2077", "Experience the dystopian future of Night City.", GameCategory.RPG, "cyberpunk"),
            Game(2, "Elden Ring", "Fantasy RPG adventure.", GameCategory.ACTION, "elden_ring"),
            Game(3, "Valorant", "5v5 character-based tactical shooter.", GameCategory.ACTION, "valorant"),
            Game(4, "The Witcher 3", "Story-driven open world RPG.", GameCategory.RPG, "witcher3"),
            Game(5, "Hades", "God-like rogue-like dungeon crawler.", GameCategory.ACTION, "hades"),
            Game(6, "Dota 2", "Competitive game of action and strategy.", GameCategory.STRATEGY, "dota2"),
            Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom")
        )

        return masterGames.filter { ids.contains(it.id) }
    }
}
