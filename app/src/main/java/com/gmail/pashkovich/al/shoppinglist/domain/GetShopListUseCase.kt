package com.gmail.pashkovich.al.shoppinglist.domain

class GetShopListUseCase(private val shopItemRepository: ShopItemRepository) {

    fun getShopList(): List<ShopItem> {
        return shopItemRepository.getShopList()
    }
}