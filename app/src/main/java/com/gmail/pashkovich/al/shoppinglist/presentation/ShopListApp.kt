package com.gmail.pashkovich.al.shoppinglist.presentation

import android.app.Application
import com.gmail.pashkovich.al.shoppinglist.di.DaggerApplicationComponent

class ShopListApp : Application() {

    val component by lazy {
        DaggerApplicationComponent.factory()
            .create(this)
    }
}