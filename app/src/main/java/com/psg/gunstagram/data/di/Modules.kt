package com.psg.gunstagram.data.di

import com.psg.gunstagram.view.login.LoginViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {

}

val viewModelModule = module {
    viewModel { LoginViewModel() }
}

val repositoryModule = module {

}