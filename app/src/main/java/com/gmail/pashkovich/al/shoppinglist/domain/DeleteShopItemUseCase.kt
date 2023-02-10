package com.gmail.pashkovich.al.shoppinglist.domain

class DeleteShopItemUseCase (private val shopItemRepository: ShopItemRepository) {

    fun deleteShopItem(shopItem: ShopItem) {
        shopItemRepository.deleteShopItem(shopItem)
    }
}