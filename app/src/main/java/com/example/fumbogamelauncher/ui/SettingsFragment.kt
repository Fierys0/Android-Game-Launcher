package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fumbogamelauncher.databinding.FragmentSettingsBinding
import com.example.fumbogamelauncher.util.ThemeHelper
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar

/**
 * [Fragment & Lifecycle]: Fragment managing application settings.
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.switchDarkMode.isChecked = ThemeHelper.isDarkMode(requireContext())
        
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Change Theme?")
                .setMessage("Are you sure you want to change the app theme?")
                .setPositiveButton("Yes") { _, _ ->
                    ThemeHelper.setDarkMode(requireContext(), isChecked)
                    Snackbar.make(binding.root, "Theme updated!", Snackbar.LENGTH_SHORT).show()
                    // Recreate is handled by setDefaultNightMode in most cases, 
                    // but for immediate effect in this setup:
                    requireActivity().recreate()
                }
                .setNegativeButton("Cancel") { _, _ ->
                    binding.switchDarkMode.isChecked = !isChecked
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
