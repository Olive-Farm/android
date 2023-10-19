package com.farmer.feature_settings

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.lifecycleScope
import com.farmer.data.repository.OliveRepository
import com.farmer.feature_settings.databinding.ActivitySettingsBinding
import com.farmer.feature_settings.util.ReadMessageHelper
import kotlinx.coroutines.launch

class SettingsActivity: ComponentActivity() {

    private lateinit var binding : ActivitySettingsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_settings)
        binding.title.text = "OLIVE BOOK"
        binding.addCashTitle.text = "가계부 내역 추가하기"
        binding.getBySms.text = "SMS 가져오기"
        binding.getBySmsDescription.text = "문자로부터 가계부 내역을 입력합니다."
        binding.getByOcr.text = "영수증 스캔하기"
        binding.getByOcrDescription.text ="영수증을 카메라로 스캔하여 가계부 내역을 입력합니다."



        binding.SMS.setOnClickListener {
            val messageHelper = ReadMessageHelper
            val currentMessage = messageHelper.readSMSMessage(this)
            Log.e("@@@settings", "currentMessage : $currentMessage")

        }
        binding.OCR.setOnClickListener {

        }
    }
}
