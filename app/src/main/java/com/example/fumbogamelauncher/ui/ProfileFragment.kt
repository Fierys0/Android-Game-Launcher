package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.fumbogamelauncher.databinding.FragmentProfileBinding

/**
 * [Fragment & Lifecycle]: Fragment displaying dummy profile data with interactive buttons.
 */
class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        binding.btnEditProfile.setOnClickListener {
            Toast.makeText(context, "Editing Profile...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnPayment.setOnClickListener {
            Toast.makeText(context, "Opening Payment Methods...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnSecurity.setOnClickListener {
            Toast.makeText(context, "Privacy & Security Settings...", Toast.LENGTH_SHORT).show()
        }
        
        binding.btnLogout.setOnClickListener {
            Toast.makeText(context, "Signing out...", Toast.LENGTH_LONG).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
