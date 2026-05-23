package com.example.fumbogamelauncher

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.fumbogamelauncher.databinding.ActivityMainBinding
import com.example.fumbogamelauncher.util.ThemeHelper

/**
 * [Advanced Navigation]: Main Activity managing the Back Stack and Navigation Drawer.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // [Material Design 3]: Splash Screen API
        installSplashScreen()
        
        // Apply theme before layout inflation
        ThemeHelper.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        
        // [Slicing UI]: Use ViewBinding for the main layout
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [Navigation Component (Dasar)]: Initialize NavController from NavHostFragment
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // [Bottom Navigation]: Link BottomNavigationView with NavController
        binding.bottomNavigation.setupWithNavController(navController)
        
        // [Navigation Drawer]: Link NavigationView with NavController
        binding.navView.setupWithNavController(navController)

        // Enable Drawer swipe for all screens
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    /**
     * Handle Hamburger Icon / Back button in Toolbar if any (or manual open)
     */
    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        // [Advanced Navigation]: Manage up navigation with drawer support
        return NavigationUI.navigateUp(navController, binding.drawerLayout) || super.onSupportNavigateUp()
    }
    
    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}
