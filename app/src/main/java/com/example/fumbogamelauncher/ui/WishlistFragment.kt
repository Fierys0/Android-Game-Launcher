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
 * Fragment displaying the user's wishlist.
 */
class WishlistFragment : Fragment() {
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
        setupUI()
    }

    private fun setupUI() {
        binding.tvLibTitle.text = "WISHLIST"
        val wishlistGames = WishlistManager.getWishlistGames(requireContext())
        
        binding.tvLibCount.text = "${wishlistGames.size} Games Saved"
        
        if (wishlistGames.isEmpty()) {
            binding.tvEmptyMessage.visibility = View.VISIBLE
            binding.rvLibrary.visibility = View.GONE
        } else {
            binding.tvEmptyMessage.visibility = View.GONE
            binding.rvLibrary.visibility = View.VISIBLE
            binding.rvLibrary.layoutManager = GridLayoutManager(requireContext(), 2)
            binding.rvLibrary.adapter = GamesAdapter(wishlistGames, GamesAdapter.TYPE_GRID)
        }
    }

    override fun onResume() {
        super.onResume()
        // Refresh when coming back from detail
        setupUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
