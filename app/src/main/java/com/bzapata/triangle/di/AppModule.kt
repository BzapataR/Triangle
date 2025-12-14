package com.bzapata.triangle.di

import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.emulatorScreen.data.GameDataBase.GamesDb
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import com.bzapata.triangle.intro.IntroViewModel
import com.bzapata.triangle.intro.paths.PathsViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single {
        ConfigRepository(context = get())
    }
    single {
        GamesDb.getDataBase(context = androidContext())
    }
    single {
        get<GamesDb>().gameDbDoa()
    }
    singleOf(::GameRepository)
    viewModelOf(::EmulatorViewModel)
    viewModelOf(::IntroViewModel)
    viewModelOf(::PathsViewModel)
}
