package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.fumbogamelauncher.databinding.ActivitySearchBinding
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory

/**
 * [Advanced Navigation]: Aktivitas khusus untuk fitur pencarian game secara global.
 */
class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private val allGames = listOf(
        Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom"),
        Game(1, "Cyberpunk 2077", "Masa depan distopia Night City.", GameCategory.RPG, "cyberpunk"),
        Game(2, "Elden Ring", "Petualangan aksi fantasi.", GameCategory.ACTION, "elden_ring"),
        Game(3, "Valorant", "Penembak taktis 5v5.", GameCategory.ACTION, "valorant"),
        Game(4, "The Witcher 3", "RPG dunia terbuka naratif.", GameCategory.RPG, "witcher3"),
        Game(5, "Hades", "Penjelajahan penjara bawah tanah.", GameCategory.ACTION, "hades"),
        Game(6, "Dota 2", "Game kompetitif strategi MOBA.", GameCategory.STRATEGY, "dota2")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [Slicing UI]: Memuat tampilan aktivitas melalui ViewBinding.
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [Material Design 3 Components]: Navigasi kembali melalui ikon di toolbar.
        binding.toolbar.setNavigationOnClickListener { finish() }

        // [Lifecycle]: Menyiapkan input pencarian.
        setupSearchView()
    }

    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterGames(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // [Lifecycle]: Memperbarui hasil pencarian secara real-time saat teks berubah.
                filterGames(newText)
                return true
            }
        })
    }

    private fun filterGames(query: String?) {
        // [Slicing UI]: Menangani tampilan jika query kosong.
        if (query.isNullOrBlank()) {
            binding.rvSearchResults.adapter = null
            binding.tvNoResults.visibility = View.GONE
            return
        }

        // Logika penyaringan data berdasarkan judul game.
        val filteredList = allGames.filter { it.title.contains(query, ignoreCase = true) }
        
        if (filteredList.isEmpty()) {
            binding.rvSearchResults.adapter = null
            binding.tvNoResults.visibility = View.VISIBLE
        } else {
            binding.tvNoResults.visibility = View.GONE
            // [Slicing UI]: Menghubungkan data hasil filter ke RecyclerView.
            binding.rvSearchResults.adapter = GamesAdapter(filteredList, GamesAdapter.TYPE_POSTER)
        }
    }
}
