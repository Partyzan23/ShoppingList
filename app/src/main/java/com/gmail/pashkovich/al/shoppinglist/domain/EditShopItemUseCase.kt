package com.gmail.pashkovich.al.shoppinglist.domain

class EditShopItemUseCase(private val shopItemRepository: ShopItemRepository) {

    fun editShopItem(shopItem: ShopItem){
        shopItemRepository.editShopItem(shopItem)
    }
}