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
 * [Fragment & Lifecycle]: Fragment managing the main Store/Games view with Steam/Epic aesthetic.
 */
class GamesFragment : Fragment() {

    private var _binding: FragmentGamesBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGamesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupToolbar()
        setupViewPager()
        setupOptionsMenu()
    }

    private fun setupToolbar() {
        binding.toolbar.setNavigationOnClickListener {
            (activity as? MainActivity)?.openDrawer()
        }

        binding.btnSearch.setOnClickListener {
            startActivity(android.content.Intent(requireContext(), SearchActivity::class.java))
        }
    }

    /**
     * [Tab Layout] & [ViewPager2]: Horizontal navigation between tabs.
     */
    private fun setupViewPager() {
        val adapter = GamesPagerAdapter(this)
        binding.viewPager.adapter = adapter

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
     * [Option Menu]: Menu in the top right corner.
     */
    private fun setupOptionsMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.main_option_menu, menu)
            }

            override fun onMenuItemSelected(item: MenuItem): Boolean {
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
        _binding = null
    }

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
 * [Fragment & Lifecycle]: Content Fragment inside ViewPager2.
 */
class GameListContentFragment : Fragment() {
    private var _binding: LayoutGamesListBinding? = null
    private val binding get() = _binding!!

    companion object {
        private const val ARG_TYPE = "type"
        fun newInstance(type: String) = GameListContentFragment().apply {
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
        setupRecyclerView(type)
        
        val featuredGame = if (type == "discover") {
            Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom")
        } else {
            Game(1, "Cyberpunk 2077", "Experience the dystopian future of Night City.", GameCategory.RPG, "cyberpunk")
        }

        binding.tvHeroTitle.text = featuredGame.title
        binding.tvHeroDesc.text = featuredGame.description
        
        val resId = if (featuredGame.imageResName != null) {
            resources.getIdentifier(featuredGame.imageResName, "drawable", requireContext().packageName)
        } else 0
        if (resId != 0) binding.ivHero.setImageResource(resId)

        binding.btnGetNow.setOnClickListener {
            val intent = android.content.Intent(requireContext(), GameDetailActivity::class.java).apply {
                putExtra(GameDetailActivity.EXTRA_GAME, featuredGame)
            }
            startActivity(intent)
        }
    }

    private fun setupRecyclerView(type: String) {
        val afterBloom = Game(101, "After Bloom", "Game petualangan indah buatan developer lokal.", GameCategory.ADVENTURE, "placeholder", "https://github.com/Fierys0/AfterBloom/releases/download/DEBUG_V0.1/AfterBloom_Android_x64_ALLABI.apk", "com.miruku.afterbloom")
        val cyberpunk = Game(1, "Cyberpunk 2077", "Experience the dystopian future of Night City.", GameCategory.RPG, "cyberpunk")
        val eldenRing = Game(2, "Elden Ring", "Fantasy RPG adventure.", GameCategory.ACTION, "elden_ring")
        val valorant = Game(3, "Valorant", "5v5 character-based tactical shooter.", GameCategory.ACTION, "valorant")
        val witcher3 = Game(4, "The Witcher 3", "Story-driven open world RPG.", GameCategory.RPG, "witcher3")
        val hades = Game(5, "Hades", "God-like rogue-like dungeon crawler.", GameCategory.ACTION, "hades")
        val dota2 = Game(6, "Dota 2", "Competitive game of action and strategy.", GameCategory.STRATEGY, "dota2")

        val allGames = listOf(afterBloom, cyberpunk, eldenRing, valorant, witcher3, hades, dota2)

        val games = when (type) {
            "trending" -> listOf(valorant, cyberpunk, eldenRing)
            "new" -> listOf(afterBloom, hades, witcher3)
            "special" -> listOf(hades, dota2, cyberpunk)
            else -> allGames // Discover tab displays all games
        }

        // Horizontal Row (Poster Style)
        binding.rvGames.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        binding.rvGames.adapter = GamesAdapter(games, GamesAdapter.TYPE_POSTER)

        // Vertical Grid (Grid Style)
        binding.rvGamesGrid.layoutManager = GridLayoutManager(requireContext(), 2)
        binding.rvGamesGrid.adapter = GamesAdapter(games.shuffled(), GamesAdapter.TYPE_GRID)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
