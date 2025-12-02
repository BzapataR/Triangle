package com.bzapata.triangle.di

import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    viewModelOf(::EmulatorViewModel)
}
