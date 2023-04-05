package com.farmer.feature_settings

import android.content.Context
import android.content.Intent
import com.farmer.navigator.SettingsActivityNavigator
import javax.inject.Inject

class SettingsActivityNavigatorImpl @Inject constructor(): SettingsActivityNavigator {
    override fun getIntent(context: Context): Intent {
        return Intent(context, SettingsActivity::class.java)
    }
}