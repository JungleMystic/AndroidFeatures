package com.lrm.androidfeatures.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.lrm.androidfeatures.databinding.ActivityEmailBinding

class EmailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEmailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityEmailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        binding.sendEmailButton.setOnClickListener {
            val emailAddress = binding.emailAddress.text.toString()
            val emailSubject = binding.emailSubject.text.toString()
            val message = binding.message.text.toString()

            sendEmail(emailAddress, emailSubject, message)
        }
    }

    private fun sendEmail(emailAddress: String, emailSubject: String, message: String) {

        val emailAddresses = arrayOf(emailAddress)

        if (emailAddress.isEmpty() || emailSubject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Please fill all the fields", Toast.LENGTH_SHORT).show()
        } else {
            val emailIntent = Intent(Intent.ACTION_SENDTO)
            emailIntent.data = Uri.parse("mailto:")
            emailIntent.putExtra(Intent.EXTRA_EMAIL, emailAddresses)
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, emailSubject)
            emailIntent.putExtra(Intent.EXTRA_TEXT, message)

            if (emailIntent.resolveActivity(packageManager) != null) {
                startActivity(Intent.createChooser(emailIntent, "Choose an app"))
                //startActivity(emailIntent)
            } else {
                Toast.makeText(this, "Required app is not installed", Toast.LENGTH_SHORT).show()
            }
        }
    }
}