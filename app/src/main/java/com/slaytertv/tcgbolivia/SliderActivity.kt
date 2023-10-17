package com.slaytertv.tcgbolivia

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.slaytertv.tcgbolivia.databinding.ActivitySliderBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SliderActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySliderBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySliderBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}