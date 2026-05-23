package com.example.fumbogamelauncher.ui

import android.os.Bundle
import android.view.*
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.fumbogamelauncher.MainActivity
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.databinding.FragmentGamesBinding
import com.example.fumbogamelauncher.databinding.LayoutGamesListBinding
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.model.GameCategory
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayoutMediator

/**
 * [Fragment dan Lifecycle]: Fragmen modular untuk mengelola tampilan utama toko game.
 */
class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // [Slicing UI]: Menggunakan ViewBinding untuk akses komponen layout fragment_games.
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // [Lifecycle]: Inisialisasi komponen UI setelah view berhasil dibuat.
        setupToolbar()
        setupViewPager()
        setupOptionsMenu()
    }

    private fun setupToolbar() {
        // [Material Design 3 Components]: Mengatur navigasi ikon hamburger untuk membuka drawer.
        binding.toolbar.setNavigationOnClickListener {
            (activity as? MainActivity)?.openDrawer()
        }

        // [Advanced Navigation]: Navigasi ke aktivitas pencarian melalui tombol search.
        binding.btnSearch.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), SearchActivity::class.java))
        }
    }

    /**
     * [Tab Layout] dan [ViewPager2]: Implementasi navigasi horizontal antar konten toko.
     */
    private fun setupViewPager() {
        val adapter = GamesPagerAdapter(this)
        binding.viewPager.adapter = adapter

        // [Tab Layout]: Menghubungkan TabLayout dengan ViewPager2 menggunakan Mediator.
        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Discover"
                1 -> "Trending"
                2 -> "New Releases"
                else -> "Special Offers"
            }
        }.attach()
    }

    /**
     * [Option Menu]: Menambahkan menu tiga titik di pojok kanan atas untuk aksi sekunder.
     */
    private fun setupOptionsMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                // [Option Menu]: Memuat menu dari resource main_option_menu XML.
                menuInflater.inflate(R.menu.main_option_menu, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
                // [Navigation Component Dasar]: Menangani perpindahan layar lewat navigasi graph.
                return when (item.itemId) {
                    R.id.action_settings -> {
                        findNavController().navigate(R.id.navigation_settings)
                        true
                    }
                    R.id.action_about -> {
                        findNavController().navigate(R.id.navigation_about)
                        true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // [Lifecycle]: Membersihkan binding untuk menghindari kebocoran memori.
        _binding = null
    }

    /**
     * Adapter untuk ViewPager2 yang mengelola daftar fragmen di dalam tab.
     */
    class GamesPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {
        override fun getItemCount(): Int = 4
        override fun createFragment(position: Int): Fragment {
            return GameListContentFragment.newInstance(when (position) {
                0 -> "discover"
                1 -> "trending"
                2 -> "new"
                else -> "special"
            })
        }
    }
}

/**
 * [Fragment dan Lifecycle]: Fragmen konten di dalam ViewPager2 untuk menampilkan daftar game.
 */
class GameListContentFragment : Fragment() {
    private var _binding: LayoutGamesListBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TYPE = "type"
        fun newInstance(type: String) = GameListContentFragment().apply {
            // [Advanced Navigation]: Mengirim data antar fragment secara aman lewat bundle.
            arguments = Bundle().apply { putString(ARG_TYPE, type) }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = LayoutGamesListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val type = arguments?.getString(ARG_TYPE) ?: "discover"
        
        // [Lifecycle]: Menyiapkan data game berdasarkan tipe tab yang dipilih.
        setupRecyclerView(type)
        
        // [Advanced Navigation]: Aksi tombol hero untuk membuka detail game terpilih.
        binding.btnGetNow.setOnClickListener {
            val featuredGame = Game(101, "After Bloom", "Game petualangan lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom")
            val intent = android.content.Intent(requireContext(), GameDetailActivity::class.java).apply {
                putExtra(GameDetailActivity.EXTRA_GAME, featuredGame)
            }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(type: String) {
        val afterBloom = Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom")
        val cyberpunk = Game(1, "Cyberpunk 2077", "Masa depan distopia Night City.", GameCategory.RPG, "cyberpunk")
        val eldenRing = Game(2, "Elden Ring", "Petualangan aksi fantasi.", GameCategory.ACTION, "elden_ring")
        val valorant = Game(3, "Valorant", "Penembak taktis 5v5.", GameCategory.ACTION, "valorant")
        val witcher3 = Game(4, "The Witcher 3", "RPG dunia terbuka naratif.", GameCategory.RPG, "witcher3")
        val hades = Game(5, "Hades", "Penjelajahan penjara bawah tanah.", GameCategory.ACTION, "hades")
        val dota2 = Game(6, "Dota 2", "Game kompetitif strategi MOBA.", GameCategory.STRATEGY, "dota2")

        val allGames = listOf(afterBloom, cyberpunk, eldenRing, valorant, witcher3, hades, dota2)

        val games = when (type) {
            "trending" -> listOf(valorant, cyberpunk, eldenRing)
            "new" -> listOf(afterBloom, hades, witcher3)
            "special" -> listOf(hades, dota2, cyberpunk)
            else -> allGames
        }

        // [Slicing UI]: Menampilkan daftar game dalam orientasi horizontal bergaya poster.
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvGames.adapter = GamesAdapter(games, GamesAdapter.TYPE_POSTER)

        // [Slicing UI]: Menampilkan daftar game tambahan dalam format grid 2 kolom.
        binding.rvGamesGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvGamesGrid.adapter = GamesAdapter(games.shuffled(), GamesAdapter.TYPE_GRID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
