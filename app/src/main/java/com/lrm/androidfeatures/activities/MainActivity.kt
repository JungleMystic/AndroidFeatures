package com.lrm.androidfeatures.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lrm.androidfeatures.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

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
            val contactsIntent = Intent(this, ContactsActivity::class.java)
            startActivity(contactsIntent)
        }
    }
}