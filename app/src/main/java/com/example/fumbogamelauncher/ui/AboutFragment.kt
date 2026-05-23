package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fumbogamelauncher.databinding.FragmentPlaceholderBinding

/**
 * [Fragment & Lifecycle]: Fragment untuk menampilkan informasi aplikasi (About).
 */
class AboutFragment : Fragment() {
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

    /**
     * [Lifecycle]: Inisialisasi teks informasi di onViewCreated.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.tvPlaceholder.text = "Fumbo Game Launcher\nVersi 1.0"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
