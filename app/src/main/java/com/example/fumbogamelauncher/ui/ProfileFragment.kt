package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fumbogamelauncher.databinding.FragmentProfileBinding

/**
 * [Fragment dan Lifecycle]: Fragmen modular untuk menampilkan profil pengguna dan statistik.
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // [Slicing UI]: Menggunakan ViewBinding untuk memisahkan desain XML dengan kode.
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        // [Lifecycle]: Menambahkan listener untuk interaksi pada komponen UI.
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Mengedit Profil...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnPayment.setOnClickListener {
            Toast.makeText(context, "Metode Pembayaran...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnSecurity.setOnClickListener {
            Toast.makeText(context, "Pengaturan Keamanan...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "Keluar dari aplikasi...", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // [Lifecycle]: Membersihkan referensi binding saat view dihancurkan.
        _binding = null
    }
}
