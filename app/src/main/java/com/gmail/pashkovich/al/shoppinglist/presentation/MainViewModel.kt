package com.gmail.pashkovich.al.shoppinglist.presentation

import androidx.lifecycle.ViewModel
import com.gmail.pashkovich.al.shoppinglist.data.ShopListRepositoryImpl
import com.gmail.pashkovich.al.shoppinglist.domain.DeleteShopItemUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.EditShopItemUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.GetShopListUseCase
import com.gmail.pashkovich.al.shoppinglist.domain.ShopItem

class MainViewModel : ViewModel() {

    private val repository = ShopListRepositoryImpl

    private val getShopListUseCase = GetShopListUseCase(repository)
    private val deleteShopItemUseCase = DeleteShopItemUseCase(repository)
    private val editShopItemUseCase = EditShopItemUseCase(repository)

    val shopList = getShopListUseCase.getShopList()

    fun deleteShopItem(shopItem: ShopItem) {
        deleteShopItemUseCase.deleteShopItem(shopItem)
    }

    fun changeEnabledState(shopItem: ShopItem) {
        val newItem = shopItem.copy(enabled = !shopItem.enabled)
        deleteShopItemUseCase.deleteShopItem(newItem)
    }
}