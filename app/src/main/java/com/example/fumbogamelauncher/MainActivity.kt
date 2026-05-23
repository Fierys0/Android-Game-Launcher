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
 * [Advanced Navigation]: Activity utama yang mengelola Back Stack secara otomatis melalui Navigation Component.
 */
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        // [Material Design 3 Components]: Implementasi Splash Screen API sesuai standar Material 3.
        installSplashScreen()
        
        // [Slicing UI dan Design System Dasar]: Menerapkan tema aplikasi sebelum layout di-inflate.
        ThemeHelper.applyTheme(this)
        
        super.onCreate(savedInstanceState)
        
        // [Slicing UI]: Menggunakan ViewBinding untuk memisahkan logika kode dengan layout XML.
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [Navigation Component Dasar]: Inisialisasi NavController menggunakan NavHostFragment dari XML.
        val navHostFragment = supportFragmentManager
            .findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // [Bottom Navigation]: Menghubungkan BottomNavigationView dengan NavController untuk akses menu utama.
        binding.bottomNavigation.setupWithNavController(navController)
        
        // [Advanced Navigation]: Menghubungkan NavigationView (Drawer) dengan NavController agar sinkron.
        binding.navView.setupWithNavController(navController)

        // [Fragment dan Lifecycle]: Memastikan Drawer dapat diakses dengan swipe di semua layar.
        binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
    }

    /**
     * Membuka menu samping secara manual melalui kode.
     */
    fun openDrawer() {
        binding.drawerLayout.openDrawer(GravityCompat.START)
    }

    override fun onSupportNavigateUp(): Boolean {
        // [Advanced Navigation]: Mengelola navigasi 'Up' dengan dukungan untuk menu drawer.
        return NavigationUI.navigateUp(navController, binding.drawerLayout) || super.onSupportNavigateUp()
    }
    
    @Deprecated("Metode lama untuk menangani tombol kembali")
    override fun onBackPressed() {
        // [Lifecycle]: Menangani penutupan drawer saat tombol back ditekan.
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        } else {
            @Suppress("DEPRECATION")
            super.onBackPressed()
        }
    }
}
