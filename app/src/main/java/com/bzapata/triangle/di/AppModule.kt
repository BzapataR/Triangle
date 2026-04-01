package com.bzapata.triangle.di

import com.bzapata.triangle.data.controller.ControllerManager
import com.bzapata.triangle.data.repository.ConfigRepository
import com.bzapata.triangle.deltaCoreKt.Delta
import com.bzapata.triangle.emulatorScreen.data.GameDataBase.GamesDb
import com.bzapata.triangle.emulatorScreen.data.GameRepository
import com.bzapata.triangle.emulatorScreen.data.romsDatabase.SavedRomsDb
import com.bzapata.triangle.emulatorScreen.presentation.EmulatorViewModel
import com.bzapata.triangle.intro.IntroViewModel
import com.bzapata.triangle.intro.paths.PathsSetupViewModel as IntroPathsViewModel
import com.bzapata.triangle.settings.Paths.PathsViewModel as SettingsPathsViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {

    single {
        ConfigRepository(context = get())
    }

    single {
        GamesDb.getDataBase(context = get())
    }
    single {
        get<GamesDb>().gameDbDoa()
    }

    single {
        SavedRomsDb.getRomsDatabase(context = get())
    }
    single {
        ControllerManager(context = get())
    }
    single { get<SavedRomsDb>().romsDbDoa() }

    single { Delta(context = get()) }

    singleOf(::GameRepository)
    viewModelOf(::EmulatorViewModel)
    viewModelOf(::IntroViewModel)
    viewModelOf(::IntroPathsViewModel)
    viewModelOf(::SettingsPathsViewModel)
}
