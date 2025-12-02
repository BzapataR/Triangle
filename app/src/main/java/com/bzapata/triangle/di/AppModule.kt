package com.bzapata.triangle.di

import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single{
        ConfigRepository(context = get())
    }

    viewModelOf(::EmulatorViewModel)
}
