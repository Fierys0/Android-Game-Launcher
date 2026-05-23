package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.databinding.ActivitySearchBinding
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val allGames = listOf(
        Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom"),
        Game(1, "Cyberpunk 2077", "Experience the dystopian future of Night City.", GameCategory.RPG, "cyberpunk"),
        Game(2, "Elden Ring", "Fantasy RPG adventure.", GameCategory.ACTION, "elden_ring"),
        Game(3, "Valorant", "5v5 character-based tactical shooter.", GameCategory.ACTION, "valorant"),
        Game(4, "The Witcher 3", "Story-driven open world RPG.", GameCategory.RPG, "witcher3"),
        Game(5, "Hades", "God-like rogue-like dungeon crawler.", GameCategory.ACTION, "hades"),
        Game(6, "Dota 2", "Competitive game of action and strategy.", GameCategory.STRATEGY, "dota2")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.toolbar.setNavigationOnClickListener { finish() }

        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterGames(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterGames(newText)
                return true
            }
        })
    }

    private fun filterGames(query: String?) {
        if (query.isNullOrBlank()) {
            binding.rvSearchResults.adapter = null
            binding.tvNoResults.visibility = View.GONE
            return
        }

        val filteredList = allGames.filter { it.title.contains(query, ignoreCase = true) }
        
        if (filteredList.isEmpty()) {
            binding.rvSearchResults.adapter = null
            binding.tvNoResults.visibility = View.VISIBLE
        } else {
            binding.tvNoResults.visibility = View.GONE
            binding.rvSearchResults.adapter = GamesAdapter(filteredList, GamesAdapter.TYPE_POSTER)
        }
    }
}
