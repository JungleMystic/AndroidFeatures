package com.lrm.androidfeatures.activities

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.media.AudioManager
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.databinding.ActivityRingerBinding

class RingerActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRingerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRingerBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        if (
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
            && !notificationManager.isNotificationPolicyAccessGranted
        ) {
            startActivity(Intent(Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS))
        }

        val audioManager = getSystemService(Context.AUDIO_SERVICE) as AudioManager

        checkCurrentMode(audioManager)

        binding.ringerMode.setOnClickListener {
            audioManager.ringerMode = AudioManager.RINGER_MODE_NORMAL
            Toast.makeText(this, "Ringer mode", Toast.LENGTH_SHORT).show()
            checkCurrentMode(audioManager)
        }

        binding.vibrationMode.setOnClickListener {
            audioManager.ringerMode = AudioManager.RINGER_MODE_VIBRATE
            Toast.makeText(this, "Vibration mode", Toast.LENGTH_SHORT).show()
            checkCurrentMode(audioManager)
        }

        binding.silentMode.setOnClickListener {
            audioManager.ringerMode = AudioManager.RINGER_MODE_SILENT
            Toast.makeText(this, "Silent mode", Toast.LENGTH_SHORT).show()
            checkCurrentMode(audioManager)
        }
    }

    private fun checkCurrentMode(audioManager: AudioManager) {
        when(audioManager.ringerMode) {
            AudioManager.RINGER_MODE_NORMAL -> binding.currentMode.text = resources.getString(R.string.ring_mode,"Ringing")
            AudioManager.RINGER_MODE_VIBRATE -> binding.currentMode.text = resources.getString(R.string.ring_mode,"Vibration")
            AudioManager.RINGER_MODE_SILENT -> binding.currentMode.text = resources.getString(R.string.ring_mode,"Silent")
        }
    }
}