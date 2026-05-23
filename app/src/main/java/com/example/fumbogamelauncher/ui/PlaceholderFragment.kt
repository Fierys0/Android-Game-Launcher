package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.databinding.FragmentPlaceholderBinding

/**
 * [Fragment & Lifecycle]: Improved Placeholder Fragment with actual interactive content.
 */
class PlaceholderFragment : Fragment() {
    private var _binding: FragmentPlaceholderBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPlaceholderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val title = arguments?.getString("arg_title") ?: "Halaman Baru"
        binding.tvPlaceholder.text = title
        
        // Customize based on title
        when {
            title.contains("Wishlist", ignoreCase = true) -> {
                binding.ivPlaceholderIcon.setImageResource(R.drawable.ic_games)
                binding.tvPlaceholderDesc.text = "You haven't added any games to your wishlist yet."
            }
            title.contains("Account", ignoreCase = true) -> {
                binding.ivPlaceholderIcon.setImageResource(R.drawable.ic_account)
                binding.tvPlaceholderDesc.text = "Manage your profile and account settings here."
            }
            title.contains("About", ignoreCase = true) -> {
                binding.ivPlaceholderIcon.setImageResource(R.drawable.ic_launcher_foreground)
                binding.tvPlaceholderDesc.text = "Fumbo Launcher v1.0.0 - Built with Love by Fumbo Studios."
            }
        }

        binding.btnGoStore.setOnClickListener {
            findNavController().navigate(R.id.navigation_games)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
