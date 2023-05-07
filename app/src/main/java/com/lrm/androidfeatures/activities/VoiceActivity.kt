package com.lrm.androidfeatures.activities

import android.content.Intent
import android.os.Bundle
import android.speech.RecognizerIntent
import androidx.activity.result.ActivityResultCallback
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.databinding.ActivityVoiceBinding
import java.util.Locale

class VoiceActivity : AppCompatActivity() {

    private lateinit var binding: ActivityVoiceBinding

    lateinit var activityResultLauncher: ActivityResultLauncher<Intent>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityVoiceBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        activityResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
            ActivityResultCallback { result ->  
                val resultCode = result.resultCode
                val data = result.data

                if (resultCode == RESULT_OK && data != null) {
                    val speakResult: ArrayList<String> =
                        data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>

                    binding.resultText.text = speakResult[0]

                    when (binding.resultText.text.toString().toLowerCase()) {
                     "run" -> binding.dogImage.setImageResource(R.drawable.run_image)
                     "sit" -> binding.dogImage.setImageResource(R.drawable.sit_image)
                     "sleep" -> binding.dogImage.setImageResource(R.drawable.sleep_image)
                     "play" -> binding.dogImage.setImageResource(R.drawable.play_image)
                     "eat" -> binding.dogImage.setImageResource(R.drawable.food_image)
                     "handshake" -> binding.dogImage.setImageResource(R.drawable.hand_shake_image)
                     else -> binding.dogImage.setImageResource(R.drawable.sit_image)
                    }
                }
            })

        binding.mic.setOnClickListener {
            convertSpeech()
        }
    }

    private fun convertSpeech() {
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault())
        activityResultLauncher.launch(intent)
    }
}