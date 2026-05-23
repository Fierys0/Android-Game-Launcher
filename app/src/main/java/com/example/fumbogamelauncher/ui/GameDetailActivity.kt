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
 * [Material Design 3 Components]: Detail view with TopAppBar, Dialog, Snackbar, and Bottom Sheet.
 */
class GameDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityGameDetailBinding
    private var currentDownloadId: Long = -1L
    private var isPolling = false
    private var currentGame: Game? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

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

        binding.toolbar.setNavigationOnClickListener {
            onBackPressedDispatcher.onBackPressed()
        }
        
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

        // [Bottom Sheet]: Show extra options
        binding.btnOptions.setOnClickListener {
            showOptionsBottomSheet(game)
        }
    }

    private fun updateLaunchButton(game: Game) {
        val packageName = game.packageName
        if (packageName != null) {
            if (DownloadHelper.isAppInstalled(this, packageName)) {
                binding.btnLaunch.text = "LAUNCH GAME"
                binding.btnLaunch.setOnClickListener {
                    DownloadHelper.launchApp(this, packageName)
                    // [Snackbar]: Short feedback
                    Snackbar.make(binding.root, "Launching ${game.title}...", Snackbar.LENGTH_SHORT).show()
                }
            } else if (game.downloadUrl != null) {
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
        } else {
            binding.btnLaunch.setOnClickListener {
                Snackbar.make(binding.root, "Game not available for download", Snackbar.LENGTH_SHORT).show()
            }
        }
    }

    private fun startProgressPolling() {
        if (isPolling) return
        isPolling = true
        
        binding.btnLaunch.visibility = View.GONE
        binding.downloadProgressContainer.visibility = View.VISIBLE
        
        lifecycleScope.launch {
            while (isPolling && currentDownloadId != -1L) {
                val progress = DownloadHelper.getDownloadProgress(this@GameDetailActivity, currentDownloadId)
                if (progress != null) {
                    when (progress.status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            isPolling = false
                            binding.downloadProgressContainer.visibility = View.GONE
                            binding.btnLaunch.visibility = View.VISIBLE
                            DownloadHelper.installApk(this@GameDetailActivity, currentDownloadId)
                            currentGame?.let { updateLaunchButton(it) }
                        }
                        DownloadManager.STATUS_FAILED -> {
                            isPolling = false
                            binding.downloadProgressContainer.visibility = View.GONE
                            binding.btnLaunch.visibility = View.VISIBLE
                            Toast.makeText(this@GameDetailActivity, "Download failed", Toast.LENGTH_SHORT).show()
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
        val dialog = BottomSheetDialog(this)
        val view = layoutInflater.inflate(R.layout.layout_game_options_bottom_sheet, null)
        
        val wishlistText = view.findViewById<android.widget.TextView>(R.id.tvWishlistAction)
        val isInWishlist = com.example.fumbogamelauncher.util.WishlistManager.isInWishlist(this, game.id)
        
        wishlistText.text = if (isInWishlist) "Remove from Wishlist" else "Add to Wishlist"
        
        view.findViewById<View>(R.id.option_wishlist).setOnClickListener {
            if (isInWishlist) {
                com.example.fumbogamelauncher.util.WishlistManager.removeFromWishlist(this, game.id)
                Snackbar.make(binding.root, "Removed from Wishlist", Snackbar.LENGTH_SHORT).show()
            } else {
                com.example.fumbogamelauncher.util.WishlistManager.addToWishlist(this, game.id)
                Snackbar.make(binding.root, "Added to Wishlist", Snackbar.LENGTH_SHORT).show()
            }
            dialog.dismiss()
        }
        
        view.findViewById<View>(R.id.option_delete).setOnClickListener {
            // [Material Design 3: Dialog]: Confirmation for important action
            MaterialAlertDialogBuilder(this)
                .setTitle("Delete Game?")
                .setMessage("Are you sure you want to delete ${game.title} data?")
                .setPositiveButton("Delete") { _, _ ->
                    Snackbar.make(binding.root, "Data deleted", Snackbar.LENGTH_SHORT).show()
                }
                .setNegativeButton("Cancel", null)
                .show()
            dialog.dismiss()
        }

        dialog.setContentView(view)
        dialog.show()
    }

    override fun onResume() {
        super.onResume()
        // Refresh the launch button state when returning to the activity
        currentGame?.let { updateLaunchButton(it) }
    }

    override fun onDestroy() {
        super.onDestroy()
        isPolling = false // Stop polling when activity is destroyed
    }

    companion object {
        const val EXTRA_GAME = "extra_game"
    }
}
