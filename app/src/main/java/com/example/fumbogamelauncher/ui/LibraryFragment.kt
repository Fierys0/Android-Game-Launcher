package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fumbogamelauncher.databinding.LayoutLibraryListBinding
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory

/**
 * [Fragment & Lifecycle]: Fragment displaying the user's game library.
 */
class LibraryFragment : Fragment() {
    private var _binding: LayoutLibraryListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = LayoutLibraryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
    }

    private fun setupRecyclerView() {
        val games = listOf(
            Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom"),
            Game(3, "Valorant", "", GameCategory.ACTION, "valorant"),
            Game(1, "Cyberpunk 2077", "", GameCategory.RPG, "cyberpunk"),
            Game(2, "Elden Ring", "", GameCategory.ACTION, "elden_ring"),
            Game(4, "The Witcher 3", "", GameCategory.RPG, "witcher3"),
            Game(5, "Hades", "", GameCategory.ACTION, "hades"),
            Game(6, "Dota 2", "", GameCategory.STRATEGY, "dota2")
        )

        binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvLibrary.adapter = GamesAdapter(games)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
