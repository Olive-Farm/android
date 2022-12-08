package com.farmer.home.di

import com.farmer.home.data.CashBookRepository
import com.farmer.home.data.CashBookRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent

@Module
@InstallIn(ViewModelComponent::class)
abstract class CashBookCommonModule {

    @Binds
    abstract fun bindCashBookRepository(
        cashBookRepositoryImpl: CashBookRepositoryImpl
    ): CashBookRepository
}
