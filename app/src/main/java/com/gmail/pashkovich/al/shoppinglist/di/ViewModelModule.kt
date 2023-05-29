package com.gmail.pashkovich.al.shoppinglist.di

import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.shoppinglist.presentation.MainViewModel
import com.gmail.pashkovich.al.shoppinglist.presentation.ShopItemViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface ViewModelModule {

    @IntoMap
    @ViewModelKey(MainViewModel::class)
    @Binds
    fun bindMainViewModel(impl: MainViewModel): ViewModel

    @IntoMap
    @ViewModelKey(ShopItemViewModel::class)
    @Binds
    fun bindShopItemViewModel(impl: ShopItemViewModel): ViewModel
}