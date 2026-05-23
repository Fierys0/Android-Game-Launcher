package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.fumbogamelauncher.databinding.LayoutLibraryListBinding
import com.example.fumbogamelauncher.util.WishlistManager

/**
 * [Fragment dan Lifecycle]: Fragmen modular untuk menampilkan daftar keinginan pengguna.
 */
class WishlistFragment : Fragment() {
    private var _binding: LayoutLibraryListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // [Slicing UI]: Memuat layout XML menggunakan ViewBinding.
        _binding = LayoutLibraryListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // [Lifecycle]: Memulai inisialisasi antarmuka pengguna.
        setupUI()
    }

    private fun setupUI() {
        binding.tvLibTitle.text = "WISHLIST"
        
        // [Advanced Navigation]: Memperoleh data wishlist terbaru dari manager penyimpanan.
        val wishlistGames = WishlistManager.getWishlistGames(requireContext())
        
        binding.tvLibCount.text = "${wishlistGames.size} Game Disimpan"
        
        // Menangani tampilan jika daftar kosong.
        if (wishlistGames.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvLibrary.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvLibrary.visibility = View.VISIBLE
            // [Slicing UI]: Menerapkan tata letak grid untuk daftar game.
            binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvLibrary.adapter = GamesAdapter(wishlistGames, GamesAdapter.TYPE_GRID)
        }
    }

    override fun onResume() {
        super.onResume()
        // [Lifecycle]: Memastikan data selalu sinkron saat pengguna kembali ke layar ini.
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
