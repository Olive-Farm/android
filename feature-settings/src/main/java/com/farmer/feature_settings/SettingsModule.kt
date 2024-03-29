package com.farmer.feature_settings

import com.farmer.navigator.SettingsActivityNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
internal abstract class SettingsModule {
    @Binds
    abstract fun bindSettingsNavigator(
        navigatorImpl: SettingsActivityNavigatorImpl
    ): SettingsActivityNavigator
}
