package com.psg.gunstagram.data.di

import com.psg.gunstagram.view.login.LoginViewModel
import com.psg.gunstagram.view.main.MainViewModel
import com.psg.gunstagram.view.photo.AddPhotoViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { LoginViewModel() }
    viewModel { MainViewModel() }
    viewModel { AddPhotoViewModel() }
}

val repositoryModule = module {

}