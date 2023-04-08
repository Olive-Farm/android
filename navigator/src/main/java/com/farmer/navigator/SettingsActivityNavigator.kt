package com.farmer.navigator

import android.content.Context
import android.content.Intent

interface SettingsActivityNavigator {
    fun getIntent(context: Context): Intent
}