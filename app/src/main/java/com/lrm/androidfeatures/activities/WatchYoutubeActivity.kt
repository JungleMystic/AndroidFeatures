package com.lrm.androidfeatures.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.activity.OnBackPressedCallback
import com.lrm.androidfeatures.databinding.ActivityWatchYoutubeBinding
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.FullscreenListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView

class WatchYoutubeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityWatchYoutubeBinding
    private lateinit var youtubePlayer: YouTubePlayer

    private var isFullscreen = false

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            if (isFullscreen) {
                youtubePlayer.toggleFullscreen()
            } else {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityWatchYoutubeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        onBackPressedDispatcher.addCallback(onBackPressedCallback)

        //binding.backIcon.setOnClickListener { onBackPressed() }

        val youtubePlayerView = binding.youtubePlayerView
        val fullscreenViewContainer = binding.fullScreenViewContainer

        val iFrameOptions = IFramePlayerOptions.Builder()
            .controls(1)
            .fullscreen(1)
            .build()

        youtubePlayerView.enableAutomaticInitialization = false

        youtubePlayerView.addFullscreenListener(object : FullscreenListener {
            override fun onEnterFullscreen(fullscreenView: View, exitFullscreen: () -> Unit) {
                isFullscreen = true

                youtubePlayerView.visibility = View.GONE
                fullscreenViewContainer.visibility = View.VISIBLE
                binding.appBar.visibility = View.GONE
                fullscreenViewContainer.addView(fullscreenView)
            }

            override fun onExitFullscreen() {
                isFullscreen = false

                youtubePlayerView.visibility = View.VISIBLE
                fullscreenViewContainer.visibility = View.GONE
                binding.appBar.visibility = View.VISIBLE
                fullscreenViewContainer.removeAllViews()
            }
        })

        youtubePlayerView.initialize(object : AbstractYouTubePlayerListener() {
            override fun onReady(youTubePlayer: YouTubePlayer) {
                this@WatchYoutubeActivity.youtubePlayer = youTubePlayer
                youTubePlayer.loadVideo("kRr1jboH6eA", 0f)


                binding.enterFullscreenButton.setOnClickListener {
                    youTubePlayer.toggleFullscreen()
                }
            }
        }, iFrameOptions)

        lifecycle.addObserver(youtubePlayerView)
    }
}