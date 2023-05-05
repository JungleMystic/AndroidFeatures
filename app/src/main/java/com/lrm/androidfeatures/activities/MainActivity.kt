package com.lrm.androidfeatures.activities

import android.Manifest
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.net.ConnectivityManager
import android.net.Network
import android.os.Build
import android.os.Bundle
import android.os.CancellationSignal
import android.view.View
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private var cancellationSignal: CancellationSignal? = null

    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() = @RequiresApi(Build.VERSION_CODES.P)
        object: BiometricPrompt.AuthenticationCallback() {
            override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                notifyUser("Authentication Error: $errString")
                super.onAuthenticationError(errorCode, errString)
            }

            override fun onAuthenticationHelp(helpCode: Int, helpString: CharSequence?) {
                super.onAuthenticationHelp(helpCode, helpString)
            }

            override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                notifyUser("Authentication Succeeded")
                super.onAuthenticationSucceeded(result)
            }

            override fun onAuthenticationFailed() {
                super.onAuthenticationFailed()
            }
        }

    @RequiresApi(Build.VERSION_CODES.P)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        checkInternet()

        binding.apply {
            viewFlipper.flipInterval = 2000
            viewFlipper.setInAnimation(applicationContext, android.R.anim.fade_in)
            viewFlipper.setOutAnimation(applicationContext, android.R.anim.fade_out)
        }

        checkBiometricSupport()

        binding.callButton.setOnClickListener {
            val callIntent = Intent(this, CallActivity::class.java)
            startActivity(callIntent)
        }

        binding.smsButton.setOnClickListener {
            val smsIntent = Intent(this, SMS_Activity::class.java)
            startActivity(smsIntent)
        }

        binding.emailButton.setOnClickListener {
            val emailIntent = Intent(this, EmailActivity::class.java)
            startActivity(emailIntent)
        }

        binding.speechButton.setOnClickListener {
            val speechIntent = Intent(this, VoiceActivity::class.java)
            startActivity(speechIntent)
        }

        binding.contactsButton.setOnClickListener {
            authenticateUser()
            val contactsIntent = Intent(this, ContactsActivity::class.java)
            startActivity(contactsIntent)
        }

        binding.callHistoryButton.setOnClickListener {
            authenticateUser()
            val callHistoryIntent = Intent(this, CallHistoryActivity::class.java)
            startActivity(callHistoryIntent)
        }

        binding.flashLightButton.setOnClickListener {
            val flashIntent = Intent(this, FlashLightActivity::class.java)
            startActivity(flashIntent)
        }

        binding.soundButton.setOnClickListener {
            val soundIntent = Intent(this, RingerActivity::class.java)
            startActivity(soundIntent)
        }

        binding.locationButton.setOnClickListener {
            val locationIntent = Intent(this, LocationActivity::class.java)
            startActivity(locationIntent)
        }

        binding.showPdf.setOnClickListener {
            val pdfIntent = Intent(this, PDF_Activity::class.java)
            startActivity(pdfIntent)
        }
    }

    private fun checkInternet() {
        val connectivityManager = getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            connectivityManager.registerDefaultNetworkCallback(object : ConnectivityManager.NetworkCallback() {
                override fun onAvailable(network: Network) {
                    super.onAvailable(network)
                    Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()
                }

                override fun onLost(network: Network) {
                    super.onLost(network)
                    Toast.makeText(this@MainActivity, "Disconnected", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    @RequiresApi(Build.VERSION_CODES.P)
    fun authenticateUser() {
        val biometricPrompt = BiometricPrompt.Builder(this)
            .setTitle("Use Fingerprint")
            .setSubtitle("Authentication is required to continue")
            .setDescription("This app uses fingerprint to protect your data")
            .setNegativeButton("Cancel", this.mainExecutor, {dialogInterface, i ->
                notifyUser("Authentication cancelled")
            }).build()

        biometricPrompt.authenticate(getCancellationSignal(), mainExecutor, authenticationCallback)
    }

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Cancelled via signal")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun checkBiometricSupport(): Boolean {
        val keyguardManager = getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Lock screen security not enabled in Settings")
            return false
        }

        if (
            ActivityCompat.checkSelfPermission(this, Manifest.permission.USE_BIOMETRIC)
            != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint authentication permission not enabled")
            return false
        }
        return if (packageManager.hasSystemFeature(PackageManager.FEATURE_FINGERPRINT)) {
            true
        } else true
    }

    fun notifyUser(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }
}