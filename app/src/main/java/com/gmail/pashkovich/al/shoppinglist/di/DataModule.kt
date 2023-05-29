package com.gmail.pashkovich.al.shoppinglist.di

import android.app.Application
import com.gmail.pashkovich.al.shoppinglist.data.AppDataBase
import com.gmail.pashkovich.al.shoppinglist.data.ShopListDao
import com.gmail.pashkovich.al.shoppinglist.data.ShopListRepositoryImpl
import com.gmail.pashkovich.al.shoppinglist.domain.ShopListRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @Binds
    @ApplicationScope
    fun bindShopListRepository(impl: ShopListRepositoryImpl): ShopListRepository

    companion object {

        @Provides
        @ApplicationScope
        fun provideShopListDao(
            application: Application
        ): ShopListDao {
           return AppDataBase.getInstance(application).shopListDao()
        }
    }
}