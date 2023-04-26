package com.farmer.feature_settings

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import com.farmer.feature_settings.databinding.ActivitySettingsBinding

class SettingsActivity: ComponentActivity() {
    private lateinit var binding : ActivitySettingsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.tvTemp.text = "이런 식으로 연결"
    }
}