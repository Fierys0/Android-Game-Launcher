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
 * [Fragment dan Lifecycle]: Fragmen modular untuk mengelola pengaturan aplikasi.
 */
class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // [Slicing UI]: Menggunakan ViewBinding untuk mengikat tata letak XML.
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        return binding.root
    }

    /**
     * [Lifecycle]: Inisialisasi logika pengaturan di onViewCreated.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // Memuat status tema yang tersimpan.
        binding.switchDarkMode.isChecked = ThemeHelper.isDarkMode(requireContext())
        
        binding.switchDarkMode.setOnCheckedChangeListener { _, isChecked ->
            // [Dialog]: Menggunakan MaterialAlertDialog untuk konfirmasi perubahan tema aplikasi.
            MaterialAlertDialogBuilder(requireContext())
                .setTitle("Ganti Tema?")
                .setMessage("Apakah Anda yakin ingin mengubah tema aplikasi?")
                .setPositiveButton("Ya") { _, _ ->
                    ThemeHelper.setDarkMode(requireContext(), isChecked)
                    // [Snackbar]: Memberikan feedback instan setelah tema diperbarui.
                    Snackbar.make(binding.root, "Tema berhasil diperbarui!", Snackbar.LENGTH_SHORT).show()
                    
                    // Melakukan refresh aktivitas untuk menerapkan tema baru secara menyeluruh.
                    requireActivity().recreate()
                }
                .setNegativeButton("Batal") { _, _ ->
                    binding.switchDarkMode.isChecked = !isChecked
                }
                .show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // [Lifecycle]: Membersihkan referensi binding untuk mencegah kebocoran memori.
        _binding = null
    }
}
