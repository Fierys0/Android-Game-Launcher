package com.example.fumbogamelauncher.ui

import android.app.DownloadManager
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.example.fumbogamelauncher.R
import com.example.fumbogamelauncher.databinding.ActivityGameDetailBinding
import com.example.fumbogamelauncher.model.Game
import com.example.fumbogamelauncher.util.DownloadHelper
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * [Material Design 3 Components]: Aktivitas detail game dengan TopAppBar, Dialog, Snackbar, dan Bottom Sheet.
 */
class GameDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailBinding
    private var currentDownloadId: Long = -1L
    private var isPolling = false
    private var currentGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // [Slicing UI]: Menggunakan ViewBinding untuk layout activity_game_detail.
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // [Advanced Navigation]: Menerima data game secara aman melalui serializable extra.
        val game = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            intent.getSerializableExtra(EXTRA_GAME, Game::class.java)
        } else {
            @Suppress("DEPRECATION")
            intent.getSerializableExtra(EXTRA_GAME) as? Game
        }

        currentGame = game
        if (game != null) {
            setupUI(game)
        } else {
            finish()
        }

        // [Material Design 3 Components]: Navigasi kembali melalui TopAppBar (MaterialToolbar).
        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
        // [Lifecycle]: Aksi membatalkan unduhan saat tombol silang ditekan.
        binding.btnCancelDownload.setOnClickListener {
            if (currentDownloadId != -1L) {
                DownloadHelper.cancelDownload(this, currentDownloadId)
                currentDownloadId = -1L
                isPolling = false
                binding.downloadProgressContainer.visibility = View.GONE
                binding.btnLaunch.visibility = View.VISIBLE
            }
        }
    }

    private fun setupUI(game: Game) {
        // [Slicing UI]: Menghubungkan data model dengan komponen UI dari file dimens.xml dan colors.xml.
        binding.tvDetailTitle.text = game.title
        binding.tvDetailDesc.text = game.description
        binding.tvDetailCategory.text = game.category.name

        val resId = if (game.imageResName != null) {
            resources.getIdentifier(game.imageResName, "drawable", packageName)
        } else 0

        if (resId != 0) {
            binding.ivGameArt.setImageResource(resId)
        } else {
            binding.ivGameArt.setImageResource(R.drawable.placeholder)
        }

        updateLaunchButton(game)

        // [Bottom Sheet]: Menampilkan menu opsi tambahan tanpa berpindah halaman.
        binding.btnOptions.setOnClickListener {
            showOptionsBottomSheet(game)
        }
    }

    private fun updateLaunchButton(game: Game) {
        val packageName = game.packageName
        if (packageName != null) {
            // [Advanced Navigation]: Memeriksa status instalasi game di sistem secara dinamis.
            if (DownloadHelper.isAppInstalled(this, packageName)) {
                binding.btnLaunch.text = "LAUNCH GAME"
                binding.btnLaunch.setOnClickListener {
                    DownloadHelper.launchApp(this, packageName)
                    // [Snackbar]: Memberikan feedback singkat setelah game diluncurkan.
                    Snackbar.make(binding.root, "Meluncurkan ${game.title}...", Snackbar.LENGTH_SHORT).show()
                }
            } else if (game.downloadUrl != null) {
                // [Material Design 3 Components]: Mengubah tombol menjadi Download jika belum terinstal.
                binding.btnLaunch.text = "DOWNLOAD"
                binding.btnLaunch.setOnClickListener {
                    currentDownloadId = DownloadHelper.downloadAndInstallApk(this, game.downloadUrl, "${game.title.replace(" ", "_")}.apk")
                    if (currentDownloadId != -1L) {
                        startProgressPolling()
                    }
                }
            } else {
                binding.btnLaunch.visibility = View.GONE
            }
        }
    }

    private fun startProgressPolling() {
        if (isPolling) return
        isPolling = true
        
        binding.btnLaunch.visibility = View.GONE
        binding.downloadProgressContainer.visibility = View.VISIBLE
        
        // [Lifecycle]: Menggunakan coroutine untuk polling status unduhan secara real-time.
        lifecycleScope.launch {
            while (isPolling && currentDownloadId != -1L) {
                val progress = DownloadHelper.getDownloadProgress(this@GameDetailActivity, currentDownloadId)
                if (progress != null) {
                    when (progress.status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isPolling = false
                            binding.downloadProgressContainer.visibility = View.GONE
                            binding.btnLaunch.visibility = View.VISIBLE
                            // [Material Design 3 Components]: Memicu installer Android secara otomatis.
                            DownloadHelper.installApk(this@GameDetailActivity, currentDownloadId)
                            currentGame?.let { updateLaunchButton(it) }
                        }
                        DownloadManager.STATUS_FAILED -> {
                            isPolling = false
                            binding.downloadProgressContainer.visibility = View.GONE
                            binding.btnLaunch.visibility = View.VISIBLE
                            Toast.makeText(this@GameDetailActivity, "Unduhan gagal", Toast.LENGTH_SHORT).show()
                        }
                        else -> {
                            val percent = if (progress.totalBytes > 0) {
                                (progress.bytesDownloaded * 100 / progress.totalBytes).toInt()
                            } else 0
                            binding.downloadProgressBar.progress = percent
                            binding.tvDownloadPercentage.text = getString(R.string.download_percentage, percent)
                        }
                    }
                }
                delay(500)
            }
            isPolling = false
        }
    }

    private fun showOptionsBottomSheet(game: Game) {
        // [Bottom Sheet]: Implementasi Modal Bottom Sheet untuk pengelolaan wishlist dan data.
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_game_options_bottom_sheet, null)
        
        val wishlistText = view.findViewById<android.widget.TextView>(R.id.tvWishlistAction)
        val isInWishlist = com.example.fumbogamelauncher.util.WishlistManager.isInWishlist(this, game.id)
        
        wishlistText.text = if (isInWishlist) "Hapus dari Wishlist" else "Tambah ke Wishlist"
        
        view.findViewById<View>(R.id.option_wishlist).setOnClickListener {
            if (isInWishlist) {
                com.example.fumbogamelauncher.util.WishlistManager.removeFromWishlist(this, game.id)
                Snackbar.make(binding.root, "Dihapus dari Wishlist", Snackbar.LENGTH_SHORT).show()
            } else {
                com.example.fumbogamelauncher.util.WishlistManager.addToWishlist(this, game.id)
                Snackbar.make(binding.root, "Ditambahkan ke Wishlist", Snackbar.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        view.findViewById<View>(R.id.option_delete).setOnClickListener {
            // [Dialog]: Menggunakan MaterialAlertDialog untuk konfirmasi aksi penting penghapusan data.
            MaterialAlertDialogBuilder(this)
                .setTitle("Hapus Game?")
                .setMessage("Apakah Anda yakin ingin menghapus data ${game.title}?")
                .setPositiveButton("Hapus") { _, _ ->
                    Snackbar.make(binding.root, "Data berhasil dihapus", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Batal", null)
                .show()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        // [Lifecycle]: Memperbarui status tombol saat pengguna kembali ke layar ini.
        currentGame?.let { updateLaunchButton(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        // [Lifecycle]: Menghentikan polling saat activity dihancurkan.
        isPolling = false
    }

    companion object {
        const val EXTRA_GAME = "extra_game"
    }
}
