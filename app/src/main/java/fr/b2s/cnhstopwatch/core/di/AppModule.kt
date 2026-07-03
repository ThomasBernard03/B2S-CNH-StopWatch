package fr.b2s.cnhstopwatch.core.di

import fr.b2s.cnhstopwatch.data.local.CNHDatabase
import fr.b2s.cnhstopwatch.data.repositories.StopwatchRepositoryImpl
import fr.b2s.cnhstopwatch.domain.repositories.StopwatchRepository
import fr.b2s.cnhstopwatch.domain.usecases.CreateStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.DeleteStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.GetAllStopwatchesUseCase
import fr.b2s.cnhstopwatch.domain.usecases.GetStopwatchByIdUseCase
import fr.b2s.cnhstopwatch.domain.usecases.ResetStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.StartStopwatchUseCase
import fr.b2s.cnhstopwatch.domain.usecases.StopStopwatchUseCase
import fr.b2s.cnhstopwatch.presentation.newStopwatch.NewStopwatchViewModel
import fr.b2s.cnhstopwatch.presentation.stopwatchDetail.StopwatchDetailViewModel
import fr.b2s.cnhstopwatch.presentation.stopwatchList.StopwatchListViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    single { CNHDatabase.getDatabase(androidContext()) }
    single { get<CNHDatabase>().stopwatchDao() }
    singleOf(::StopwatchRepositoryImpl) bind StopwatchRepository::class
    singleOf(::CreateStopwatchUseCase)
    singleOf(::GetAllStopwatchesUseCase)
    singleOf(::GetStopwatchByIdUseCase)
    singleOf(::StartStopwatchUseCase)
    singleOf(::StopStopwatchUseCase)
    singleOf(::ResetStopwatchUseCase)
    singleOf(::DeleteStopwatchUseCase)
    viewModelOf(::NewStopwatchViewModel)
    viewModelOf(::StopwatchListViewModel)
    viewModelOf(::StopwatchDetailViewModel)
}


