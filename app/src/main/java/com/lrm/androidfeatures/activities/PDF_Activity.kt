package com.lrm.androidfeatures.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lrm.androidfeatures.R
import com.lrm.androidfeatures.databinding.ActivityPdfBinding

class PDF_Activity : AppCompatActivity() {

    private lateinit var binding: ActivityPdfBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPdfBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.backIcon.setOnClickListener { onBackPressed() }

        binding.pdfView.fromAsset("panda_result.pdf").load()
    }
}